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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ConditionBoundedBuffer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/21/2021 4:11 PM
 */
@ThreadSafe
public class ConditionBoundedBuffer<T> {

  protected final Lock lock = new ReentrantLock();

  private final Condition notFull = lock.newCondition();

  private final Condition notEmpty = lock.newCondition();

  private final T[] items = (T[]) new Object[1024];

  @GuardedBy("lock")
  private int tail, head, count;

  public void put(T t) throws InterruptedException {
    lock.lock();
    try {
      while (count == items.length) {
        notFull.await();
      }
      items[tail] = t;
      if (++tail == items.length) {
        tail = 0;
      }
      ++count;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public T take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0) {
        notEmpty.await();
      }
      T t = items[head];
      items[head] = null;
      if (++head == items.length) {
        head = 0;
      }
      --count;
      notFull.signal();
      return t;
    } finally {
      lock.unlock();
    }
  }
}
