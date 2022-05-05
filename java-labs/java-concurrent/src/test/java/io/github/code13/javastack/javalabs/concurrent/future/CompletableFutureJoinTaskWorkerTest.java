/*
 *
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

package io.github.code13.javastack.javalabs.concurrent.future;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * CompletableFutureJoinTaskWorkerTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/27 20:33
 */
class CompletableFutureJoinTaskWorkerTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getAllResult() {

    List<Long> list = List.of(1000L, 2000L, 3000L);

    CompletableFutureJoinTaskWorker<Long, String> futureTaskWorker =
        new CompletableFutureJoinTaskWorker<>(list, this::getThreadName);

    long begin = System.currentTimeMillis();
    List<String> allResult = futureTaskWorker.getAllResult();
    long end = System.currentTimeMillis();

    System.out.println(allResult);
    System.out.println("结束耗时:" + (end - begin));
    Assertions.assertNotNull(allResult);
  }

  private CompletableFuture<String> getThreadName(long sleepTime) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            Thread.sleep(sleepTime);
            System.out.println(Thread.currentThread().getName() + "已经睡眠" + sleepTime + "毫秒");
            return Thread.currentThread().getName();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          return null;
        });
  }
}
