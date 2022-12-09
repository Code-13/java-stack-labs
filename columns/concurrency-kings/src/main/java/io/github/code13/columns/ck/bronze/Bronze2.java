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

package io.github.code13.columns.ck.bronze;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜2：本来面目-如何简单认识Java中的线程.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/2 09:04
 */
@DisplayName("并发王者课-青铜2：本来面目-如何简单认识Java中的线程")
class Bronze2 {

  /**
   *
   *
   * <pre>
   *   //  1. 构造函数
   *   在 JDK 11 中，Thread 共有 11 个构造函数，但经常使用的只有
   *      Thread()，这个构造器会默认生成一个Thread-+n的名字，n是由内部方法nextThreadNum()生成的一个整型数字；
   *      Thread(String name)，在构建线程时指定线程名，是一个很不错的实践；
   *      Thread(Runnable target)，传入Runnable的实例，这个我们在上一篇文章中已经展示过；
   *      Thread(Runnable target, String name)，在传入Runnable实例时指定线程名。
   * </pre>
   *
   * <p>2. 关于名字
   * 虽然Thread默认会生成一个线程名，但为了方便日志输出和问题排查，比较建议你在创建线程时自己手动设置名称，比如anQiLaPlayer的线程名可以设置为Thread-anQiLa。
   *
   * <p>3.关于线程ID
   * 和线程名一样，每个线程都有自己的ID，如果你没有指定的话，Thread会自动生成。确切地说，线程的ID是根据threadSeqNumber()对Thread的静态变量threadSeqNumber进行累加得到：{@link
   * Thread#nextThreadID()}
   *
   * <pre>
   *   4. 关于线程优先级
   * 在创建新的线程时，线程的优先级默认和当前父线程的优先级一致，当然我们也可以通过setPriority(int newPriority)方法来设置
   * Thread线程的优先级设置是不可靠的
   * 线程组的优先级高于线程优先级
   * </pre>
   */
  @Test
  @DisplayName("Thread类-Java中的线程基础")
  void test0() {}

  @Test
  @DisplayName("使用不同的构造方式，编写两个线程并打印出线程的关键信息；")
  void test() {
    Thread thread1 = new Thread(() -> System.out.println("线程"));
    System.out.println(thread1.getId());
    System.out.println(thread1.getName());
    System.out.println(thread1.getPriority());

    Thread thread2 = new Thread(() -> System.out.println("线程"), "线程1");
    System.out.println(thread2.getId());
    System.out.println(thread2.getName());
    System.out.println(thread2.getPriority());
  }
}
