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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金8：峡谷幽会-看CyclicBarrier如何跨越重峦叠嶂.
 *
 * <p>{@link java.util.concurrent.CyclicBarrier}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/26 23:05
 */
@DisplayName("王者并发课-铂金8：峡谷幽会-看CyclicBarrier如何跨越重峦叠嶂")
class Platinum8 {

  @Test
  @DisplayName("多个线程相互等待，到齐后再执行特定动作。")
  void test1() throws InterruptedException {

    String appointmentPlace = "峡谷森林";

    var cyclicBarrier =
        new CyclicBarrier(2, () -> print("🌹🌹🌹到达约会地点：大乔和铠都来到了👉" + appointmentPlace));

    var daQiao =
        new Thread(
            () -> {
              try {
                say("恺，你在哪里");
                cyclicBarrier.await();
                say("恺, 你终于来了");
              } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
              }
            },
            "大乔");

    var kai =
        new Thread(
            () -> {
              try {
                say("我先打个野");
                Thread.sleep(1000);
                cyclicBarrier.await();
                say("乔，不好意思，刚打野遇上兰陵王了，你还好吗？！");
              } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
              }
            },
            "恺");

    daQiao.start();
    kai.start();

    Thread.currentThread().join();
  }

  @Test
  @DisplayName("可重用")
  void test2() throws InterruptedException {

    AtomicReference<String> appointmentPlace = new AtomicReference<>("峡谷森林");

    var cyclicBarrier =
        new CyclicBarrier(2, () -> print("🌹🌹🌹到达约会地点：大乔和铠都来到了👉" + appointmentPlace));

    var daQiao =
        new Thread(
            () -> {
              try {
                say("恺，你在哪里");
                cyclicBarrier.await();
                say("恺, 你终于来了");

                Thread.sleep(2600); // 约会中...

                say("那你小心，我先去了");

                cyclicBarrier.await();

                say("我愿意");
              } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
              }
            },
            "大乔");

    var kai =
        new Thread(
            () -> {
              try {
                say("我先打个野");
                Thread.sleep(1000);
                cyclicBarrier.await();
                say("乔，不好意思，刚打野遇上兰陵王了，你还好吗？！");

                Thread.sleep(2600); // 约会中...

                say("该死的兰陵王，乔，你先走，小河边见");

                // 修改新的约会地点
                appointmentPlace.set("小河边");

                Thread.sleep(1000); // 与兰陵王对决

                System.out.println("对决开始，恺干掉了兰陵王，前往" + appointmentPlace.get());

                cyclicBarrier.await();

                say("乔，我已经解决了兰陵王，你看今晚夜色多美，我陪你看星星到天明...");
              } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
              }
            },
            "恺");

    daQiao.start();
    kai.start();

    Thread.currentThread().join();
  }

  private void say(String s) {
    System.out.println(Thread.currentThread().getName() + ":" + s);
  }

  private void print(String s) {
    System.out.println(s);
  }
}
