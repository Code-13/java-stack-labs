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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金4：令行禁止-为何说信号量是线程间的同步利器.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/26 09:12
 */
@DisplayName("王者并发课-铂金4：令行禁止-为何说信号量是线程间的同步利器")
class Platinum4 {

  /*
   * 信号量是线程间的同步结构，主要用于多线程协作时的信号传递，以及对共享资源的保护，防止竞态的发生
   */

  /*
   * 信号量的关键信息：
   *    共享的资源：是线程协作的基础
   *    多个线程访问相同的共享资源，并根据资源的状态采取行动
   */

  /*
   * 多线程共享一份资源列表，但是资源是有限的。
   * 线程必须按照一定的顺序有序的访问资源，并在访问结束后释放资源。没有获得资源的线程，只能等待其他线程释放资源后再次重试
   */

  @Test
  @DisplayName("简单的信号量实现")
  void test1() {

    class ForHelpSemaphore {
      private boolean signal = false;

      public synchronized void sendSignal() {
        signal = true;
        notify();
        System.out.println("信号已经发送");
      }

      public synchronized void receiveSignal() throws InterruptedException {
        while (!signal) {
          wait();
        }
        signal = false;
        System.out.println("信号已收到");
      }
    }

    var semaphore = new ForHelpSemaphore();

    var t1 = new Thread(semaphore::sendSignal);
    var t2 =
        new Thread(
            () -> {
              try {
                semaphore.receiveSignal();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    t1.start();
    t2.start();
  }

  @Test
  @DisplayName("不同类型测信号量")
  void test2() {

    /** 计数型信号量. */
    class CountingSemaphore {
      private int signals = 0;

      public synchronized void take() {
        signals++;
        notify();
      }

      public synchronized void release() throws InterruptedException {
        while (signals == 0) {
          wait();
        }
        signals--;
      }
    }

    /** 边界型信号量. */
    class BoundedSemaphore {
      private int signal = 0;
      private int bound = 0;

      public BoundedSemaphore(int upperBound) {
        bound = upperBound;
      }

      public synchronized void take() throws InterruptedException {
        while (signal == bound) {
          wait();
        }
        signal++;
        notify();
      }

      public synchronized void release() throws InterruptedException {
        while (signal == 0) {
          wait();
        }
        signal--;
      }
    }
  }
}
