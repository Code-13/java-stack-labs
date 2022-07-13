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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 15:41
 * @see TemporalAdjuster
 * @see TemporalAdjusters
 */
class TemporalAdjusterTest {

  /** TemporalAdjuster 接口，在 java.time.temporal 包，提供了具有时间价值并返回调整值的方法。 调节器可以用于任何基于时间/ Temporal 的类型。 */
  @DisplayName("预定义的 TemporalAdjuster")
  static class Predefined {

    /**
     * TemporalAdjusters 类（注意是复数）提供了一组时间调节器，比如查找月的第一天或最后一天预定义的调节器，
     * 在今年的第一天或最后一天，每月的最后一个星期三，或特定日期后的第一个星期二。 预定义的调整器被定义为静态方法，并被设计为与 静态导入语句一起使用。
     */
    @Test
    @DisplayName("示例：")
    void test1() {
      // 2000 10 15
      LocalDate date = LocalDate.of(2000, Month.OCTOBER, 15);
      DayOfWeek dotw = date.getDayOfWeek(); // 获取当天是周几
      System.out.printf("%s is on a %s%n", date, dotw);

      System.out.printf(
          "first day of Month: %s%n", date.with(TemporalAdjusters.firstDayOfMonth())); // 当月第一天
      System.out.printf(
          "first Monday of Month: %s%n",
          date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))); // 当月第一个周一
      System.out.printf(
          "last day of Month: %s%n", date.with(TemporalAdjusters.lastDayOfMonth())); // 当月最后一天
      System.out.printf(
          "first day of next Month: %s%n",
          date.with(TemporalAdjusters.firstDayOfNextMonth())); // 基于当前月的下个月第一天
      System.out.printf(
          "first day of next Year: %s%n",
          date.with(TemporalAdjusters.firstDayOfNextYear())); // 基于当前时间 下一年的第一天
      System.out.printf(
          "first day of Year: %s%n", date.with(TemporalAdjusters.firstDayOfYear())); // 基于当年的第一天
    }
  }

  @DisplayName("自定义 TemporalAdjuster")
  static class Custom {

    @Test
    @DisplayName("自定义一个调结器：每月 15 号发工资，但是你是 15 号后入职的，那么就月底最后一天发，如果遇到周 6 周日，则推前到周 5")
    void test1() {
      TemporalAdjuster adjuster =
          input -> {
            LocalDate date = LocalDate.from(input);
            int day;

            if (date.getDayOfMonth() <= 15) {
              day = 15;
            } else {
              day = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            }

            date = date.withDayOfMonth(day);

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
              date = date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            }

            return input.with(date);
          };

      LocalDate d1 = LocalDate.of(2018, 05, 13);
      LocalDate d2 = LocalDate.of(2018, 05, 16);
      System.out.println(d1.with(adjuster));
      System.out.println(d2.with(adjuster));
    }
  }
}
