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

package io.github.code13.javastack.books.jcip.ch6_task_execution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SingleThreadWebServer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/10/2021 9:27 AM
 */
public class LifecycleWebServer {

  private static final int NTHREADS = 100;

  private static final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);

  public static void main(String[] args) throws IOException {
    ServerSocket socket = new ServerSocket(80);
    while (!executor.isShutdown()) {
      try {
        Socket connection = socket.accept();
        executor.execute(() -> handleRequest(connection));
      } catch (RejectedExecutionException e) {
        if (!executor.isShutdown()) {
          log("task submission rejected", e);
        }
      }
    }
  }

  public static void stop() {
    executor.shutdown();
  }

  private static void log(String msg, Exception e) {
    Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
  }

  private static void handleRequest(Socket connection) {
    // handle request
  }
}
