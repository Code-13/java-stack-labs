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

import java.text.DecimalFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DecimalFormat_Runner.
 *
 * @see java.text.DecimalFormat
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/8 10:06
 */
public class DecimalFormat_Runner {

  /*
   * DecimalFormat
   *
   * 用于格式化十进制数字。它具有各种特性，可以解析和格式化数字，包括：西方数字、阿拉伯数字和印度数字。
   * 它还支持不同种类的数字，包括：整数(123)、小数(123.4)、科学记数法(1.23E4)、百分数(12%)和货币金额($123)。
   * 所有这些都可以进行本地化。
   *
   * 详情见 format.md
   */

  @Test
  @DisplayName("0和#的使用（最常见使用场景）")
  void test1() {
    double myNum = 1220.0455;

    System.out.println("===============0的使用===============");
    System.out.println("只保留整数部分：" + new DecimalFormat("0").format(myNum));
    System.out.println("保留3位小数：" + new DecimalFormat("0.000").format(myNum));
    System.out.println(
        "整数部分、小数部分都5位。不够的都用0补位(整数高位部，小数低位补)：" + new DecimalFormat("00000.00000").format(myNum));

    System.out.println("===============#的使用===============");
    System.out.println("只保留整数部分：" + new DecimalFormat("#").format(myNum));
    System.out.println("保留2为小数并以百分比输出：" + new DecimalFormat("#.##%").format(myNum));

    // 非标准数字（不建议这么用）
    System.out.println("===============非标准数字的使用===============");
    System.out.println(new DecimalFormat("666").format(myNum));
    System.out.println(new DecimalFormat(".6666").format(myNum));
  }

  /** DecimalFormat 科学计数法 */
  @Test
  @DisplayName("科学计数法E")
  void test2() {
    double myNum = 1220.0455;

    System.out.println(new DecimalFormat("0E0").format(myNum));
    System.out.println(new DecimalFormat("0E00").format(myNum));
    System.out.println(new DecimalFormat("00000E00000").format(myNum));
    System.out.println(new DecimalFormat("#E0").format(myNum));
    System.out.println(new DecimalFormat("#E00").format(myNum));
    System.out.println(new DecimalFormat("#####E00000").format(myNum));

    // 1E3
    // 1E03
    // 12200E-00001
    // .1E4
    // .1E04
    // 1220E00000
  }

  @Test
  @DisplayName("分组分隔符 , ")
  void test3() {
    double myNum = 1220.0455;

    System.out.println(new DecimalFormat(",###").format(myNum));
    System.out.println(new DecimalFormat(",##").format(myNum));
    System.out.println(new DecimalFormat(",##").format(123456789));

    // 分隔符,左边是无效的
    System.out.println(new DecimalFormat("###,##").format(myNum));

    // 1,220
    // 12,20
    // 1,23,45,67,89
    // 12,20
  }

  @Test
  @DisplayName("百分号%")
  void test4() {
    double myNum = 1220.0455;

    System.out.println("百分位表示：" + new DecimalFormat("#.##%").format(myNum));
    System.out.println("千分位表示：" + new DecimalFormat("#.##\u2030").format(myNum));
  }

  @Test
  @DisplayName("本地货币符号¤")
  void test5() {
    double myNum = 1220.0455;

    System.out.println(new DecimalFormat(",000.00¤").format(myNum));
    System.out.println(new DecimalFormat(",000.¤00").format(myNum));
    System.out.println(new DecimalFormat("¤,000.00").format(myNum));
    System.out.println(new DecimalFormat("¤,000.¤00").format(myNum));
    // 世界货币表达形式
    System.out.println(new DecimalFormat(",000.00¤¤").format(myNum));

    // 1,220.05¥
    // 1,220.05¥
    // ¥1,220.05
    // ¥1,220.05¥
    // 1,220.05CNY
  }
}
