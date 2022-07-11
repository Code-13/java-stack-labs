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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * CacheMessageListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 22:31
 */
public class CacheMessageListener implements MessageListener {

  private static final Logger logger = LoggerFactory.getLogger(CacheMessageListener.class);

  private final RedisTemplate<Object, Object> redisTemplate;

  private final RedisCaffeineCacheManager redisCaffeineCacheManager;

  public CacheMessageListener(
      RedisTemplate<Object, Object> redisTemplate,
      RedisCaffeineCacheManager redisCaffeineCacheManager) {
    this.redisTemplate = redisTemplate;
    this.redisCaffeineCacheManager = redisCaffeineCacheManager;
  }

  @Override
  public void onMessage(Message message, byte[] pattern) {
    CacheMessage cacheMessage =
        (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
    logger.debug(
        "recevice a redis topic message, clear local cache, the cacheName is {}, the key is {}",
        cacheMessage.getCacheName(),
        cacheMessage.getKey());
    redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
  }
}
