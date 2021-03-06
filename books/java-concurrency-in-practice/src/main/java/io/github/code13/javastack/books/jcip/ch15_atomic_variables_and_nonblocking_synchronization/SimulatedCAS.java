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

package io.github.code13.javastack.books.jcip.ch15_atomic_variables_and_nonblocking_synchronization;

import io.github.code13.javastack.books.jcip.GuardedBy;
import io.github.code13.javastack.books.jcip.ThreadSafe;

/**
 * SimulatedCAS.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/22/2021 10:26 AM
 */
@ThreadSafe
public class SimulatedCAS {

  @GuardedBy("this")
  private int value;

  public synchronized int get() {
    return value;
  }

  public synchronized int compareAndSwap(int expectedValue, int newValue) {
    int oldValue = value;
    if (oldValue == expectedValue) {
      value = newValue;
    }
    return oldValue;
  }

  public synchronized boolean compareAndSet(int expectedValue, int newValue) {
    return expectedValue == compareAndSwap(expectedValue, newValue);
  }
}
