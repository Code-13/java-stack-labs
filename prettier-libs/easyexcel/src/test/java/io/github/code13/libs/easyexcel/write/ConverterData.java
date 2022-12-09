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

package io.github.code13.libs.easyexcel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * ConverterData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/15/2022 10:21 AM
 */
@Data
public class ConverterData {

  /** 我想所有的 字符串起前面加上"自定义："三个字 */
  @ExcelProperty(value = "字符串标题", converter = CustomStringStringConverter.class)
  private String string;

  /** 我想写到excel 用年月日的格式 */
  @DateTimeFormat("yyyy/MM/dd ss:mm:ss")
  @ExcelProperty("日期标题")
  private LocalDateTime date;

  /** 我想写到excel 用百分比表示 */
  @NumberFormat("#.##%")
  @ExcelProperty(value = "数字标题")
  private Double doubleData;
}
