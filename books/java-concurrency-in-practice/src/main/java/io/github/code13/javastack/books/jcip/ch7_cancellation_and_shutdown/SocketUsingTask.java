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

import io.github.code13.javastack.books.jcip.GuardedBy;
import io.github.code13.javastack.books.jcip.ThreadSafe;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SocketUsingTask.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/13 21:49
 */
public class SocketUsingTask<T> implements CancellableTask<T> {

  @GuardedBy("this")
  private Socket socket;

  public SocketUsingTask<T> setSocket(Socket socket) {
    this.socket = socket;
    return this;
  }

  @Override
  public void cancel() {
    try {
      if (socket != null) {
        socket.close();
      }
    } catch (IOException ignored) {
      // ignored
    }
  }

  @Override
  public RunnableFuture<T> newTask() {
    return new FutureTask<>(this) {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        try {
          SocketUsingTask.this.cancel();
        } finally {
          return super.cancel(mayInterruptIfRunning);
        }
      }
    };
  }

  @Override
  public T call() throws Exception {
    // 此处读取 Socket
    return null;
  }
}

interface CancellableTask<T> extends Callable<T> {
  void cancel();

  RunnableFuture<T> newTask();
}

@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {
  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    if (callable instanceof CancellableTask) {
      return ((CancellableTask<T>) callable).newTask();
    } else {
      return super.newTaskFor(callable);
    }
  }
}
