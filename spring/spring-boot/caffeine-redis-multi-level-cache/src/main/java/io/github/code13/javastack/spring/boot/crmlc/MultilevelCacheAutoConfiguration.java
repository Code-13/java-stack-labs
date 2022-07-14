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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * MultilevelCacheAutoConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 22:47
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheConfigProperties.class)
class MultilevelCacheAutoConfiguration {

  @Bean
  @ConditionalOnBean(RedisTemplate.class)
  public RedisCaffeineCacheManager cacheManager(
      CacheConfigProperties cacheConfigProperties,
      RedisTemplate<Object, Object> stringKeyRedisTemplate) {
    return new RedisCaffeineCacheManager(cacheConfigProperties, stringKeyRedisTemplate);
  }

  @Bean
  @ConditionalOnMissingBean(name = "stringKeyRedisTemplate")
  public RedisTemplate<Object, Object> stringKeyRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    return template;
  }

  @Bean
  public RedisMessageListenerContainer cacheMessageListenerContainer(
      CacheConfigProperties cacheConfigProperties,
      RedisTemplate<Object, Object> stringKeyRedisTemplate,
      RedisCaffeineCacheManager redisCaffeineCacheManager) {
    RedisMessageListenerContainer redisMessageListenerContainer =
        new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(
        stringKeyRedisTemplate.getConnectionFactory());
    CacheMessageListener cacheMessageListener =
        new CacheMessageListener(stringKeyRedisTemplate, redisCaffeineCacheManager);
    redisMessageListenerContainer.addMessageListener(
        cacheMessageListener, new ChannelTopic(cacheConfigProperties.getRedis().getTopic()));
    return redisMessageListenerContainer;
  }
}
