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

package io.github.code13.javastack.cm.gold;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-黄金1：两败俱伤-互不相让的线程如何导致了死锁僵局.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/22 22:39
 */
@DisplayName("并发王者课-黄金1：两败俱伤-互不相让的线程如何导致了死锁僵局 ")
class Gold1 {

  /*
  在并发编程中，死锁表示的是一种状态。在这种状态下，各方都在等待另一方释放所持有的资源，但是它们之间又缺乏必要的通信机制，导致彼此存在环路依赖而永远地等待下去。
   */

  /*
    死锁产生的必要条件:
  互斥：            一个资源每次只能被一个线程使用。比如，上图中的A和B同时只能被线程1和线程2其中一个使用；
  请求与保持条件：   一个线程在请求其他资源被阻塞时，对已经持有的资源保持不释放。比如，上图中的线程1在请求B时，并不会释放A；
  不剥夺条件：       对于线程已经获得的资源，在它主动释放前，不可以主动剥夺。比如，上图中线程1和线程2已经获得的资源，除非自己释放，否则不可以被强制剥夺；
  循环等待条件：     多个线程之间形成环状等待。上图中的线程1和线程2所形成的就是循环等待。
      */

  /*
    死锁的处理:
  1. 忽略死锁: 这种策略适用于死锁发生概率较低且影响可容忍的场景，如果死锁被证明永远不会发生也可以采用这种策略。
  2. 检测: 线程终止; 资源抢占
  3. 预防: 对待死锁问题，预防是关键。要预防死锁，只要打破其中任一条件即可
     */

  @Test
  @DisplayName("模拟死锁")
  void test1() throws InterruptedException {

    var lockA = new Object();
    var lockB = new Object();

    var t1 =
        new Thread(
            () -> {
              synchronized (lockA) {
                System.out.println("t1 持有 A");

                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }

                synchronized (lockB) {
                  System.out.println("t1 持有 B");
                }
              }
            });

    var t2 =
        new Thread(
            () -> {
              synchronized (lockB) {
                System.out.println("t2 持有 B");

                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }

                synchronized (lockA) {
                  System.out.println("t2 持有 A");
                }
              }
            });

    t1.start();
    t2.start();

    Thread.currentThread().join();
  }
}
