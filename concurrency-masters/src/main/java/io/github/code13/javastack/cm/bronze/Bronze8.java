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

import java.lang.Thread.State;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜8：分工协作-从本质认知线程的状态和动作方法.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/19 09:16
 */
@DisplayName("并发王者课-青铜8：分工协作-从本质认知线程的状态和动作方法")
class Bronze8 {

  @Test
  @DisplayName("线程的状态")
  void test1() {
    Arrays.stream(State.values()).forEach(state -> System.out.println(state.name()));

    //    NEW
    // RUNNABLE
    // BLOCKED
    // WAITING
    // TIMED_WAITING
    // TERMINATED

  }

  @Test
  @DisplayName("Start")
  void testStart() {
    Runnable neZhaRunnable = () -> System.out.println("我是哪吒，我去上路");

    Thread zeZhaPlayer = new Thread(neZhaRunnable);
    zeZhaPlayer.start();
  }

  @Test
  @DisplayName("wait 和 notify")
  void testWaitAndNotify() {

    Player player = new Player();

    var fightThread = new Thread(player::fight);
    var refreshSkillThread = new Thread(player::refreshSkill);

    fightThread.start();
    refreshSkillThread.start();
  }

  @Test
  @DisplayName("interrupt")
  void testInterrupt() {
    var player = new Player();
    var goHomeT = new Thread(player::backHome);
    goHomeT.start();
    goHomeT.interrupt();
  }

  @Test
  @DisplayName("Join")
  void testJoin() throws InterruptedException {
    Player player = new Player();

    var fightThread = new Thread(player::fight);
    var refreshSkillThread = new Thread(player::refreshSkill);

    refreshSkillThread.start();
    refreshSkillThread.join();
    fightThread.start();
  }

  @Test
  @DisplayName("yield")
  void testYield() {
    Player player = new Player();

    var fightThread = new Thread(player::fight);

    fightThread.start();
    Thread.yield();
  }

  @Test
  @DisplayName("Sleep")
  void testSleep() throws InterruptedException {
    Player player = new Player();

    var fightThread = new Thread(player::fight);

    fightThread.start();
    Thread.sleep(1000);
  }

  static class Player {
    void fight() {
      System.out.println("技能未就绪，冷却中");
      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          System.out.println("技能冷却被打断");
        }
      }
    }

    void refreshSkill() {
      System.out.println("技能冷却中...");
      synchronized (this) {
        notifyAll();
        System.out.println("技能以刷新");
      }
    }

    void backHome() {
      System.out.println("回程中");
      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          System.out.println("回城被打断");
        }
      }
    }
  }
}
