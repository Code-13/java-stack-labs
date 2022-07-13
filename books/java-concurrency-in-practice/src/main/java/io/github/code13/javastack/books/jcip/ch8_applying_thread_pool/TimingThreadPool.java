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

package io.github.code13.javastack.books.jcip.ch8_applying_thread_pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * TimingThreadPool.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/15/2021 3:04 PM
 */
public class TimingThreadPool extends ThreadPoolExecutor {

  private static final ThreadLocal<Long> startTime = new ThreadLocal<>();
  private final Logger log = Logger.getLogger("TimingThreadPool");
  private final AtomicLong numTasks = new AtomicLong();
  private final AtomicLong totalTime = new AtomicLong();

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
    log.fine(String.format("Thread %s: start %s", t, r));
    startTime.set(System.nanoTime());
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    try {
      long endTime = System.nanoTime();
      long taskTime = endTime - startTime.get();
      numTasks.incrementAndGet();
      totalTime.addAndGet(taskTime);
      log.fine(String.format("Thread %s : end %s, time= %dns", t, r, taskTime));
    } finally {
      super.afterExecute(r, t);
    }
  }

  @Override
  protected void terminated() {
    try {
      log.info(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
    } finally {
      super.terminated();
    }
  }

  public TimingThreadPool(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public TimingThreadPool(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  public TimingThreadPool(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public TimingThreadPool(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }
}
