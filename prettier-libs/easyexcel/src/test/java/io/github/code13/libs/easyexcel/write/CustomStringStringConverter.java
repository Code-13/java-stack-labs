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

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * CustomStringStringConverter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 4:23 PM
 */
public class CustomStringStringConverter implements Converter<String> {

  @Override
  public Class<?> supportJavaTypeKey() {
    return String.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  /**
   * 这里读的时候会调用
   *
   * @param context
   * @return
   */
  @Override
  public String convertToJavaData(ReadConverterContext<?> context) {
    return "自定义：" + context.getReadCellData().getStringValue();
  }

  /**
   * 这里是写的时候会调用 不用管
   *
   * @return
   */
  @Override
  public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) {
    return new WriteCellData<>(context.getValue());
  }
}
