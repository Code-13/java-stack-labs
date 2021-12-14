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

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

/**
 * LogWriter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 9:34 AM
 */
public class LogWriter {

  private final BlockingQueue<String> queue;
  private final LoggerThread logger;

  public LogWriter(BlockingQueue<String> queue, LoggerThread logger) {
    this.queue = queue;
    this.logger = logger;
  }

  public void start() {
    logger.start();
  }

  public void log(String msg) throws InterruptedException {
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
          writer.println(queue.take());
        }
      } catch (InterruptedException ignored) {
        // ignored
      } finally {
        writer.close();
      }
    }
  }
}
