/*
 *
 * Copyright $today.year-present the original author or authors.
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

package io.github.code13.javastack.libs.easyexcel.write.template;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * TemplateWriteTest.
 *
 * <p>https://my.oschina.net/u/815005/blog/4706446
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/31 2:47 PM
 */
@DisplayName("根据模板更新")
class TemplateWriteTest {

  private static final Logger logger = LoggerFactory.getLogger(TemplateWriteTest.class);

  static final ClassPathResource templateResource =
      new ClassPathResource("template/excel-template.xlsx");

  static List<IclExportDataDTO> mockData() {
    List<IclExportDataDTO> result = new ArrayList<>();

    int ix = ThreadLocalRandom.current().nextInt(5, 10);
    for (int i = 0; i < ix; i++) {
      IclExportDataDTO exportData = new IclExportDataDTO();

      result.add(exportData);

      exportData.setIclCode("ICL" + String.format("%0" + 7 + "d", i));
      exportData.setIclName("ICl测试数据" + i);
      exportData.setCategory("Document");
      exportData.setInfoOwners("Zhang San, Admin");
      exportData.setRecordCreator("Li Si, Admin");
      exportData.setStatus("In Progress");
      exportData.setSecurityClass("");
      exportData.setSubmitDate("03/18/2022 03:42 PM");
      ArrayList<IobExportDataDTO> iobExportDataList = new ArrayList<>();
      exportData.setIobExportDataList(iobExportDataList);

      int jx = ThreadLocalRandom.current().nextInt(1, 5);
      for (int j = 0; j < jx; j++) {
        IobExportDataDTO iobExportData = new IobExportDataDTO();
        iobExportData.setIobCode("IOB" + String.format("%0" + 7 + "d", j));
        iobExportData.setIobName(exportData.getIclCode());
        iobExportData.setClassificationMethod("General");
        iobExportData.setIobSecurityClass("HP");
        iobExportData.setIobConfidentiality("C");
        iobExportData.setIobIntegrity("I");
        iobExportData.setIobAvailability("A");
        iobExportDataList.add(iobExportData);
      }
    }

    return result;
  }

  private static String buildFile(String type) {
    String fileName =
        Objects.requireNonNull(TemplateWriteTest.class.getResource("/")).getPath()
            + type
            + System.currentTimeMillis()
            + ".xlsx";
    logger.info("文件地址： {}", fileName);
    return fileName;
  }

  @Test
  @DisplayName("testWriteWithTemplate")
  void testWriteWithTemplate() throws IOException {

    List<IclExportDataDTO> iclExportDataDTOS = mockData();

    List<IclExportData> iclExportDataList = map(iclExportDataDTOS);

    ExcelWriter excelWriter =
        EasyExcel.write(buildFile("template"), IclExportData.class)
            .registerWriteHandler(new MergeStrategy(iclExportDataDTOS))
            .withTemplate(templateResource.getFile())
            .build();

    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    FillConfig fillConfig = FillConfig.builder().forceNewRow(true).build();
    excelWriter.fill(iclExportDataList, fillConfig, writeSheet);
    excelWriter.finish();
  }

  private List<IclExportData> map(List<IclExportDataDTO> iclExportDataDTOS) {
    List<IclExportData> iclExportDataList = new ArrayList<>();
    for (IclExportDataDTO iclExportDataDTO : iclExportDataDTOS) {
      if (ObjectUtils.isEmpty(iclExportDataDTO.getIobExportDataList())) {
        IclExportData iclExportData = new IclExportData();
        BeanUtils.copyProperties(iclExportDataDTO, iclExportData);
        iclExportDataList.add(iclExportData);
      } else {
        for (IobExportDataDTO iobExportDataDTO : iclExportDataDTO.getIobExportDataList()) {
          IclExportData iclExportData = new IclExportData();
          BeanUtils.copyProperties(iclExportDataDTO, iclExportData);
          BeanUtils.copyProperties(iobExportDataDTO, iclExportData);
          iclExportDataList.add(iclExportData);
        }
      }
    }
    return iclExportDataList;
  }
}
