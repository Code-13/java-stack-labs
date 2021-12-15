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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MyAppThread.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/15/2021 2:41 PM
 */
public class MyAppThread extends Thread {
  public static final String DEFAULT_NAME = "MyAppThread";
  private static volatile boolean debugLifecycle = false;
  private static final AtomicInteger created = new AtomicInteger();
  private static final AtomicInteger alive = new AtomicInteger();
  private static final Logger log = Logger.getAnonymousLogger();

  public MyAppThread(Runnable r) {
    this(r, DEFAULT_NAME);
  }

  public MyAppThread(Runnable runnable, String name) {
    super(runnable, name + "-" + created.incrementAndGet());
    setUncaughtExceptionHandler(
        (t, e) -> log.log(Level.SEVERE, "UNCAUGHT in thread " + t.getName(), e));
  }

  @Override
  public void run() {
    // Copy debug flag to ensure consistent value throughout.
    boolean debug = debugLifecycle;
    if (debug) {
      log.log(Level.FINE, "Created " + getName());
    }
    try {
      alive.incrementAndGet();
      super.run();
    } finally {
      alive.decrementAndGet();
      if (debug) {
        log.log(Level.FINE, "Exiting " + getName());
      }
    }
  }

  public static int getThreadsCreated() {
    return created.get();
  }

  public static int getThreadsAlive() {
    return alive.get();
  }

  public static boolean getDebug() {
    return debugLifecycle;
  }

  public static void setDebug(boolean b) {
    debugLifecycle = b;
  }
}
