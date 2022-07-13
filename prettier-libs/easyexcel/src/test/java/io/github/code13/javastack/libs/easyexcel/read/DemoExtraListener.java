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

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoExtraListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:17 PM
 */
public class DemoExtraListener implements ReadListener<DemoExtraData> {

  private static final Logger logger = LoggerFactory.getLogger(DemoExtraListener.class);

  @Override
  public void invoke(DemoExtraData data, AnalysisContext context) {}

  @Override
  public void extra(CellExtra extra, AnalysisContext context) {
    logger.info("读取到了一条额外信息:{}", extra.toString());
    switch (extra.getType()) {
      case COMMENT:
        logger.info(
            "额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}",
            extra.getRowIndex(),
            extra.getColumnIndex(),
            extra.getText());
        break;
      case HYPERLINK:
        if ("Sheet1!A1".equals(extra.getText())) {
          logger.info(
              "额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}",
              extra.getRowIndex(),
              extra.getColumnIndex(),
              extra.getText());
        } else if ("Sheet2!A1".equals(extra.getText())) {
          logger.info(
              "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                  + "内容是:{}",
              extra.getFirstRowIndex(),
              extra.getFirstColumnIndex(),
              extra.getLastRowIndex(),
              extra.getLastColumnIndex(),
              extra.getText());
        } else {
          Assertions.fail("Unknown hyperlink!");
        }
        break;
      case MERGE:
        logger.info(
            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
            extra.getFirstRowIndex(),
            extra.getFirstColumnIndex(),
            extra.getLastRowIndex(),
            extra.getLastColumnIndex());
        break;
      default:
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}
}
