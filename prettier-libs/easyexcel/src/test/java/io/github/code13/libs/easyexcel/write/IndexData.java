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
import java.time.LocalDateTime;
import lombok.Data;

/**
 * IndexData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/15/2022 9:56 AM
 */
@Data
public class IndexData {

  @ExcelProperty(value = "字符串标题", index = 0)
  private String string;

  @ExcelProperty(value = "日期标题", index = 1)
  private LocalDateTime date;

  /** 这里设置3，会导致第二列为空 */
  @ExcelProperty(value = "数字标题", index = 3)
  private Double doubleData;
}
