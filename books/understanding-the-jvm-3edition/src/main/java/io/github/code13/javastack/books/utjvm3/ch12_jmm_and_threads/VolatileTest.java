/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.utjvm3.ch12_jmm_and_threads;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * VolatileTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 1/4/2022 2:09 PM
 */
public class VolatileTest {

  static volatile int race = 0;

  static void increase() {
    race++;
  }

  static final int THREAD_COUNT = 20;

  @Test
  @DisplayName("test")
  void test() throws InterruptedException {
    Thread[] threads = new Thread[THREAD_COUNT];

    for (int i = 0; i < THREAD_COUNT; i++) {
      threads[i] =
          new Thread(
              () -> {
                for (int j = 0; j < 10000; j++) {
                  increase();
                }
              });

      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    System.out.println(race);
  }
}
