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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TrackingExecutor.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 10:52 AM
 */
public class TrackingExecutor extends AbstractExecutorService {

  private final ExecutorService executor;
  private final Set<Runnable> taskCancelledAtShutdown =
      Collections.synchronizedSet(new HashSet<>());

  public TrackingExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public void shutdown() {
    executor.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return executor.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return executor.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return executor.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return executor.awaitTermination(timeout, unit);
  }

  @Override
  public void execute(Runnable command) {
    executor.execute(
        () -> {
          try {
            command.run();
          } finally {
            if (isShutdown() && Thread.currentThread().isInterrupted()) {
              taskCancelledAtShutdown.add(command);
            }
          }
        });
  }

  public List<Runnable> getCancelledTasks() {
    return new ArrayList<>(taskCancelledAtShutdown);
  }
}
