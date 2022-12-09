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

package io.github.code13.columns.ck.lignt05;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Light01_JMM.
 *
 * <p>王者并发课-星耀01：群雄逐鹿-从鹿死谁手深入理解Java内存模型
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/9 13:18
 */
@DisplayName("王者并发课-星耀01：群雄逐鹿-从鹿死谁手深入理解Java内存模型")
class Light01_JMM {

  static class DeerGame {

    /** 待宰的小鹿 */
    private final Deer deer = new Deer();

    /** 物理攻击，一次攻击掉血10个单位 */
    public boolean physicalAttack() {
      return deer.reduceBlood(10) == 0;
    }

    /** 魔法攻击，一次攻击掉血5个单位 */
    public boolean magicAttack() {
      return deer.reduceBlood(5) == 0;
    }

    @Data
    private static class Deer {
      private int blood = 100;

      public int reduceBlood(int bloodToReduce) {
        int remainBlood = blood - bloodToReduce;
        blood = Math.max(remainBlood, 0);
        return blood;
      }
    }
  }

  /** 两个线程都在读写Deer中的blood字段，但这个字段却没有任何的并发处理，结果就是程序故障，两人可能都获胜。 */
  @Test
  @DisplayName("test_attack_Deer_by_multiHero")
  void test_attack_Deer_by_multiHero() {
    DeerGame deerGame = new DeerGame();

    Thread hero1 =
        new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                if (deerGame.physicalAttack()) {
                  System.out.println("兰陵王胜出");
                }
              }
            });

    Thread hero2 =
        new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                if (deerGame.physicalAttack()) {
                  System.out.println("恺胜出");
                }
              }
            });

    hero1.start();
    hero2.start();

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static class DeerGameWithVolatile {

    /** 待宰的小鹿 */
    private final DeerWithVolatile deer = new DeerWithVolatile();

    /** 物理攻击，一次攻击掉血10个单位 */
    public boolean physicalAttack() {
      return deer.reduceBlood(10) == 0;
    }

    /** 魔法攻击，一次攻击掉血5个单位 */
    public boolean magicAttack() {
      return deer.reduceBlood(5) == 0;
    }

    @Data
    private static class DeerWithVolatile {
      private volatile int blood = 100;

      public int reduceBlood(int bloodToReduce) {
        int remainBlood = blood - bloodToReduce;
        blood = Math.max(remainBlood, 0);
        return blood;
      }
    }
  }

  /** 两个线程都在读写Deer中的blood字段，但这个字段却没有任何的并发处理，结果就是程序故障，两人可能都获胜。 */
  @Test
  @DisplayName("test_attack_Deer_by_multiHero")
  void test_attack_Deer_by_multiHero_with_volatile() {
    DeerGameWithVolatile deerGame = new DeerGameWithVolatile();

    Thread hero1 =
        new Thread(
            () -> {
              for (int i = 0; i < 10; i++) {
                if (deerGame.physicalAttack()) {
                  System.out.println("兰陵王胜出");
                  break;
                }
              }
            });

    Thread hero2 =
        new Thread(
            () -> {
              while (true) {
                if (deerGame.deer.blood == 0) {
                  System.out.println("beer 血量已经见底");
                  break;
                }
              }
            });

    hero1.start();
    hero2.start();

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
