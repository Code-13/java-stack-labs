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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 并发王者课-青铜3：兴利除弊-如何理解多线程的安全问题
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/2 09:41
 */
@DisplayName("并发王者课-青铜3：兴利除弊-如何理解多线程的安全问题")
class Bronze3 {

  @Test
  @DisplayName("理解并发（Concurrency）和并行（Parallelism）")
  void test1() {
    //从CPU计算的角度看， 并发和并行的概念可以理解为：

    //如果1个CPU同时执行5个任务，就是并发；
    //如果5个CPU同时执行5个任务，并且是每个CPU执行一个，那么就是并行。

  }

  @Test
  @DisplayName("理解竞态（Race Condition）下的安全问题")
  void test2() {
    //  竞态：多个线程试图在同一时刻修改共享数据的情况
    /*
     * 常见的竞态模式：
     * Read-modify-write 是在写入时因并发导致值被覆盖
     * Check-then-act 则是因并发导致条件判断失效
     */
  }

  @Test
  @DisplayName("如何预发竞态")
  void test3() {
    //  其核心在于锁+原子操作，即对临界区进行加锁，让临界区每次有且只能有一个线程访问，在当前线程未离开临界区时，其他线程不得进入，且线程在临界区的操作必须保证原子性。
  }

  @Test
  @DisplayName("写一段多线程并发代码，体验并发时的数据错误。")
  void test4() throws InterruptedException {
    class Master {
      private int blood = 100;

      public int decreaseBlood() {
        if (isAlive()) {
          blood = blood - 3;
          return blood;
        }
        return blood;
      }

      public boolean isAlive() {
        return blood > 0;
      }
    }

    Master master = new Master();

    ExecutorService executorService = Executors.newFixedThreadPool(100);

    for (int i = 0; i < 200; i++) {
      executorService.execute(master::decreaseBlood);
    }

    executorService.awaitTermination(5, TimeUnit.SECONDS);

    System.out.println(master.blood);
  }

}
