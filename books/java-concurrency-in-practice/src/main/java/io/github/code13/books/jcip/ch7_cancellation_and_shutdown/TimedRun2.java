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

package io.github.code13.books.jcip.ch7_cancellation_and_shutdown;

import static io.github.code13.books.jcip.ch5_building_blocks.LaunderThrowable.launderThrowable;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TimedRun2.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 5:43 PM
 */
public class TimedRun2 {

  private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);

  public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
    class RethrowableTask implements Runnable {
      private volatile Throwable t;

      @Override
      public void run() {
        try {
          r.run();
        } catch (Throwable t) {
          this.t = t;
        }
      }

      void rethrow() {
        if (t != null) {
          throw launderThrowable(t);
        }
      }
    }

    RethrowableTask task = new RethrowableTask();
    Thread taskThread = new Thread(task);
    taskThread.start();
    cancelExec.schedule(
        new Runnable() {
          @Override
          public void run() {
            taskThread.interrupt();
          }
        },
        timeout,
        unit);
    taskThread.join(unit.toMillis(timeout));
    task.rethrow();
  }
}
