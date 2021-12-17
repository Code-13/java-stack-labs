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

package io.github.code13.javastack.books.jcip.ch11_performance_and_scalability;

import java.util.concurrent.BlockingQueue;

/**
 * WorkerThread.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/17/2021 12:21 PM
 */
public class WorkerThread extends Thread {

  private final BlockingQueue<Runnable> queue;

  public WorkerThread(BlockingQueue<Runnable> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Runnable runnable = queue.take();
        runnable.run();
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}
