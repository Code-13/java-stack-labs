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

package io.github.code13.javase.jnaf.jdk13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SwitchRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 22:16
 */
@DisplayName("jdk 13 switch 新特性")
public class SwitchRunner {

  @Test
  @DisplayName("switch")
  void testSwitch() {

    String x = "3";
    int i;

    switch (x) {
      case "1":
        i = 1;
        break;
      case "2":
        i = 2;
        break;
      default:
        i = x.length();
        break;
    }

    System.out.println(i);
  }

  @Test
  @DisplayName("switch yield1")
  void testSwitchYield1() {
    String x = "3";
    int i =
        switch (x) {
          case "1" -> 5;
          case "2" -> 6;
          default -> {
            yield 7;
          }
        };
    System.out.println(i);
  }

  @Test
  @DisplayName("switch yield1")
  void testSwitchYield2() {
    String x = "3";
    int i =
        switch (x) {
          case "1":
            yield 5;
          case "2":
            yield 6;
          default:
            yield 7;
        };
    System.out.println(i);
  }
}
