/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.ck.platinum;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Lock 的不可重入锁简单实现.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 14:51
 */
class WildMonsterLock implements java.util.concurrent.locks.Lock {

  private boolean isLocked = false;

  @Override
  public void lock() {
    synchronized (this) {
      while (isLocked) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      isLocked = true;
    }
  }

  @Override
  public void unlock() {
    synchronized (this) {
      isLocked = false;
      notifyAll();
    }
  }

  // 我们先只实现 lock 以及 unLock，其余的 Api 先忽略

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
