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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;

/**
 * CountDownLatchRunner.
 *
 * <p>Redis based distributed CountDownLatch object for Java has structure similar to CountDownLatch
 * object.
 *
 * <p>Should be initialized with count by trySetCount(count) method before usage.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:12
 */
@DisplayName("CountDownLatch")
class CountDownLatchRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("CountDownLatch")
  void execute() throws InterruptedException {
    RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("CountDownLatch");
    countDownLatch.trySetCount(5);

    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      executorService.execute(
          () -> {
            // ... do something inside runnable task
            System.out.println("i = " + finalI);

            // user countDown
            countDownLatch.countDown();
          });
    }
    executorService.shutdown();

    // await for count down
    countDownLatch.await();
  }
}
