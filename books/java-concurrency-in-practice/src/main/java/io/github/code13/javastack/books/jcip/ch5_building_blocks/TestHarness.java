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

package io.github.code13.javastack.books.jcip.ch5_building_blocks;

import java.util.concurrent.CountDownLatch;

/**
 * TestHarness.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/8/2021 3:21 PM
 */
public class TestHarness {

  public long timeTasks(int nThread, Runnable task) throws InterruptedException {
    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch endGate = new CountDownLatch(nThread);
    for (int i = 0; i < nThread; i++) {
      new Thread(
              () -> {
                try {
                  startGate.await();

                  try {
                    task.run();
                  } finally {
                    endGate.countDown();
                  }

                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              })
          .start();
    }

    long start = System.nanoTime();
    startGate.countDown();
    endGate.await();
    long end = System.nanoTime();

    return end - start;
  }
}
