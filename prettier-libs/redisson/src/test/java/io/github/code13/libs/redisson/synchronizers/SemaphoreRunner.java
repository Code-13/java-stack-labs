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

package io.github.code13.libs.redisson.synchronizers;

import io.github.code13.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

/**
 * SemaphoreRunner.
 *
 * <p>Redis based distributed Semaphore object for Java similar to Semaphore object.
 *
 * <p>Could be initialized before usage, but it's not requirement, with available permits amount
 * through trySetPermits(permits) method.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 17:15
 */
class SemaphoreRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("Semaphore")
  void test1() throws InterruptedException {
    RSemaphore semaphore = redissonClient.getSemaphore("Semaphore");
    semaphore.trySetPermits(100);

    // acquire single permit
    semaphore.acquire();

    // or acquire 10 permits
    semaphore.acquire(10);

    // or try to acquire permit
    boolean res = semaphore.tryAcquire();

    // or try to acquire permit or wait up to 15 seconds
    res = semaphore.tryAcquire(15, TimeUnit.SECONDS);

    // or try to acquire 10 permit
    res = semaphore.tryAcquire(10);

    // or try to acquire 10 permits or wait up to 15 seconds
    res = semaphore.tryAcquire(10, 15, TimeUnit.SECONDS);
    if (res) {
      try {
        System.out.println("res = " + res);
      } finally {
        semaphore.release();
      }
    }
  }
}
