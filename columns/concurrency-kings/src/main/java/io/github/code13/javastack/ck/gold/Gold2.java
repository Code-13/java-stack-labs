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

package io.github.code13.javastack.ck.gold;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-黄金2：行稳致远-如何让你的线程免于死锁 .
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 08:56
 */
@DisplayName("并发王者课-黄金2：行稳致远-如何让你的线程免于死锁")
class Gold2 {

  /*
  简单来说，预防死锁主要有三种策略：
    顺序化加锁；
    给锁一个超时期限；
    检测死锁。
   */

  @Test
  @DisplayName("顺序化加锁")
  void test1() throws InterruptedException {
    /*
     * 如果能按照一定的顺序分别满足各个线程的请求，那么死锁也就不再存在，也就是所谓的顺序化加锁（Lock Ordering）。
     */

    /*
     * 调整加锁顺序是一种简单但有效的死锁预防策略。但是，这一策略并不是万能的，它仅适用于你在编码时已经知晓加锁的顺序。
     */

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
              synchronized (lockA) {
                System.out.println("t2 持有 B");

                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }

                synchronized (lockB) {
                  System.out.println("t2 持有 A");
                }
              }
            });

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }

  @Test
  @DisplayName("给锁一个超时时间")
  void test2() {

    /*
     * 死锁的产生有一些必要的条件，其中一个是无限等待。设定锁超时时间正是为了打破这一条件，让无限等待变成有限等待。
     *
     * <p>synchronized代码块不可以指定锁超时；如果需要锁超时，你需要使用自定义锁，或者使用JDK提供的并发工具类
     *
     * <p>所谓给锁加一个超时的期限，其实有两层含义。一是在请求锁时需要设定超时时间，二是在获取锁之后对锁的持有也要有个超时时间，总不能到手就不放，那是耍流氓。
     */

  }

  @Test
  @DisplayName("死锁检测")
  void test3() {
    /*
     * 在你感觉线程可能被阻塞或死锁时，可以通过jstack命令查看。如果存在死锁，输出的结果中会有明确的死锁提示
     */

    /* 除了jstack之外，JProfiler、jConsole、jVisualvm 都可以 */

    /*
     * jshdb jstack --pid xxx
     */

  }
}
