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

package io.github.code13.javastack.cm.bronze;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜4：宝刀屠龙-如何使用synchronized之初体验.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/2 10:21
 */
@DisplayName("并发王者课-青铜4：宝刀屠龙-如何使用synchronized之初体验")
class Bronze4 {

  @Test
  @DisplayName("认识synchronized")
  void test1() {
    //  在Java中，每个对象都会有一把锁。当多个线程都需要访问对象时，那么就需要通过获得锁来获得许可，只有获得锁的线程才能访问对象，并且其他线程将进入等待状态，等待其他线程释放锁。

    /*
     *根据Sun官文文档的描述，synchronized关键字提供了一种预防线程干扰和内存一致性错误的简单策略，即如果一个对象对多个线程可见，那么该对象变量（final修饰的除外）的读写都需要通过synchronized来完成。
     * 你可能已经注意到其中的两个关键名词：
     *
     * 线程干扰（Thread Interference）：不同线程中运行但作用于相同数据的两个操作交错时，就会发生干扰。这意味着这两个操作由多个步骤组成，并且步骤顺序重叠；
     * 内存一致性错误（Memory Consistency Errors）：当不同的线程对应为相同数据的视图不一致时，将发生内存一致性错误。内存一致性错误的原因很复杂，幸运的是，我们不需要详细了解这些原因，所需要的只是避免它们的策略。
     *
     * 从竞态的角度讲，线程干扰对应的是Read-modify-write，而内存一致性错误对应的则是Check-then-act。
     * 结合锁和synchronized的概念可以理解为，锁是多线程安全的基础机制，而synchronized是锁机制的一种实现.
     */
  }

  @Test
  @DisplayName("synchronized的四种用法")
  void test2() {
    class Master {
      // 主宰的初始血量
      private int blood = 100;

      // 每次被击打后血量减5
      public synchronized int decreaseBlood() {
        blood = blood - 5;
        return blood;
      }

      // 通过血量判断主宰是否还存活
      public boolean isAlive() {
        return blood > 0;
      }
    }
  }

  /**
   * Java中的synchronized关键字用于解决多线程访问共享资源时的同步，以解决线程干扰和内存一致性问题； 你可以通过 代码块（code block） 或者 方法（method）
   * 来使用synchronized关键字； synchronized的原理基于对象中的锁，当线程需要进入synchronized修饰的方法或代码块时，它需要先获得锁并在执行结束后释放它；
   * 当线程进入非静态（non-static）同步方法时，它获得的是对象实例（Object level）的锁。而线程进入静态同步方法时，它所获得的是类实例（Class
   * level）的锁，两者没有必然关系； 如果synchronized中使用的对象是null，将会抛出NullPointerException错误；
   * synchronized对方法的性能有一定影响，因为线程要等待获取锁； 使用synchronized时尽量使用代码块，而不是整个方法，以免阻塞整个方法；
   * 尽量不要使用String类型和原始类型作为参数。这是因为，JVM在处理字符串、原始类型时会对它们进行优化。比如，你原本是想对不同的字符串进行加锁，然而JVM认为它们是同一个，很显然这不是你想要的结果.
   */
  @Test
  @DisplayName("synchronized小结")
  void test3() {}
}
