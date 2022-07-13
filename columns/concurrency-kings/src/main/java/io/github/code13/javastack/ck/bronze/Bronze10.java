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

package io.github.code13.javastack.ck.bronze;

import java.util.LinkedList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜10：千锤百炼-如何解决生产者与消费者经典问题.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/19 14:59
 */
@DisplayName("并发王者课-青铜10：千锤百炼-如何解决生产者与消费者经典问题")
class Bronze10 {

  /*
   * 生产者与消费者问题的要点：
   * 生产者与消费者解耦，两者通过缓冲区传递数据；
   * 缓冲区数据装满了之后，生产者停止数据生产或丢弃数据；
   * 缓冲区数据为空后，消费者停止消费并进入等待状态，等待生产者通知。
   */

  @Test
  @DisplayName("生产者与消费者")
  void test1() throws InterruptedException {
    var wildMonsterArea = new LinkedList<String>();

    var lanLinWangT =
        new Thread(
            () -> {
              while (true) {
                synchronized (wildMonsterArea) {
                  if (wildMonsterArea.size() == 0) {
                    try {
                      wildMonsterArea.wait();
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    }
                  }

                  var last = wildMonsterArea.getLast();
                  wildMonsterArea.remove(last);
                  System.out.println("打野收获了一个野怪：" + last);
                }
              }
            });

    var wildMonsterProducer =
        new Thread(
            () -> {
              for (int i = 0; ; i++) {
                synchronized (wildMonsterArea) {
                  if (wildMonsterArea.size() == 0) {
                    wildMonsterArea.add("野怪：" + i);
                    System.out.println(wildMonsterArea.getLast());
                    wildMonsterArea.notify();
                  }
                }
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  System.out.println("野怪投放被打断");
                }
              }
            });

    wildMonsterProducer.start();
    lanLinWangT.start();

    //    Thread.currentThread().join();
    wildMonsterProducer.join();
  }
}
