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

package io.github.code13.javastack.books.jcip.ch14_building_custom_synchronizers;

/**
 * SleepyBoundedBuffer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/21/2021 11:43 AM
 */
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

  private static final long SLEEP_GRANULARITY = 1000;

  public SleepyBoundedBuffer(int capacity) {
    super(capacity);
  }

  public void put(V v) throws InterruptedException {
    while (true) {
      synchronized (this) {
        if (!isFull()) {
          doPut(v);
          return;
        }
      }
      Thread.sleep(SLEEP_GRANULARITY);
    }
  }

  public V take() throws InterruptedException {
    while (true) {
      synchronized (this) {
        if (!isEmpty()) {
          return doTake();
        }
      }
      Thread.sleep(SLEEP_GRANULARITY);
    }
  }
}
