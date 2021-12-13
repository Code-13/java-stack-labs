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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import io.github.code13.javastack.books.jcip.ch5_building_blocks.LaunderThrowable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * TimedRun.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/13 21:18
 */
public class TimedRun {

  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public static void timeRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
    Future<?> future = executor.submit(r);

    try {
      future.get(timeout, unit);
    } catch (ExecutionException e) {
      throw LaunderThrowable.launderThrowable(e);
    } catch (TimeoutException e) {
      // task will be cancelled below
    } finally {
      // Harmless if task already completed
      future.cancel(true);
    }
  }
}
