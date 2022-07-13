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

package io.github.code13.javastack.javalabs.time;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Month
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 12:02
 * @see java.time.Month
 */
@DisplayName("java.time.Month")
class MonthTest {

  @Test
  @DisplayName("Month 枚举包含 一月（1）至十二月（12），使用定义的常量（Month.SEPTEMBER）使您的代码更具可读性")
  void test1() {
    Arrays.stream(Month.values()).forEachOrdered(System.out::println);

    // JANUARY
    // FEBRUARY
    // MARCH
    // APRIL
    // MAY
    // JUNE
    // JULY
    // AUGUST
    // SEPTEMBER
    // OCTOBER
    // NOVEMBER
    // DECEMBER
  }

  @Test
  @DisplayName("打印二月份最大可能的天数")
  void test2() {
    System.out.printf("%d%n", Month.FEBRUARY.maxLength());

    // 29
  }

  @Test
  @DisplayName("使用 getDisplayName(TextStyle, Locale) 方法， 相当于使用指定的语言环境进行翻译")
  void test3() {
    Month month = Month.AUGUST;
    Locale locale = Locale.getDefault();
    System.out.println(month.getDisplayName(TextStyle.FULL, locale));
    System.out.println(month.getDisplayName(TextStyle.NARROW, locale));
    System.out.println(month.getDisplayName(TextStyle.SHORT, locale));
  }
}
