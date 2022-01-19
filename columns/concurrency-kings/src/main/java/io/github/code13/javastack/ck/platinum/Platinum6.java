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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金6：青出于蓝-Condition如何把等待与通知玩出新花样.
 *
 * <p>{@link java.util.concurrent.locks.Condition}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/26 21:33
 */
@DisplayName("王者并发课-铂金6：青出于蓝-Condition如何把等待与通知玩出新花样 ")
class Platinum6 {

  /*
   * 从功能定位上说，作为Lock的配套工具，Condition是wait、notify和notifyAll增强版本，wait和notify有的能力它都有，wait和notify没有的能力它也有。
   */

  /*
   * JUC中的Condition是以接口的形式出现，并定义了一些核心方法：
   *
   * await()：让当前线程进入等待，直到收到信号或者被中断；
   * await(long time, TimeUnit unit)：让当前线程进入等待，直到收到信号或者被中断，或者到达指定的等待超时时间；
   * awaitNanos(long nanosTimeout)：让当前线程进入等待，直到收到信号或者被中断，或者到达指定的等待超时时间，只是在时间单位上和上一个方法有所区别；
   * awaitUninterruptibly(）：让当前线程进入等待，直到收到信号。注意，这个方法对中断是不敏感的；
   * awaitUntil(Date deadline)：让当前线程进入等待，直到收到信号或者被中断，或者到达截止时间；
   * signal()：随机唤醒一个线程；
   * signalAll()：唤醒所有等待的线程。
   *
   * 相较于原生的通知与等待，它的能力明显增强了很多，比如awaitUninterruptibly()和awaitUntil()。另外，Condition竟然是可以唤醒指定线程的，这就很有意思.
   */

  @Test
  @DisplayName("使用Condition唤醒指定线程")
  void test1() {

    Lock lock = new ReentrantLock();

    var condition = lock.newCondition();

    new Thread(
            () -> {
              lock.lock();
              try {
                System.out.println(Thread.currentThread().getName() + "开始等待");
                condition.await(5, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + "等待完成");
              } catch (InterruptedException e) {
                e.printStackTrace();
              } finally {
                lock.unlock();
              }
            },
            "线程1")
        .start();

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
