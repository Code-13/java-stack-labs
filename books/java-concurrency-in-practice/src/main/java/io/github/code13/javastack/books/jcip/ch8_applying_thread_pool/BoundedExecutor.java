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

package io.github.code13.javastack.books.jcip.ch8_applying_thread_pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * BoundedExecutor.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/15/2021 2:27 PM
 */
public class BoundedExecutor {

  private final ExecutorService executorService;
  private final Semaphore semaphore;

  public BoundedExecutor(ExecutorService executorService, Semaphore semaphore) {
    this.executorService = executorService;
    this.semaphore = semaphore;
  }

  public void submitTask(Runnable command) throws InterruptedException {
    semaphore.acquire();
    try {
      executorService.execute(
          () -> {
            try {
              command.run();
            } finally {
              semaphore.release();
            }
          });
    } catch (RejectedExecutionException e) {
      semaphore.release();
    }
  }
}
