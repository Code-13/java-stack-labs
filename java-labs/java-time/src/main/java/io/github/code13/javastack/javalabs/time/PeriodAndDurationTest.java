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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 周期和持续时间
 *
 * <p>当您编写代码来指定一段时间时，请使用最符合您需要的类或方法： Duration 类， Period 类或 ChronoUnit.between 方法。 Duration
 * 使用基于时间的值（秒，毫微秒）的时间量。Period 使用基于日期的值（年，月，日）。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 16:58
 * @see java.time.Duration
 * @see java.time.Period
 */
class PeriodAndDurationTest {

  /**
   * 在测量机器时间的情况下，Duration 最合适，例如使用 Instant 对象的代码。 Duration 对象以秒或纳秒度量， 不使用基于 Date
   * 的结构，如年、月和日，尽管类提供了转换为天数、小时和分钟的方法。 一个 Duration 可以有一个负值， 如果它是在开始点之前发生的端点创建的。
   */
  @DisplayName("java.time.Duration")
  static class DurationTest {

    @Test
    @DisplayName("Duration 示例")
    void test1() {
      Instant start = Instant.now();
      Duration gap = Duration.ofSeconds(10);
      Instant later = start.plus(gap);
      System.out.println(later);
    }

    @Test
    @DisplayName("ChronoUnit")
    void test2() {
      Instant current = Instant.now();
      // 10秒前
      Instant previous = current.minus(10, ChronoUnit.SECONDS);
      if (previous != null) {
        // 计算两个时间之前间隔多少毫秒
        long between = ChronoUnit.MILLIS.between(previous, current);
        System.out.println(between); // 10000
      }
    }
  }

  /**
   * 要用基于日期的值（年、月、日）来定义大量的时间，使用周期类。 周期类提供了各种 get 方法， 例如 getMonths， getDays 和
   * getYears，这样您就可以从周期中提取出时间的数量。
   */
  @DisplayName("Period 示例")
  static class PeriodTest {

    @Test
    @DisplayName("报告了你的年龄，假设你是在 1960 年 1 月 1 日出生的")
    void test1() {
      LocalDate today = LocalDate.now();
      // 1960.06.01
      LocalDate birthday = LocalDate.of(1960, Month.JANUARY, 1);

      Period p = Period.between(birthday, today);
      long p2 = ChronoUnit.DAYS.between(birthday, today);
      // 生活了58年，4个月，8天，总共21313天
      System.out.println(
          "You are "
              + p.getYears()
              + " years, "
              + p.getMonths()
              + " months, and "
              + p.getDays()
              + " days old. ("
              + p2
              + " days total)");

      // 下面是输出
      // You are 61 years, 5 months, and 14 days old.(22446 days total)

    }
  }
}
