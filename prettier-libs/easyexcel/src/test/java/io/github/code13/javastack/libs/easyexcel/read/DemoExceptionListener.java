/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.libs.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoExceptionListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:35 PM
 */
public class DemoExceptionListener implements ReadListener<DemoExtraData> {

  private static final Logger logger = LoggerFactory.getLogger(DemoExceptionListener.class);

  @Override
  public void onException(Exception exception, AnalysisContext context) throws Exception {
    logger.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
    // 如果是某一个单元格的转换异常 能获取到具体行号
    // 如果要获取头的信息 配合invokeHeadMap使用
    if (exception instanceof ExcelDataConvertException) {
      ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
      logger.error(
          "第{}行，第{}列解析异常，数据为:{}",
          excelDataConvertException.getRowIndex(),
          excelDataConvertException.getColumnIndex(),
          excelDataConvertException.getCellData());
    }
  }

  @Override
  public void invoke(DemoExtraData data, AnalysisContext context) {}

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}
}
