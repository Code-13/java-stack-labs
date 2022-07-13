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

import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金7：整齐划一-CountDownLatch如何协调多线程的开始和结束.
 *
 * <p>{@link java.util.concurrent.CountDownLatch}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/26 22:39
 */
@DisplayName("王者并发课-铂金7：整齐划一-CountDownLatch如何协调多线程的开始和结束")
class Platinum7 {

  /*
   * CountDownLatch是JUC中的一款常用工具类，当你在编写多线程代码时，如果你需要协调多个线程的开始和结束动作时，它可能会是你的不错选择。
   */

  /*
   * CountDownLatch适用的两个典型应用场景
   *
   * 场景1. 协调子线程结束动作：等待所有子线程运行结束
   * 场景2. 协调子线程开始动作：统一各线程动作开始的时机
   */

  /*
   * Java 中的 CountDownLatch 设计
   *
   * await()：等待latch降为0；
   * boolean await(long timeout, TimeUnit unit)：等待latch降为0，但是可以设置超时时间。比如有玩家超时未确认，那就重新匹配，总不能为了某个玩家等到天荒地老吧。
   * countDown()：latch数量减1；
   * getCount()：获取当前的latch数量。
   */

  @Test
  @DisplayName("场景1. CountDownLatch实现对各子线程的等待")
  void test1() throws InterruptedException {

    var countDownLatch = new CountDownLatch(5);

    var t1 = new Thread(countDownLatch::countDown);
    var t2 = new Thread(countDownLatch::countDown);
    var t3 = new Thread(countDownLatch::countDown);
    var t4 = new Thread(countDownLatch::countDown);
    var t5 =
        new Thread(
            () -> {
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              countDownLatch.countDown();
            });

    t1.start();
    t2.start();
    t3.start();
    t4.start();
    t5.start();

    countDownLatch.await();

    System.out.println("所有的线程已经准备就绪");
  }

  @Test
  @DisplayName("场景2. CountDownLatch实现对多线程的统一管理")
  void test2() throws InterruptedException {

    var countDownLatch = new CountDownLatch(1);

    var t1 = new Thread(() -> waitForCountDown(countDownLatch), "t1");
    var t2 = new Thread(() -> waitForCountDown(countDownLatch), "t2");
    var t3 = new Thread(() -> waitForCountDown(countDownLatch), "t3");
    var t4 = new Thread(() -> waitForCountDown(countDownLatch), "t4");
    var t5 = new Thread(() -> waitForCountDown(countDownLatch), "t5");

    t1.start();
    t2.start();
    t3.start();
    t4.start();
    t5.start();

    Thread.sleep(1000);

    countDownLatch.countDown();

    System.out.println("所有的线程等待就绪");
  }

  private static void waitForCountDown(CountDownLatch countDownLatch) {
    try {
      System.out.println(Thread.currentThread().getName() + ": 开始等待主线程");
      countDownLatch.await();
      System.out.println(Thread.currentThread().getName() + ": 开始执行");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
