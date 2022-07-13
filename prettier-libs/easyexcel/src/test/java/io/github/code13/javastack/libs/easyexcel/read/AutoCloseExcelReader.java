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

package io.github.code13.javastack.libs.easyexcel.read;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.metadata.ReadSheet;
import java.util.List;

/**
 * AutoCloseExcelReader.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 3:37 PM
 */
public class AutoCloseExcelReader implements AutoCloseable {

  private final ExcelReader excelReader;

  public AutoCloseExcelReader(ExcelReader excelReader) {
    this.excelReader = excelReader;
  }

  public void readAll() {
    excelReader.readAll();
  }

  public ExcelReader read(ReadSheet... readSheet) {
    return excelReader.read(readSheet);
  }

  public ExcelReader read(List<ReadSheet> readSheetList) {
    return excelReader.read(readSheetList);
  }

  public AnalysisContext analysisContext() {
    return excelReader.analysisContext();
  }

  public ExcelReadExecutor excelExecutor() {
    return excelReader.excelExecutor();
  }

  public void finish() {
    excelReader.finish();
  }

  @Override
  public void close() throws Exception {
    if (excelReader != null) {
      excelReader.finish();
    }
  }
}
