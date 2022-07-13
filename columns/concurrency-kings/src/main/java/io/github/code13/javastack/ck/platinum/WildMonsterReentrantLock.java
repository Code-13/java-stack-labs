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

package io.github.code13.javastack.ck.platinum;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 可重入锁.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 14:54
 */
class WildMonsterReentrantLock implements java.util.concurrent.locks.Lock {

  private boolean isLocked = false;

  /** 实现可重入锁的要点一： 记录获取锁的线程. */
  private Thread lockedBy = null;

  /** 实现可重入锁的要点二：记录锁的重入次数. */
  private int lockedCount = 0;

  @Override
  public void lock() {

    synchronized (this) {
      var currentThread = Thread.currentThread();
      while (isLocked && currentThread != lockedBy) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        isLocked = true;
        lockedBy = currentThread;
        lockedCount++;
      }
    }
  }

  @Override
  public void unlock() {
    synchronized (this) {
      if (Thread.currentThread() == lockedBy) {
        lockedCount--;
        if (lockedCount == 0) {
          isLocked = false;
          lockedBy = null;
          notifyAll();
        }
      }
    }
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {}

  @Override
  public boolean tryLock() {
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
