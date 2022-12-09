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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Format_Runner.
 *
 * @see java.text.Format
 * @see java.text.NumberFormat
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/8 09:51
 */
public class Format_Runner {

  @Test
  @DisplayName("DateFormat")
  void dateFormat() {
    Date curr = new Date();

    // 格式化日期 + 时间
    System.out.println(
        DateFormat.getInstance().getClass() + "-->" + DateFormat.getInstance().format(curr));
    System.out.println(
        DateFormat.getDateTimeInstance().getClass()
            + "-->"
            + DateFormat.getDateTimeInstance().format(curr));

    // 格式化日期
    System.out.println(
        DateFormat.getDateInstance().getClass()
            + "-->"
            + DateFormat.getDateInstance().format(curr));

    // 格式化时间
    System.out.println(
        DateFormat.getTimeInstance().getClass()
            + "-->"
            + DateFormat.getTimeInstance().format(curr));
  }

  @Test
  @DisplayName("SimpleDateFormat")
  void simpleDateFormat() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd HH:mm:ss
    System.out.println(dateFormat.format(new Date()));
  }

  @Test
  @DisplayName("NumberFormat")
  void numberFormat() {
    double myNum = 1220.0455;

    System.out.println(
        NumberFormat.getInstance().getClass() + "-->" + NumberFormat.getInstance().format(myNum));
    System.out.println(
        NumberFormat.getCurrencyInstance().getClass()
            + "-->"
            + NumberFormat.getCurrencyInstance().format(myNum));
    System.out.println(
        NumberFormat.getIntegerInstance().getClass()
            + "-->"
            + NumberFormat.getIntegerInstance().format(myNum));
    System.out.println(
        NumberFormat.getNumberInstance().getClass()
            + "-->"
            + NumberFormat.getNumberInstance().format(myNum));
    System.out.println(
        NumberFormat.getPercentInstance().getClass()
            + "-->"
            + NumberFormat.getPercentInstance().format(myNum));
  }
}
