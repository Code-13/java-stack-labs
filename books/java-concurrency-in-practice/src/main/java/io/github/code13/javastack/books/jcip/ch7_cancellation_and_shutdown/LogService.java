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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import io.github.code13.javastack.books.jcip.GuardedBy;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * LogWriter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 9:34 AM
 */
public class LogService {

  private final BlockingQueue<String> queue;
  private final LoggerThread logger;

  @GuardedBy("this")
  private boolean isShutdown;

  @GuardedBy("this")
  private int reservations;

  public LogService(BlockingQueue<String> queue, LoggerThread logger) {
    this.queue = queue;
    this.logger = logger;
  }

  public void start() {
    logger.start();
  }

  public void stop() {
    synchronized (this) {
      isShutdown = true;
    }
    logger.interrupt();
  }

  public void log(String msg) throws InterruptedException {
    synchronized (this) {
      if (isShutdown) {
        throw new IllegalStateException("");
      }
      ++reservations;
    }
    queue.put(msg);
  }

  class LoggerThread extends Thread {
    private final PrintWriter writer;

    LoggerThread(PrintWriter writer) {
      this.writer = writer;
    }

    @Override
    public void run() {
      try {
        while (true) {
          try {
            synchronized (LogService.this) {
              if (isShutdown && reservations == 0) {
                break;
              }
            }

            String msg = queue.take();

            synchronized (this) {
              --reservations;
            }

            writer.println(msg);
          } catch (InterruptedException ignored) {
            // retry
          }
        }
      } finally {
        writer.close();
      }
    }
  }
}
