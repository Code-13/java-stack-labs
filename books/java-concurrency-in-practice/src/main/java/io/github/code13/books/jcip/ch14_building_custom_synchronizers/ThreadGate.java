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

package io.github.code13.books.jcip.ch14_building_custom_synchronizers;

import io.github.code13.books.jcip.GuardedBy;
import io.github.code13.books.jcip.ThreadSafe;

/**
 * ThreadGate.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/21/2021 3:46 PM
 */
@ThreadSafe
public class ThreadGate {

  @GuardedBy("this")
  private boolean isOpen;

  @GuardedBy("this")
  private int generation;

  public synchronized void close() {
    isOpen = false;
  }

  public synchronized void open() {
    ++generation;
    isOpen = true;
    notifyAll();
  }

  public synchronized void await() throws InterruptedException {
    int arrivalGeneration = generation;
    while (!isOpen && arrivalGeneration == generation) {
      wait();
    }
  }
}
