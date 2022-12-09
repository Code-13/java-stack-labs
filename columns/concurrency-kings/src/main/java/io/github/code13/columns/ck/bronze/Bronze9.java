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
 * 并发王者课-青铜9：防患未然-如何处理线程中的异常.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/19 10:55
 */
class Bronze9 {

  /* 在Java中，每个线程所运行的都是独立运行的代码片段，如果我们没有主动提供线程间通信和协作的机制，那么它们彼此之间是隔离的。 */

  /*
  当线程出错时，首先会检查当前线程是否指定了错误处理器；
    如果当前线程没有指定错误处理器，则继续检查其所在的线程组是否指定（注意，前面我们已经说过，每个线程都是有线程组的）；
    如果当前线程的线程组也没有指定，则继续检查其父线程是否指定；
    如果父线程同样没有指定错误处理器，则最后检查默认处理是否设置；
    如果默认处理器也没有设置，那么将不得不输出错误的堆栈信息。
   */

  /*
  UncaughtExceptionHandler: 在Java中，UncaughtExceptionHandler用于处理线程突然异常终止的情况
   */

  @Test
  @DisplayName("UncaughtExceptionHandler")
  void test1() {

    var thread =
        new Thread(
            () -> {
              System.out.println(Thread.currentThread().getName());
              throw new RuntimeException("抛出的异常");
            });

    thread.setName("测试");
    thread.setUncaughtExceptionHandler(
        (t, e) -> System.out.println("出错了！线程名：" + t.getName() + "，错误信息：" + e.getMessage()));

    thread.start();
  }
}
