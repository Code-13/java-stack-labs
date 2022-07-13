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

package io.github.code13.javastack.books.jcip.ch13_explicit_locks;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TimedLocking.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/20 21:25
 */
public class TimedLocking {

  private Lock lock = new ReentrantLock();

  public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit)
      throws InterruptedException {
    long nanosToLock = unit.toNanos(timeout) - estimatedNanosToSend(message);
    if (!lock.tryLock(nanosToLock, NANOSECONDS)) {
      return false;
    }
    try {
      return sendOnSharedLine(message);
    } finally {
      lock.unlock();
    }
  }

  private boolean sendOnSharedLine(String message) {
    /* send something */
    return true;
  }

  long estimatedNanosToSend(String message) {
    return message.length();
  }
}
