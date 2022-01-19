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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-铂金2：豁然开朗-“晦涩难懂”的ReadWriteLock竟如此妙不可言.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 15:35
 */
@DisplayName("并发王者课-铂金2：豁然开朗-“晦涩难懂”的ReadWriteLock竟如此妙不可言 ")
class Platinum2 {

  @Test
  @DisplayName("ReadWriteLock存在的价值")
  void test1() {
    /*
     * 数据允许多个线程同时读取，但只允许一个线程进行写入；
     * 在读取数据的时候，不可以存在写操作或者写请求；
     * 在写数据的时候，不可以存在读请求。
     */

  }

  @Test
  @DisplayName("自主实现ReadWriteLock")
  void test2() {

    class ReadWriteLock {

      private int writers = 0;

      /** 保证写请求优先处理. */
      private int writeRequests = 0;

      private int readers = 0;

      public synchronized void lockRead() throws InterruptedException {
        while (writers > 0 || writeRequests > 0) {
          wait();
        }
        readers++;
      }

      public synchronized void unlockRead() {
        readers--;
        notifyAll();
      }

      public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || writers > 0) {
          wait();
        }
        writeRequests--;
        writers++;
      }

      public synchronized void unlockWrite() {
        writers--;
        notifyAll();
      }
    }
  }
}
