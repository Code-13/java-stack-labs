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

package io.github.code13.libs.redisson.frameworks.spring;

import io.github.code13.libs.redisson.RedissonClientBuilder;
import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

/**
 * SpringRedissonApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/18 12:34
 */
@SpringBootApplication
public class SpringRedissonApp {

  public static void main(String[] args) {
    SpringApplication.run(SpringRedissonApp.class, args);
  }

  @Bean(destroyMethod = "shutdown")
  public static RedissonClient redissonClient(RedisProperties properties) {
    return RedissonClientBuilder.build(
        properties.getHost(),
        properties.getPort(),
        properties.getPassword(),
        properties.getDatabase());
  }
}
