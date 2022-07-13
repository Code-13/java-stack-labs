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

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteMap.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/20 22:12
 */
public class ReadWriteMap<K, V> {

  private final Map<K, V> map;

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private final Lock readLock = readWriteLock.readLock();
  private final Lock writeLock = readWriteLock.writeLock();

  public ReadWriteMap(Map<K, V> map) {
    this.map = map;
  }

  public V put(K k, V v) {
    writeLock.lock();
    try {
      return map.put(k, v);
    } finally {
      writeLock.unlock();
    }
  }

  public V get(K k) {
    readLock.lock();
    try {
      return map.get(k);
    } finally {
      readLock.unlock();
    }
  }
}
