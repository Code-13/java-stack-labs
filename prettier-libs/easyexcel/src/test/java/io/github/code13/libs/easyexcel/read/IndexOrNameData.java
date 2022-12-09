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

package io.github.code13.libs.easyexcel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * IndexOrNameData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 3:42 PM
 */
@Data
public class IndexOrNameData {

  /** 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配 */
  @ExcelProperty(index = 2)
  private Double doubleData;

  /** 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据 */
  @ExcelProperty("字符串标题")
  private String string;

  @ExcelProperty("日期标题")
  private LocalDateTime date;
}
