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

package io.github.code13.columns.ck.platinum;

import java.util.concurrent.Exchanger;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金9：互通有无-Exchanger如何完成线程间的数据交换.
 *
 * <p>{@link java.util.concurrent.Exchanger}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/27 09:59
 */
@DisplayName("王者并发课-铂金9：互通有无-Exchanger如何完成线程间的数据交换")
class Platinum9 {

  @Test
  @DisplayName("王者并发课-铂金9：互通有无-Exchanger如何完成线程间的数据交换")
  void test1() throws InterruptedException {

    var exchanger = new Exchanger<WildMonster>();

    var t1 =
        new Thread(
            () -> {
              try {
                var bear = new Bear("棕熊");
                say("我有" + bear.getName());

                var wildMonster = exchanger.exchange(bear);
                say("交易完成，我获得了：", bear.getName(), "->", wildMonster.getName());
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            },
            "铠");

    var t2 =
        new Thread(
            () -> {
              try {
                var wolf = new Wolf("野狼");
                say("我有" + wolf.getName());

                var wildMonster = exchanger.exchange(wolf);
                say("交易完成，我获得了：", wolf.getName(), "->", wildMonster.getName());
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            },
            "兰陵王");

    t1.start();
    t2.start();

    Thread.currentThread().join();
  }

  @Data
  private static class WildMonster {
    protected String name;
  }

  private static class Wolf extends WildMonster {
    public Wolf(String name) {
      this.name = name;
    }
  }

  private static class Bear extends WildMonster {
    public Bear(String name) {
      this.name = name;
    }
  }

  private static class Pig extends WildMonster {
    public Pig(String name) {
      this.name = name;
    }
  }

  private void say(String... s) {
    System.out.println(Thread.currentThread().getName() + ":" + String.join("", s));
  }

  private void print(String... s) {
    System.out.println(String.join("", s));
  }
}
