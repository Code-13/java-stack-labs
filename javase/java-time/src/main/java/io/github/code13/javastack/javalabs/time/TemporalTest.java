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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 14:52
 * @see java.time.temporal.Temporal
 */
@DisplayName("Temporal 包")
class TemporalTest {

  /**
   * java.time.temporal 提供了一组接口、类和枚举，它们支持日期和时间代码，特别是日期和时间计算。
   * 这些接口的目的是在最低级别使用。典型的应用程序代码应该以具体类型的形式声明变量和参数， 比如 LocalDate 或 ZonedDateTime. 而不是 Temporal 接口。这与声明
   * String 类型的变量完全相同，而不是 CharSequence 类型。
   */

  /**
   * Temporal 接口提供了访问 temporal - based 对象的框架；并通过基于时间的类，如实现 Instant, LocalDateTime, and
   * ZonedDateTime. 该接口提供了添加或减少时间单位的方法，使得基于时间的算术在各种日期和时间类中变得简单和一致。 TemporalAccessor 接口提供的只读版本
   * Temporal 接口。
   *
   * <p>Temporal and TemporalAccessor 对象是用字段来定义的，如TemporalField 接口;字段的单位由 TemporalUnit 接口指定 Temporal
   * 接口中的基于算术的方法需要使用 TemporalAmount 值定义的参数
   */

  /**
   * 实现 TemporalField 接口的 ChronoField 枚举提供了一组用于访问日期和时间值的常量。 一些示例是 CLOCK_HOUR_OF_DAY，NANO_OF_DAY 和
   * DAY_OF_YEAR。 此枚举可用于表示时间的概念方面， 例如一年的第三周，一天的第 11 个小时或本月的第一个星期一。 当你遇到位置类型的 Temporal 时， 可以使用
   * TemporalAccessor.isSupported（TemporalField） 方法来确定时态是否支持特定的字段。 下面这行代码返回 false，表明 LocalDate 不支持
   * ChronoField.CLOCK_HOUR_OF_DAY 字段的单位由。
   */
  @DisplayName("java.")
  static class ChronoFieldTest {

    @Test
    @DisplayName("isSupported")
    void test1() {
      // 是否支持 am.pm 上午下午这样的字段
      // 由于 LocalDate 不包含时分秒，所以不支持
      boolean supported = LocalDate.now().isSupported(ChronoField.CLOCK_HOUR_OF_DAY);
      System.out.println(supported);
    }

    @Test
    @DisplayName("示例")
    void test2() {
      Instant time = Instant.now();
      // 返回当前时间的毫秒数也就是秒后面的毫秒数0-999
      int i = time.get(ChronoField.MILLI_OF_SECOND);
      LocalDateTime date = LocalDateTime.now();
      // 返回了当前日期所属日期
      int qoy = date.get(IsoFields.QUARTER_OF_YEAR);
      // 当前毫秒：256
      System.out.println("当前毫秒：" + i);
      // 所属季度：2
      System.out.println("所属季度：" + qoy);
    }
  }

  @DisplayName("ChronoUnit")
  static class ChronoUnitTest {

    @Test
    @DisplayName("supported")
    void test1() {
      Instant now = Instant.now();
      boolean isSupported = now.isSupported(ChronoUnit.DAYS);
      System.out.println(isSupported);
    }
  }
}
