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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Instant 类返回的值计算从 1970 年 1 月 1 日（1970-01-01T00：00：00Z）第一秒开始的时间， 也称为 EPOCH
 * <p>
 * 发生在时期之前的瞬间具有负值，并且发生在时期后的瞬间具有正值 （1970-01-01T00：00：00Z 中的 Z 其实就是偏移量为 0）
 * <p>
 * Instant 类提供的其他常量是 MIN 表示最小可能（远远）的瞬间， MAX 表示最大（远期）瞬间。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 14:32
 */
@DisplayName("java.time.Instant")
class InstantTest {

  @Test
  @DisplayName("Instant")
  void test1() {
    Instant oneHourLater = Instant.now().plus(1, ChronoUnit.HOURS);

    System.out.println(oneHourLater);

    long secondsFromEpoch = Instant.ofEpochSecond(0L).until(Instant.now(),
      ChronoUnit.SECONDS);

    System.out.println(secondsFromEpoch);

    LocalDateTime start = LocalDateTime.of(2018, 05, 01, 0, 0, 0);
    LocalDateTime end = LocalDateTime.of(2018, 05, 8, 0, 0, 0);

    // 两个时间之间相差了7天
    long until = start.until(end, ChronoUnit.DAYS);
    System.out.println(until);
  }

  @Test
  @DisplayName("Instant 不包含年，月，日等单位。但是可以转换成 LocalDateTime 或 ZonedDateTime， 如下 把一个 Instant + 默认时区转换成一个 LocalDateTime")
  void test2() {
    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    // MAY 8 2018 at 13:37
    System.out.printf("%s %d %d at %d:%d%n", ldt.getMonth(), ldt.getDayOfMonth(), ldt.getYear(), ldt.getHour(), ldt.getMinute());
  }

}
