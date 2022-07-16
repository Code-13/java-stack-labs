/*
 * Copyright 2022-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.code13.javastack.spring.boot.crmlc;

import com.github.benmanes.caffeine.cache.Cache;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * RedisCaffeineCache.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 15:33
 */
class RedisCaffeineCache extends AbstractValueAdaptingCache {

  private static final Logger logger = LoggerFactory.getLogger(RedisCaffeineCache.class);

  private final String name;
  private final Cache<Object, Object> caffeineCache;
  private final RedisTemplate<Object, Object> stringKeyRedisTemplate;
  private final String cachePrefix;

  private final Duration defaultExpiration;

  private final Map<String, Duration> expires;

  private final String topic;

  private final Map<String, ReentrantLock> keyLockMap = new ConcurrentHashMap<>();

  public RedisCaffeineCache(
      String name,
      RedisTemplate<Object, Object> stringKeyRedisTemplate,
      Cache<Object, Object> caffeineCache,
      CacheConfigProperties cacheConfigProperties) {
    super(cacheConfigProperties.isCacheNullValues());
    this.name = name;
    this.caffeineCache = caffeineCache;
    this.stringKeyRedisTemplate = stringKeyRedisTemplate;
    cachePrefix = cacheConfigProperties.getCachePrefix();
    defaultExpiration = cacheConfigProperties.getRedis().getDefaultExpiration();
    expires = cacheConfigProperties.getRedis().getExpires();
    topic = cacheConfigProperties.getRedis().getTopic();
  }

  @Override
  protected Object lookup(Object key) {
    Object cacheKey = getKey(key);
    Object value = caffeineCache.getIfPresent(key);
    if (value != null) {
      logger.debug("get cache from caffeine, the key is : {}", cacheKey);
      return value;
    }

    // 避免自动一个 RedisTemplate 覆盖失效
    stringKeyRedisTemplate.setKeySerializer(RedisSerializer.string());
    value = stringKeyRedisTemplate.opsForValue().get(cacheKey);

    if (value != null) {
      logger.debug("get cache from redis and put in caffeine, the key is : {}", cacheKey);
      caffeineCache.put(key, value);
    }
    return value;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getNativeCache() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    Object value = lookup(key);
    if (value != null) {
      return (T) value;
    }

    ReentrantLock lock =
        keyLockMap.computeIfAbsent(
            key.toString(),
            s -> {
              logger.trace("create lock for key : {}", s);
              return new ReentrantLock();
            });

    lock.lock();
    try {
      value = lookup(key);
      if (value != null) {
        return (T) value;
      }
      value = valueLoader.call();
      Object storeValue = toStoreValue(value);
      put(key, storeValue);
      return (T) value;
    } catch (Exception e) {
      throw new ValueRetrievalException(key, valueLoader, e.getCause());
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void put(Object key, Object value) {
    if (!super.isAllowNullValues() && value == null) {
      evict(key);
      return;
    }
    doPut(key, value);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    Object cacheKey = getKey(key);
    Object prevValue;
    // 考虑使用分布式锁，或者将redis的setIfAbsent改为原子性操作
    synchronized (key) {
      prevValue = stringKeyRedisTemplate.opsForValue().get(cacheKey);
      if (prevValue == null) {
        doPut(key, value);
      }
    }
    return toValueWrapper(prevValue);
  }

  @Override
  public void evict(Object key) {
    // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
    stringKeyRedisTemplate.delete(getKey(key));

    push(new CacheMessage(name, key));

    caffeineCache.invalidate(key);
  }

  @Override
  public void clear() {
    // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
    Set<Object> keys = stringKeyRedisTemplate.keys(name.concat(":*"));

    if (!CollectionUtils.isEmpty(keys)) {
      stringKeyRedisTemplate.delete(keys);
    }

    push(new CacheMessage(name, null));

    caffeineCache.invalidateAll();
  }

  private void doPut(Object key, Object value) {
    Duration expire = getExpire();
    value = toStoreValue(value);
    if (!expire.isNegative()) {
      stringKeyRedisTemplate.opsForValue().set(getKey(key), value, expire);
    } else {
      stringKeyRedisTemplate.opsForValue().set(getKey(key), value);
    }

    push(new CacheMessage(name, key));

    caffeineCache.put(key, value);
  }

  private Object getKey(Object key) {
    return name.concat(":")
        .concat(
            StringUtils.hasText(cachePrefix)
                ? key.toString()
                : cachePrefix.concat(":").concat(key.toString()));
  }

  private Duration getExpire() {
    Duration cacheNameExpire = expires.get(name);
    return cacheNameExpire == null ? defaultExpiration : cacheNameExpire;
  }

  private void push(CacheMessage message) {
    stringKeyRedisTemplate.convertAndSend(topic, message);
  }

  public void clearLocal(Object key) {
    logger.debug("clear local cache, the key is : {}", key);
    if (key == null) {
      caffeineCache.invalidateAll();
    } else {
      caffeineCache.invalidate(key);
    }
  }
}
