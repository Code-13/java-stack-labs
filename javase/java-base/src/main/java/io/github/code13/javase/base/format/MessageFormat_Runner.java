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

package io.github.code13.javase.base.format;

import java.text.MessageFormat;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MessageFormat_Runner.
 *
 * @see java.text.MessageFormat
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/8 10:21
 */
public class MessageFormat_Runner {

  /*
   * 字符串格式化
   *
   * MessageFormat提供了一种与语言无关（不管你在中国还是其它国家，效果一样）的方式生成拼接消息/拼接字符串的方法。使用它来构造显示给最终用户的消息。
   * MessageFormat接受一组对象，对它们进行格式化，然后在模式的适当位置插入格式化的字符串。
   */

  @Test
  @DisplayName("简单示例")
  void test1() {
    String sourceStrPattern = "Hello {0},my name is {1}";
    Object[] args = new Object[] {"girl", "YourBatman"};

    String formattedStr = MessageFormat.format(sourceStrPattern, args);
    System.out.println(formattedStr);
  }

  @Test
  @DisplayName("test2")
  void test2() {
    MessageFormat messageFormat =
        new MessageFormat(
            "Hello, my name is {0}. I’am {1,number,#.##} years old. Today is {2,date,yyyy-MM-dd HH:mm:ss}");
    // 亦可通过编程式 显示指定某个位置要使用的格式化器
    // messageFormat.setFormatByArgumentIndex(1, new DecimalFormat("#.###"));

    System.out.println(messageFormat.format(new Object[] {"WhoAMI", 24.123456, new Date()}));

    // Hello, my name is WhoAMI. I’am 24.12 years old. Today is 2022-11-08 10:28:55
  }

  @Test
  @DisplayName("test3")
  void test3() {
    System.out.println(MessageFormat.format("{1} - {1}", new Object[] {1})); // {1} - {1}
    System.out.println(MessageFormat.format("{0} - {1}", new Object[] {1})); // 输出：1 - {1}
    System.out.println(MessageFormat.format("{0} - {1}", new Object[] {1, 2, 3})); // 输出：1 - 2

    System.out.println("---------------------------------");

    System.out.println(MessageFormat.format("'{0} - {1}", new Object[] {1, 2})); // 输出：{0} - {1}
    System.out.println(MessageFormat.format("''{0} - {1}", new Object[] {1, 2})); // 输出：'1 - 2
    System.out.println(MessageFormat.format("'{0}' - {1}", new Object[] {1, 2})); // {0} - 2
    // 若你数据库值两边都需要''包起来，请你这么写
    System.out.println(MessageFormat.format("''{0}'' - {1}", new Object[] {1, 2})); // '1' - 2

    System.out.println("---------------------------------");
    System.out.println(MessageFormat.format("0} - {1}", new Object[] {1, 2})); // 0} - 2
    System.out.println(
        MessageFormat.format(
            "{0 - {1}",
            new Object[] {
              1, 2
            })); // java.lang.IllegalArgumentException: Unmatched braces in the pattern.

    // 参数模式的索引值必须从0开始，否则所有索引值无效
    // 实际传入的参数个数可以和索引个数不匹配，不报错（能匹配上几个算几个）
    // 两个单引号''才算作一个'，若只写一个将被忽略甚至影响整个表达式  谨慎使用单引号'  关注'的匹配关系
    // {}只写左边报错，只写右边正常输出（注意参数的对应关系）
  }
}
