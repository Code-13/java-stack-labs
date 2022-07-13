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

package io.github.code13.javastack.libs.redisson.objects;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * RateLimiterRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 15:23
 */
@DisplayName("RateLimiter")
class RateLimiterRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("")
  void test1() {
    var rateLimiter = redissonClient.getRateLimiter("RateLimiter");
    rateLimiter.trySetRate(RateType.OVERALL, 5, 2, RateIntervalUnit.SECONDS);

    var availablePermits = rateLimiter.availablePermits();
    System.out.println(availablePermits);

    var executorService = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 10; ++i) {
      int finalI = i;
      executorService.submit(
          () -> {
            rateLimiter.acquire(1);
            System.out.println(finalI + "哈哈哈");
          });
    }

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
