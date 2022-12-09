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

package io.github.code13.libs.easyexcel.write.template;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import java.util.List;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * MergeStrategy.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/31 5:53 PM
 */
public class MergeStrategy extends AbstractMergeStrategy {

  private final List<IclExportDataDTO> dtos;

  public MergeStrategy(List<IclExportDataDTO> iclExportDataDTOS) {
    dtos = iclExportDataDTOS;
  }

  @Override
  protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
    // 0 ~ 22

    int[] columns = Stream.iterate(0, i -> i + 1).limit(23).mapToInt(Integer::intValue).toArray();

    if (cell.getRowIndex() == 2 && cell.getColumnIndex() == 0) {
      // 第几行需要合并
      int startRow = 2;
      int endRow = 3;
      for (IclExportDataDTO dto : dtos) {
        List<IobExportDataDTO> iobExportDataList = dto.getIobExportDataList();

        // 如果单元格只有一个，不需要合并
        if (iobExportDataList.size() == 1) {
          endRow++;
        } else {
          endRow = startRow + iobExportDataList.size() - 1;
          for (int c : columns) {
            sheet.addMergedRegionUnsafe(new CellRangeAddress(startRow, endRow, c, c));
          }
          startRow = endRow;
        }
        startRow++;
      }
    }
  }
}
