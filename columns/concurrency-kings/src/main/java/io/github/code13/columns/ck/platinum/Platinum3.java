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

package io.github.code13.columns.ck.platinum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-铂金3：一劳永逸-如何理解锁的多次可重入问题.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 17:49
 */
@DisplayName("并发王者课-铂金3：一劳永逸-如何理解锁的多次可重入问题")
class Platinum3 {

  /* 所谓锁的可重入，指的是锁可以被线程 重复 或 递归 调用，也可以理解为对同一把锁的重复获取。 */

  /*
   * 如何避免锁的可重入问题
   *
   * 尽量避免编写需要重入获取锁的代码；
   * 如果需要，使用可重入锁。
   */

  /*
   * 此外，需要注意的是，锁的可重入对锁的性能有一定的影响，而且实现起来更为复杂。所以，我们不能说锁的可重入与不可重入哪个好，这要取决于具体的问题。
   */

  @Test
  @DisplayName("锁的可重入造成的问题")
  void test1() {

    class ReentrantWildArea {
      // 野区锁定
      private boolean isAreaLocked = false;

      // 进入野区A
      public synchronized void enterAreaA() throws InterruptedException {
        isAreaLocked = true;
        System.out.println("已经进入野区A...");
        enterAreaB();
      }
      // 进入野区B
      public synchronized void enterAreaB() throws InterruptedException {
        while (isAreaLocked) {
          System.out.println("野区B方法进入等待中...");
          wait();
        }
        System.out.println("已经进入野区B...");
      }

      public synchronized void unlock() {
        isAreaLocked = false;
        notify();
      }
    }

    // 打野英雄铠进入野区
    Thread kaiThread =
        new Thread(
            () -> {
              ReentrantWildArea wildArea = new ReentrantWildArea();
              try {
                wildArea.enterAreaA();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    kaiThread.start();

    try {
      kaiThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
