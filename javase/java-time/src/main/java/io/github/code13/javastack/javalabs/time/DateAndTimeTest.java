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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 13:49
 */
@DisplayName("日期和时间类 / Date and Time")
class DateAndTimeTest {

  @DisplayName("java.time.LocalTime")
  static class LocalTimeTest {

    @Test
    @DisplayName("此类对于表示基于人的时间（如电影时间）或本地图书馆的开始和结束时间很有用,在 LocalTime 类不存储时区或夏令时信息")
    void test1() {
      LocalTime thisSec = LocalTime.now();
      display(thisSec.getHour(), thisSec.getMinute(), thisSec.getSecond());
    }

    private void display(int hour, int minute, int second) {
      System.out.println(hour + ":" + minute + ":" + second);
    }
  }

  /**
   * 无时区的时间类是 LocalDateTime，它是 Date-Time API 的核心类之一。 这个类用来表示日期（月 - 日 - 年）和时间（小时 - 分秒 - 纳秒）， 实际上是
   * LocalDate 和 LocalTime 的组合。 本类可以用来表示一个特定的事件，例如 2013 年 8 月 17 日下午 1:10 开始的美洲杯挑战者系列赛第一场路易威登杯决赛。
   * 请注意，这意味着下午 1:10 在当地时间。要包含时区，您必须使用 ZonedDateTime 或 OffsetDateTime，如 时区和偏移类中所述
   */
  @DisplayName("java.time.LocalDateTime")
  static class LocalDateTimeTest {

    @Test
    @DisplayName("LocalDateTime 示例")
    void test1() {
      System.out.printf("now: %s%n", LocalDateTime.now());

      // 1994年4月15号11点30分
      System.out.printf(
          "Apr 15, 1994 @ 11:30am: %s%n", LocalDateTime.of(1994, Month.APRIL, 15, 11, 30));

      // 基于 瞬时类 只有纳秒 + 时区id
      System.out.printf(
          "now (from Instant): %s%n",
          LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));

      // 6月后
      System.out.printf("6 months from now: %s%n", LocalDateTime.now().plusMonths(6));

      // 6月前
      System.out.printf("6 months ago: %s%n", LocalDateTime.now().minusMonths(6));
    }
  }
}
