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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

/**
 * java.time.temporal.TemporalQuery
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 16:38
 * @see java.time.temporal.TemporalQuery
 */
@DisplayName("TemporalQuery 可用于检索来自基于时间的对象的信息")
class TemporalQueryTest {

  @DisplayName("预定义的查询")
  static class Predefined {

    @Test
    @DisplayName("precision")
    void test1() {
      // 精度查询，不过返回的是英文
      TemporalQuery<TemporalUnit> query = TemporalQueries.precision();
      System.out.printf("LocalDate precision is %s%n", LocalDate.now().query(query));
      System.out.printf("LocalDateTime precision is %s%n", LocalDateTime.now().query(query));
      System.out.printf("Year precision is %s%n", Year.now().query(query));
      System.out.printf("YearMonth precision is %s%n", YearMonth.now().query(query));
      System.out.printf("Instant precision is %s%n", Instant.now().query(query));
    }

  }

  @DisplayName("自定义的查询")
  static class Custom {

    @Test
    @DisplayName("查询一个日子是否是家人重要的日子")
    void test1() {
      // 在此假设 4月 3号 ~ 4月8号 以及 8月 8号~14号 是重要的日子

      TemporalQuery<Boolean> query = date -> {
        int month = date.get(ChronoField.MONTH_OF_YEAR);
        int day = date.get(ChronoField.DAY_OF_MONTH);

        // Angie's 的生日是4月3号
        if ((month == Month.APRIL.getValue()) && (day == 3)) {
          return Boolean.TRUE;
        }

        // Sue's 的生日是6月18号
        if ((month == Month.JUNE.getValue()) && (day == 18)) {
          return Boolean.TRUE;
        }

        // Joe's 的生日是5月29号
        if ((month == Month.MAY.getValue()) && (day == 29)) {
          return Boolean.TRUE;
        }

        if (month == Month.APRIL.getValue() && (day >= 3 && day <= 8)) {
          return Boolean.TRUE;
        }

        if (month == Month.AUGUST.getValue() && (day >= 8 && day <= 14)) {
          return Boolean.TRUE;
        }

        return Boolean.FALSE;
      };

      LocalDate date = LocalDate.now();
      // 不使用拉姆达表达式查询
      Boolean isFamilyVacation = date.query(query);

      if (isFamilyVacation) {
        System.out.printf("%s 是一个重要的日子!%n", date);
      } else {
        System.out.printf("%s 不是一个重要的日子.%n", date);
      }

    }

  }

}
