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

package io.github.code13.spring.boot.crmlc;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RedisCaffeineCacheManager.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 22:31
 */
class RedisCaffeineCacheManager implements CacheManager {

  private static final Logger logger = LoggerFactory.getLogger(RedisCaffeineCacheManager.class);

  private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

  private final CacheConfigProperties cacheConfigProperties;

  private final RedisTemplate<Object, Object> stringKeyRedisTemplate;

  private final boolean dynamic;

  private final Set<String> cacheNames;

  public RedisCaffeineCacheManager(
      CacheConfigProperties cacheConfigProperties,
      RedisTemplate<Object, Object> stringKeyRedisTemplate) {
    super();
    this.cacheConfigProperties = cacheConfigProperties;
    this.stringKeyRedisTemplate = stringKeyRedisTemplate;
    dynamic = cacheConfigProperties.isDynamic();
    cacheNames = cacheConfigProperties.getCacheNames();
  }

  @Override
  public Cache getCache(String name) {
    Cache cache = cacheMap.get(name);
    if (cache != null) {
      return cache;
    }
    if (!dynamic && !cacheNames.contains(name)) {
      return cache;
    }
    cache =
        new RedisCaffeineCache(
            name, stringKeyRedisTemplate, caffeineCache(), cacheConfigProperties);
    Cache oldCache = cacheMap.putIfAbsent(name, cache);
    logger.debug("create cache instance, the cache name is : {}", name);
    return oldCache == null ? cache : oldCache;
  }

  public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
    Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
    doIfPresent(
        cacheConfigProperties.getCaffeine().getExpireAfterAccess(),
        cacheBuilder::expireAfterAccess);
    doIfPresent(
        cacheConfigProperties.getCaffeine().getExpireAfterWrite(), cacheBuilder::expireAfterWrite);
    doIfPresent(
        cacheConfigProperties.getCaffeine().getRefreshAfterWrite(),
        cacheBuilder::refreshAfterWrite);

    if (cacheConfigProperties.getCaffeine().getInitialCapacity() > 0) {
      cacheBuilder.initialCapacity(cacheConfigProperties.getCaffeine().getInitialCapacity());
    }
    if (cacheConfigProperties.getCaffeine().getMaximumSize() > 0) {
      cacheBuilder.maximumSize(cacheConfigProperties.getCaffeine().getMaximumSize());
    }
    if (cacheConfigProperties.getCaffeine().getKeyStrength() != null) {
      switch (cacheConfigProperties.getCaffeine().getKeyStrength()) {
        case WEAK:
          cacheBuilder.weakKeys();
          break;
        case SOFT:
          throw new UnsupportedOperationException("caffeine 不支持 key 软引用");
        default:
      }
    }
    if (cacheConfigProperties.getCaffeine().getValueStrength() != null) {
      switch (cacheConfigProperties.getCaffeine().getValueStrength()) {
        case WEAK:
          cacheBuilder.weakValues();
          break;
        case SOFT:
          cacheBuilder.softValues();
        default:
      }
    }
    return cacheBuilder.build();
  }

  protected static void doIfPresent(Duration duration, Consumer<Duration> consumer) {
    if (duration != null && !duration.isNegative()) {
      consumer.accept(duration);
    }
  }

  @Override
  public Collection<String> getCacheNames() {
    return cacheNames;
  }

  public void clearLocal(String cacheName, Object key) {
    Cache cache = cacheMap.get(cacheName);
    if (cache == null) {
      return;
    }

    RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
    redisCaffeineCache.clearLocal(key);
  }
}
