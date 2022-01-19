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

import java.util.ArrayList;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜1：牛刀小试-如何创建线程之初体验.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 17:10
 */
@SuppressWarnings("DuplicatedCode")
@DisplayName("并发王者课-青铜1：牛刀小试-如何创建线程之初体验")
class Bronze1 {

  @Test
  @DisplayName("Demo")
  void test() {

    /*
    在本局游戏中，将有3位玩家出场，他们分别是哪吒、苏烈和安其拉。
    根据玩家不同的角色定位，在王者峡谷中，他们会有不同的游戏路线：
      作为战士的哪吒将走上路的对抗路线；
      法师安其拉则去镇守中路；
      战坦苏烈则决定去下路。
     */

    Thread zeZhaPlayer = new Thread(() -> System.out.println("我是哪吒，我去上路"));
    Thread anQiLaPlayer = new Thread(() -> System.out.println("我是安其拉，我去中路"));
    Thread suLiePlayer = new Thread(() -> System.out.println("我是苏烈，我去下路"));

    zeZhaPlayer.start();
    anQiLaPlayer.start();
    suLiePlayer.start();
  }

  /** 用两种不同的方式，创建出两个线程，交差打印1~100之间的奇数和偶数，并断点调试。 */
  @Test
  @DisplayName("trial")
  void test2() {

    ArrayList<Integer> evenNumbers =
        IntStream.rangeClosed(1, 100)
            .filter(n -> n % 2 == 0)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    ArrayList<Integer> oddNumbers =
        IntStream.rangeClosed(1, 100)
            .filter(n -> n % 2 != 0)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

    System.out.println(evenNumbers);
    System.out.println(oddNumbers);

    Object monitor = new Object();

    Thread printOdd =
        new Thread(
            () -> {
              for (Integer oddNumber : oddNumbers) {
                synchronized (monitor) {
                  System.out.println(oddNumber);
                  monitor.notifyAll();
                  try {
                    monitor.wait();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              }
            });

    Thread printEven =
        new Thread(
            () -> {
              for (Integer evenNumber : evenNumbers) {
                synchronized (monitor) {
                  System.out.println(evenNumber);
                  monitor.notifyAll();
                  try {
                    monitor.wait();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }
              }
            });

    printOdd.start();
    printEven.start();
  }
}
