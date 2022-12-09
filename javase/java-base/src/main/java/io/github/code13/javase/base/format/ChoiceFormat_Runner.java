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

import java.text.ChoiceFormat;
import java.text.NumberFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ChoiceFormat_Runner.
 *
 * @see java.text.ChoiceFormat
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/8 10:16
 */
public class ChoiceFormat_Runner {

  /*
   * 这个格式化器非常有意思：相当于以数字为键，字符串为值的键值对。
   * 使用一组double类型的数组作为键，一组String类型的数组作为值，两数组相同（不一定必须是相同，见示例）索引值的元素作为一对。
   */

  @Test
  @DisplayName("test1")
  void test1() {
    double[] limits = {1, 2, 3, 4, 5, 6, 7};
    String[] formats = {"周一", "周二", "周三", "周四", "周五", "周六", "周天"};
    NumberFormat numberFormat = new ChoiceFormat(limits, formats);

    System.out.println(numberFormat.format(1));
    System.out.println(numberFormat.format(4.3));
    System.out.println(numberFormat.format(5.8));
    System.out.println(numberFormat.format(9.1));
    System.out.println(numberFormat.format(11));

    // 周一
    // 周四
    // 周五
    // 周天
    // 周天

    // 结果解释：
    //
    // 4.3位于4和5之间，取值4；5.8位于5和6之间，取值5
    // 9.1和11均超过了数组最大值（或者说找不到匹配的），则取值最后一对键值对。
  }
}
