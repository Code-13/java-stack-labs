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

package io.github.code13.javastack.books.jcip.ch14_building_custom_synchronizers;

/**
 * BoundedBuffer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/21/2021 12:04 PM
 */
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {

  protected BoundedBuffer(int capacity) {
    super(capacity);
  }

  public synchronized void put(V v) throws InterruptedException {
    while (isFull()) {
      wait();
    }
    doPut(v);
    notifyAll();
  }

  public synchronized V take() throws InterruptedException {
    while (isEmpty()) {
      wait();
    }
    V v = doTake();
    notifyAll();
    return v;
  }
}
