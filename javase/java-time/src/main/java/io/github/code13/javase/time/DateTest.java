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

package io.github.code13.javase.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 日期类
 *
 * <p>日期时间 API 提供四个专门处理日期信息的类，不考虑时间或时区。类名建议使用这些类：LocalDate，YearMonth，MonthDay 和 Year。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 12:10
 */
@DisplayName("日期类：LocalDate，YearMonth，MonthDay 和 Year")
class DateTest {

  /** 一个 LocalDate 代表 年月日的 ISO 日历，表示没有时间的日期是有用的。 您可以使用 LocalDate 跟踪重大事件， 例如出生日期或结婚日期。 */
  @DisplayName("LocalDate")
  static class LocalDateTest {

    @Test
    @DisplayName("以下示例使用 of 和 with 方法来创建 LocalDate 的实例")
    void test1() {
      // 2000年11月20日 星期一
      LocalDate date = LocalDate.of(2000, Month.NOVEMBER, 20);

      // 当前指定日期的下一个 星期三
      LocalDate nextWed = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));

      // 当前指定日期的下一个 星期一
      LocalDate nextMond = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

      System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(date));
      System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(nextWed));
      System.out.println(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(nextMond));
    }

    @Test
    @DisplayName("getDayOfWeek")
    void test2() {
      DayOfWeek dayOfWeek = LocalDate.of(2012, Month.JULY, 9).getDayOfWeek();
      System.out.println(dayOfWeek);
    }

    @Test
    @DisplayName("TemporalAdjuster 检索特定日期后的第一个星期三")
    void test3() {
      LocalDate date = LocalDate.of(2000, Month.NOVEMBER, 20);
      LocalDate nextWed = date.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
      System.out.printf("For the date of %s, the next Wednesday is %s.%n", date, nextWed);

      // For the date of 2000-11-20, the next Wednesday is 2000-11-22.
    }
  }

  @DisplayName("YearMonth 类代表一个特定的一年中的月份")
  static class YearMonthTest {

    @Test
    @DisplayName("使用 YearMonth.lengthOfMonth() 方法 返回该时间月有多少天")
    void test1() {
      YearMonth now = YearMonth.now();
      System.out.printf("%s: %d%n", now, now.lengthOfMonth());
    }

    @Test
    @DisplayName("lengthOfYear 返回该年有多少天")
    void test2() {
      YearMonth date = YearMonth.of(2012, Month.FEBRUARY);
      // 2012-02: 366  // 返回该年有多少天
      System.out.printf("%s: %d%n", date, date.lengthOfYear());
    }
  }

  @DisplayName("某月的一天")
  static class MonthDayTest {

    @Test
    @DisplayName("使用 MonthDay.isValidYear 方法来确定 2 月 29 日是否对 2010 年有效")
    void test1() {
      // 2 月29号
      MonthDay date = MonthDay.of(Month.FEBRUARY, 29);
      // 对于 2010 年是否是有效的时间
      boolean validLeapYear = date.isValidYear(2010);

      System.out.println(validLeapYear);
    }
  }

  @DisplayName("Year: 代表一年")
  static class YearTest {

    @Test
    @DisplayName("判断是否是闰年")
    void test1() {
      boolean leap = Year.of(2021).isLeap();
      System.out.println(leap);
    }
  }
}
