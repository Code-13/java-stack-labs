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

package io.github.code13.columns.ck.gold;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-黄金3：雨露均沾-不要让你的线程在竞争中被“饿死” .
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 09:25
 */
@DisplayName("并发王者课-黄金3：雨露均沾-不要让你的线程在竞争中被“饿死”")
class Gold3 {

  /**
   * 线程 饥饿（Starvation） 指的是在多线程的资源竞争中，存在贪婪的线程一直锁定资源不释放，其他的线程则始终处于等待状态，然而这个等待是没有结果的，它们会被活活地饿死。
   *
   * <p>饥饿一般由下面三种原因导致： （1）线程被无限阻塞 （2） 线程优先级降低没有获得CPU时间 （3） 线程永远在等待资源
   */
  @Test
  @DisplayName("Starvation")
  void test1() {

    /* 体验线程饥饿. */

    class WildMonster {
      public synchronized void killWildMonster() {
        while (true) {
          var name = Thread.currentThread().getName();
          System.out.println(name + ": 斩获野怪");

          try {
            Thread.sleep(500); // 使用 sleep 会造成线程饥饿
            // wait();                  // wait 则不会
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }

    var wildMonster = new WildMonster();

    String[] players = {"哪吒", "兰陵王", "铠", "典韦"};

    var i = 0;
    for (String player : players) {
      i += 2;
      var thread = new Thread(wildMonster::killWildMonster, player);
      thread.setPriority(i);
      thread.start();
    }

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("活锁")
  void test2() {

    /*
     * 所谓活锁（LiveLock），指的是两个线程都忙于响应对方的请求，但却不干自己的事。它们不断地重复特定的代码，却一事无成
     */

    /*
     * 在结果上，活锁和死锁都是灾难性的，都将会造成应用程序无法提供正常的服务能力.
     */
  }
}
