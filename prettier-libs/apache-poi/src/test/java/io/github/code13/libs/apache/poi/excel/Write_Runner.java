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

package io.github.code13.libs.apache.poi.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Write_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/3 11:14
 */
public class Write_Runner {

  @Test
  @DisplayName("basic")
  void basic() {
    // Excel
    XSSFWorkbook workbook = new XSSFWorkbook();

    // Sheet
    Sheet sheet = workbook.createSheet("Persons");

    sheet.setColumnWidth(0, 6000);
    sheet.setColumnWidth(1, 4000);

    // 表头
    Row header = sheet.createRow(0);

    // 单元格样式
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    XSSFFont font = workbook.createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short) 16);
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("Name");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Age");
    headerCell.setCellStyle(headerStyle);

    for (int i = 1; i <= 5; i++) {
      sheet.createRow(i);
    }

    int lastRowNum = sheet.getLastRowNum();
    System.out.println(lastRowNum);
  }
}
