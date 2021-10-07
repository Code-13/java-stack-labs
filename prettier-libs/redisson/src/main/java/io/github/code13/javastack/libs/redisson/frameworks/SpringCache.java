/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.libs.redisson.frameworks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringCache.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/5 17:39
 */
@SpringBootApplication
public class SpringCache {

  public static void main(String[] args) {
    SpringApplication.run(SpringCache.class, args);
  }

  @Configuration
  @EnableCaching
  @EnableConfigurationProperties(RedisProperties.class)
  public static class Config {

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
      Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();

      // create "testMap" cache with ttl = 24 minutes and maxIdleTime = 12 minutes
      config.put("testMap", new CacheConfig(24 * 60 * 1000, 12 * 60 * 1000));
      return new RedissonSpringCacheManager(redissonClient, config);
    }

    @Bean
    public static RedissonClient doBuild(RedisProperties properties) {
      org.redisson.config.Config config = new org.redisson.config.Config();
      SingleServerConfig singleServerConfig =
          config
              .useSingleServer()
              .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
              .setDatabase(properties.getDatabase());

      if (StringUtils.isNotBlank(properties.getPassword())) {
        singleServerConfig.setPassword(properties.getPassword());
      }

      ObjectMapper objectMapper = buildObjectMapper();

      config.setCodec(new JsonJacksonCodec(objectMapper));

      return Redisson.create(config);
    }

    private static ObjectMapper buildObjectMapper() {
      JavaTimeModule javaTimeModule = new JavaTimeModule();
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      javaTimeModule.addSerializer(
          LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
      javaTimeModule.addDeserializer(
          LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

      // 处理LocalDate
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
      javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

      // 处理LocalTime
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
      javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
      javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

      var objectMapper = new ObjectMapper();
      objectMapper.registerModule(javaTimeModule);
      return objectMapper;
    }
  }

  @RestController
  public static class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @RequestMapping(value = "/test")
    @Cacheable(cacheNames = "testMap", key = "#root.args[0]")
    public Map<String, String> test(@RequestParam String id) {
      logger.info("id 为：{}", id);
      return Map.of(id, "v1");
    }
  }
}
