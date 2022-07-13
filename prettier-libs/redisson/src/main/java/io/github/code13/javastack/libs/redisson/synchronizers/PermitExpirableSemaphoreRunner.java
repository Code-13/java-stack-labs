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

package io.github.code13.javastack.libs.redisson.synchronizers;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;

/**
 * PermitExpirableSemaphoreRunner.
 *
 * <p>Redis based distributed Semaphore object for Java with lease time parameter support for each
 * acquired permit. Each permit identified by own id and could be released only using its id.
 *
 * <p>Should be initialized before usage with available permits amount through
 * trySetPermits(permits) method. Allows to increase/decrease number of available permits through
 * addPermits(permits) method.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:09
 */
@DisplayName("PermitExpirableSemaphore")
class PermitExpirableSemaphoreRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("PermitExpirableSemaphore")
  void exe() throws InterruptedException {
    RPermitExpirableSemaphore semaphore =
        redissonClient.getPermitExpirableSemaphore("PermitExpirableSemaphore");
    semaphore.trySetPermits(23);

    // acquire permit
    String id = semaphore.acquire();

    // or acquire permit with lease time in 10 seconds
    id = semaphore.acquire(10, TimeUnit.SECONDS);

    // or try to acquire permit
    id = semaphore.tryAcquire();

    // or try to acquire permit or wait up to 15 seconds
    id = semaphore.tryAcquire(15, TimeUnit.SECONDS);

    // or try to acquire permit with least time 15 seconds or wait up to 10 seconds
    id = semaphore.tryAcquire(10, 15, TimeUnit.SECONDS);
    if (id != null) {
      try {
        System.out.println("id = " + id);
      } finally {
        semaphore.release(id);
      }
    }
  }
}
