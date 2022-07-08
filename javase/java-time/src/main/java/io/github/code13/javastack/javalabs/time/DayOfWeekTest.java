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
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 指定一周中的几天
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/15 11:47
 * @see java.time.DayOfWeek
 */
@DisplayName("java.time.DayOfWeek")
class DayOfWeekTest {

  /**
   * DayOfWeek 由七个常量形容一周的日子：星期一至星期日。 DayOfWeek 常量的整数值范围从 1（星期一）到 7（星期日）。
   * 使用定义的常量（DayOfWeek.FRIDAY）使您的代码更具可读性。
   */
  @Test
  @DisplayName("打印所有的 DayOfWeek")
  void test1() {
    Arrays.stream(DayOfWeek.values()).forEachOrdered(System.out::println);

    // MONDAY
    // TUESDAY
    // WEDNESDAY
    // THURSDAY
    // FRIDAY
    // SATURDAY
    // SUNDAY
  }

  @Test
  @DisplayName("将“周一”添加 3 天并打印结果。输出是 “THURSDAY”")
  void test2() {
    System.out.printf("%s%n", DayOfWeek.MONDAY.plus(3));
  }

  @Test
  @DisplayName("使用 getDisplayName(TextStyle, Locale) 方法， 相当于使用指定的语言环境进行翻译")
  void test3() {
    DayOfWeek dow = DayOfWeek.MONDAY;
    Locale locale = Locale.getDefault();
    // 星期一
    System.out.println(dow.getDisplayName(TextStyle.FULL, locale));
    // 一
    System.out.println(dow.getDisplayName(TextStyle.NARROW, locale));
    // 星期一
    System.out.println(dow.getDisplayName(TextStyle.SHORT, locale));
  }
}
