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

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 13:55
 */
class ZoneAndOffsetTest {

  @DisplayName("ZoneId: 指定时区标识符并提供 Instant 和 LocalDateTime 之间转换的规则。")
  static class ZoneIdTest {

    @Test
    @DisplayName("ZoneId")
    void test1() {
      // 获取所有可用的时区
      Set<String> allZones = ZoneId.getAvailableZoneIds();

      // 按自然顺序排序
      // Create a List using the set of zones and sort it.
      List<String> zoneList = new ArrayList<>(allZones);
      Collections.sort(zoneList);

      LocalDateTime dt = LocalDateTime.now();

      for (String s : zoneList) {
        ZoneId zone = ZoneId.of(s);

        ZonedDateTime zonedDateTime = dt.atZone(zone);

        ZoneOffset offset = zonedDateTime.getOffset();

        int secondsOfHour = offset.getTotalSeconds() % (60 * 60);

        String out = String.format("%35s %10s%n", zone, offset);

        if (secondsOfHour != 0) {
          System.out.printf(out);
        }
      }
    }
  }

  /**
   * 你什么时候使用 OffsetDateTime 而不是 ZonedDateTime？ 如果您正在编写复杂的软件，
   * 该软件根据地理位置对自己的日期和时间计算规则进行建模，或者将时间戳存储在仅跟踪格林威治/ UTC 时间的绝对偏移量的数据库中， 则可能需要使用 OffsetDateTime。另外，XML
   * 和其他网络格式将日期时间传输定义为 OffsetDateTime 或 OffsetTime。
   */
  @DisplayName("java.time.ZonedDateTime")
  static class ZonedDateTimeTest {
    // 实际上，结合了 LocalDateTime 与类 了 zoneid 类。它用于表示具有时区（地区/城市，如欧洲/巴黎）的完整日期（年，月，日）和时间（小时，分钟，秒，纳秒）。

    @Test
    @DisplayName("ZonedDateTime 示例")
    void test1() {
      // DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");
      DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd  HH:mm:ss");

      // Leaving from San Francisco on July 20, 2013, at 7:30 p.m.
      // 2013-07-20  19:30:00
      LocalDateTime leaving = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
      ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
      ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

      try {
        String out1 = departure.format(format);
        System.out.printf("LEAVING:  %s (%s)%n", out1, leavingZone);
      } catch (DateTimeException exc) {
        System.out.printf("%s can't be formatted!%n", departure);
        throw exc;
      }

      // Flight is 10 hours and 50 minutes, or 650 minutes
      ZoneId arrivingZone = ZoneId.of("Asia/Tokyo");
      // 使用美国洛杉矶出发的时间，然后换算成东京的时区，返回该时区对应的时间
      ZonedDateTime arrival =
          departure.withZoneSameInstant(arrivingZone).plusMinutes(650); // 在该时区的基础上加650分钟

      try {
        String out2 = arrival.format(format);
        System.out.printf("ARRIVING: %s (%s)%n", out2, arrivingZone);
      } catch (DateTimeException exc) {
        System.out.printf("%s can't be formatted!%n", arrival);
        throw exc;
      }

      // 夏令时
      if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant())) {
        System.out.printf("  (%s daylight saving time will be in effect.)%n", arrivingZone);
      } else {
        // 标准时间
        System.out.printf("  (%s standard time will be in effect.)%n", arrivingZone);
      }
    }
  }

  /**
   * 结合了 LocalDateTime 与类 ZoneOffset 类。 它用于表示格林威治/ UTC 时间的偏移量 （+/-小时：分钟，例如 +06：00
   * 或-）的整个日期（年，月，日）和时间（小时，分钟，秒，纳秒）08:00）。
   */
  @DisplayName("java.time.OffsetDateTime")
  static class OffsetDateTimeTest {

    @Test
    @DisplayName("OffsetDateTime")
    void test1() {
      LocalDateTime localDate = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);
      ZoneOffset offset = ZoneOffset.of("-08:00");

      OffsetDateTime offsetDate = OffsetDateTime.of(localDate, offset);

      OffsetDateTime lastThursday =
          offsetDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
      System.out.printf(
          "The last Thursday in July 2013 is the %sth.%n", lastThursday.getDayOfMonth());
    }
  }

  /**
   * 结合 LocalDateTime 与类 ZoneOffset 类。它用于表示格林威治/ UTC 时间偏移 （+/-小时：分钟，例如+06：00或-08：00）的时间（小时，分钟，秒，纳秒）。
   */
  @DisplayName("java.time.OffsetTime")
  static class OffsetTime {

    @Test
    @DisplayName("OffsetTime 示例")
    void test1() {
      // 一个不带任何时区的时间
      LocalDateTime date = LocalDateTime.of(2018, 05, 01, 0, 0, 0);

      ZonedDateTime d1 = ZonedDateTime.of(date, ZoneId.systemDefault());

      ZoneOffset offset = ZoneOffset.of("+08:00");
      OffsetDateTime d2 = OffsetDateTime.of(date, offset);

      // 2018-05-01T00:00+08:00[GMT+08:00]
      // ZoneId 带了具体的ID
      System.out.println(d1);
      // 2018-05-01T00:00+08:00
      // 而偏移没有ID,因为多个ID对应的值有可能是一样的
      System.out.println(d2);

      // 那么把中国时间变成其他的时间
      // 2018-04-30T20:00+04:00[Asia/Yerevan]
      // 把该时间转换成指定时区了
      d1.withZoneSameInstant(ZoneId.of("Asia/Yerevan"));
      // 2018-05-01T00:00+04:00[Asia/Yerevan]
      // 只是改变了时区
      d1.withZoneSameLocal(ZoneId.of("Asia/Yerevan"));
    }
  }
}
