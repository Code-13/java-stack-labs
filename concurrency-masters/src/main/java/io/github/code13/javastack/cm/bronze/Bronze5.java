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

import static io.github.code13.javastack.cm.bronze.Bronze6.PrintObjectHeader.printObjectHeader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * 并发王者课-青铜5：一探究竟-如何从synchronized理解Java对象头中的锁.
 *
 * <p>粗略地介绍synchronized背后的一些基本原理，让你对Java中的锁有个粗略但直观的印象
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/12 09:11
 */
@DisplayName("并发王者课-青铜5：一探究竟-如何从synchronized理解Java对象头中的锁")
class Bronze5 {

  @SuppressWarnings("checkstyle:LineLength")
  @Test
  @DisplayName("一、从Mark Word认识锁")
  void test1() {
    /*
    我们知道，在HotSpot虚拟机中，一个对象的存储分布由3个部分组成：

    对象头（Header）：由Mark Word和Klass Pointer组成；
    实例数据（Instance Data）：对象的成员变量及数据；
    对齐填充（Padding）：对齐填充的字节，暂时不必理会。

    在这3个部分中，对象头中的Mark Word是本文的重点，也是理解Java锁的关键。Mark Word记录的是对象运行时的数据，其中包括：

    哈希码（identity_hashcode）
    GC分代年龄（age）
    锁状态标志
    线程持有的锁
    偏向线程ID（thread）

    所以，从对象头中的Mark Word看，Java中的锁就是对象头中的一种数据。在JVM中，每个对象都有这样的锁，并且用于多线程访问对象时的并发控制。
    如果一个线程想访问某个对象的实例，那么这个线程必须拥有该对象的锁。首先，它需要通过对象头中的Mark Word判断该对象的实例是否已经被线程锁定。如果没有锁定，那么线程会在Mark Word中写入一些标记数据，就是告诉别人：这个对象是我的啦！如果其他线程想访问这个实例的话，就需要进入等待队列，直到当前的线程释放对象的锁，也就是把Mark Word中的数据擦除。
    当一个线程拥有了锁之后，它便可以多次进入。当然，在这个线程释放锁的时候，那么也需要执行相同次数的释放动作。比如，一个线程先后3次获得了锁，那么它也需要释放3次，其他线程才可以继续访问.
    */
  }

  @Test
  @DisplayName("二、通过JOL体验Mark Word的变化")
  void test2() {

    /*
     * 为了直观感受对象头中Mark Word的变化，我们可以通过 JOL（Java Object Layout） 工具演示一遍。JOL是一个不错的Java内存布局查看工具，希望你能记住它。
     * dependencies {
     *     implementation 'org.openjdk.jol:jol-core:0.10'
     * }
     *
     * 执行测试，查看结果
     */

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

    Master master = new Master();
    System.out.println("====加锁前====");
    System.out.println(ClassLayout.parseInstance(master).toPrintable());
    System.out.println("====加锁后====");
    synchronized (master) {
      System.out.println(ClassLayout.parseInstance(master).toPrintable());
    }

    /* 借助工具类格式化输出. */
    Master master1 = new Master();
    System.out.println("====加锁前====");
    printObjectHeader(master1);
    System.out.println("====加锁后====");
    synchronized (master1) {
      printObjectHeader(master1);
    }
  }
}
