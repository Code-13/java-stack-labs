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

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * OneShotLatch.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/21 20:26
 */
public class OneShotLatch {

  private final Sync sync = new Sync();

  public void signal() {
    sync.releaseShared(0);
  }

  public void await() throws InterruptedException {
    sync.acquireSharedInterruptibly(0);
  }

  static class Sync extends AbstractQueuedSynchronizer {

    @Override
    protected int tryAcquireShared(int ignored) {
      // 如果闭锁是开的（state==1），那么这个操作将成功，否则将失败
      return (getState() == 1) ? 1 : -1;
    }

    @Override
    protected boolean tryReleaseShared(int ignored) {
      // 现在打开闭锁
      setState(1);
      // 现在其他的线程可以获取该闭锁
      return true;
    }
  }
}
