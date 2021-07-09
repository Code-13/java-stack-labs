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

package io.github.code13.javastack.javalabs.time;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 问题与练习
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 17:14
 */
@DisplayName("问题与练习")
class Exercises {

  @Test
  @DisplayName("你会在几年，几个月，几天，几秒和几纳秒之间使用哪一类来存储你的生日？")
  void test1() {
    System.out.println(LocalDate.class.getName());
  }

  @Test
  @DisplayName("给定一个随机日期，你如何找到上个星期四的日期？")
  void test2() {
    LocalDate now = LocalDate.now();
    LocalDate with = now.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY));
  }

  @Test
  @DisplayName("ZoneId 和 ZoneOffset 有什么区别？")
  void test3() {
    // ZoneId 中包含了 ZoneOffset
  }

  @Test
  @DisplayName("你如何将 Instant 转换为 ZonedDateTime？你会如何转换一个 ZonedDateTime 到 Instant？")
  void test4() {
    Instant instant = Instant.now();
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

    ZonedDateTime now = ZonedDateTime.now();
    Instant instant1 = now.toInstant();
  }

  @Test
  @DisplayName("写一个例子，给定一个年份，打印出该年中每个月有多少天")
  void test5() {
    int year = 2021;
    Arrays.stream(Month.values())
        .map(month -> LocalDate.of(year, month.getValue(), 1))
        .forEach(
            date -> {
              Month month = date.getMonth();
              String displayNameMonth = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
              System.out.printf("%s: %d days%n", displayNameMonth, date.lengthOfMonth());
              System.out.format("%3s %3s%n", displayNameMonth, date.lengthOfMonth());
            });
  }

  @Test
  @DisplayName("写一个例子，在当年的某个特定月份，列出当月的所有星期一")
  void test6() {
    int m = 6;
    Month month = Month.of(m);

    LocalDate date =
        Year.now().atMonth(month).atDay(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

    Month mi = date.getMonth();
    System.out.printf("%s的星期一有以下几天:%n", month.getDisplayName(TextStyle.FULL, Locale.getDefault()));

    while (mi == month) {
      System.out.printf("%s%n", date);
      date = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
      mi = date.getMonth();
    }
  }

  @Test
  @DisplayName("写一个例子，测试一个给定的日期是否发生在 13 日星期五。")
  void test7() {
    // 假设给定5月13号，判断该日期是否是13号并且还是星期五
    int m = 5;
    int day = 13;
    LocalDate date = Year.now().atMonth(m).atDay(day);

    Boolean query =
        date.query(
            temporal -> {
              int dayOfMonth = temporal.get(ChronoField.DAY_OF_MONTH);
              int dayOfWeek = temporal.get(ChronoField.DAY_OF_WEEK);
              return dayOfMonth == 13 && dayOfWeek == 5;
            });

    System.out.println(query);
  }

  @Test
  @DisplayName("找一年中是13号又是星期5的日期")
  void test8() {
    int year = 2021;
    LocalDate date =
        Year.of(year).atMonth(1).atDay(1).with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY));
    int targetYear = date.getYear();
    while (targetYear == year) {
      Boolean query =
          date.query(
              temporal -> {
                int dayOfMonth = temporal.get(ChronoField.DAY_OF_MONTH);
                int dayOfWeek = temporal.get(ChronoField.DAY_OF_WEEK);
                return dayOfMonth == 13 && dayOfWeek == 5;
              });

      if (query) {
        System.out.println(date);
      }

      date = date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
      targetYear = date.getYear();
    }
  }
}
