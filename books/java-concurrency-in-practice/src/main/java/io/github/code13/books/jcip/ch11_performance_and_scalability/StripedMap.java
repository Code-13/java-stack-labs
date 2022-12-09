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

package io.github.code13.books.jcip.ch11_performance_and_scalability;

import io.github.code13.books.jcip.ThreadSafe;

/**
 * StripedMap.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/19 15:13
 */
@ThreadSafe
public class StripedMap {

  // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
  private static final int N_LOCKS = 16;
  private final Node[] buckets;
  private final Object[] locks;

  private static class Node {
    Node next;
    Object key;
    Object value;
  }

  public StripedMap(int numBuckets) {
    buckets = new Node[numBuckets];
    locks = new Object[N_LOCKS];
    for (int i = 0; i < N_LOCKS; i++) {
      locks[i] = new Object();
    }
  }

  private int hash(Object key) {
    return Math.abs(key.hashCode() % buckets.length);
  }

  public Object get(Object key) {
    int hash = hash(key);
    synchronized (locks[hash % N_LOCKS]) {
      for (Node m = buckets[hash]; m != null; m = m.next) {
        if (m.key.equals(key)) {
          return m.value;
        }
      }
    }
    return null;
  }

  public void clear() {
    for (int i = 0; i < buckets.length; i++) {
      synchronized (locks[i % N_LOCKS]) {
        buckets[i] = null;
      }
    }
  }
}
