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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ListUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * ReadTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 1:43 PM
 */
@DisplayName("EasyExcel 读测试")
class ReadTest {

  private static final Logger logger = LoggerFactory.getLogger(ReadTest.class);

  @DisplayName("最简单的读测试")
  static class SimpleReadTest {
    @Test
    @DisplayName("第一种写法，不用额外写一个DemoDataListener")
    void simple_read_test_1() throws IOException {

      File file = new ClassPathResource("example.xlsx").getFile();

      EasyExcel.read(
              file,
              DemoData.class,
              new PageReadListener<DemoData>(
                  dataList -> {
                    for (DemoData demoData : dataList) {
                      System.out.println("读取到一条数据：" + demoData.toString());
                      assertNotNull(demoData);
                    }
                  }))
          .sheet()
          .doRead();
    }

    @Test
    @DisplayName("第二种写法，匿名内部类")
    void simple_read_test_2() throws IOException {

      File file = new ClassPathResource("example.xlsx").getFile();

      EasyExcel.read(
              file,
              DemoData.class,
              new ReadListener<DemoData>() {
                /** 单次缓存的数据量 */
                public static final int BATCH_COUNT = 100;
                /** 临时存储 */
                private List<DemoData> cachedDataList =
                    ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override
                public void invoke(DemoData data, AnalysisContext context) {
                  cachedDataList.add(data);
                  if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                  }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                  saveData();
                }

                /** 加上存储数据库 */
                private void saveData() {
                  logger.info("{}条数据，开始存储数据库！", cachedDataList.size());
                  logger.info("存储数据库成功！");
                }
              })
          .sheet()
          .doRead();
    }

    @Test
    @DisplayName("第三种写法，类")
    void simple_read_test_3() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    @Test
    @DisplayName("第四种写法，ExcelReader")
    void simple_read_test_4() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();

      ExcelReader excelReader = null;
      try {
        excelReader = EasyExcel.read(file, DemoData.class, new DemoDataListener()).build();
        // 构建一个sheet 这里可以指定名字或者no
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        // 读取一个sheet
        excelReader.read(readSheet);
      } finally {
        if (excelReader != null) {
          // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
          excelReader.finish();
        }
      }
    }
  }

  @DisplayName("指定列的下标或者列名")
  static class IndexOrNameTest {
    @Test
    @DisplayName("test_index_or_name")
    void test_index_or_name() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, IndexOrNameData.class, new IndexOrNameDataListener()).sheet().doRead();
    }
  }

  @DisplayName("读多个sheet")
  static class RepeatedRead {

    @Test
    @DisplayName("读取所有的 sheet")
    void test_read_all_sheet() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, DemoData.class, new DemoDataListener()).doReadAll();
    }

    @Test
    @DisplayName("读取部分sheet")
    void test_read_part_sheet() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();

      ExcelReader excelReader = null;
      try {
        excelReader = EasyExcel.read(file).build();

        ReadSheet readSheet0 =
            EasyExcel.readSheet(0)
                .head(DemoData.class)
                .registerReadListener(new DemoDataListener())
                .build();

        ReadSheet readSheet1 =
            EasyExcel.readSheet(1)
                .head(DemoData.class)
                .registerReadListener(new DemoDataListener())
                .build();

        excelReader.read(readSheet0, readSheet1);
      } finally {
        if (excelReader != null) {
          excelReader.finish();
        }
      }
    }
  }

  @DisplayName("日期、数字与自定义格式转换")
  static class ConverterRead {

    @Test
    @DisplayName("格式转换")
    void test() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, ConverterData.class, new ConverterDataListener()).doReadAll();
    }
  }

  @DisplayName("多行头")
  static class ComplexHeaderTest {
    @Test
    @DisplayName("测试")
    void test_complex_header() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();

      EasyExcel.read(file, DemoData.class, new DemoDataListener())
          .sheet()
          // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
          .headRowNumber(1)
          .doRead();
    }
  }

  @DisplayName("同步的返回")
  static class SynchronousRead {

    /** 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面 */
    @Test
    @DisplayName("同步读")
    void test_synchronous_read() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      List<DemoData> list = EasyExcel.read(file).head(DemoData.class).sheet().doReadSync();
      for (DemoData demoData : list) {
        logger.info("读取到的数据{}", demoData.toString());
      }

      List<Map<Integer, String>> listMap = EasyExcel.read(file).sheet().doReadSync();
      for (Map<Integer, String> data : listMap) {
        logger.info("读取到数据：{}", data.toString());
      }
    }
  }

  @DisplayName("读取表头数据")
  static class InvokeHeader {

    @Test
    @DisplayName("读取表头")
    void test_header() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, DemoData.class, new DemoHeadDataListener()).sheet().doRead();
    }
  }

  @DisplayName("额外信息（批注、超链接、合并单元格信息读取）")
  static class ExtraRead {

    @Test
    @DisplayName("test_extra_read")
    void test_extra_read() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();

      EasyExcel.read(file, DemoExtraData.class, new DemoExtraListener())
          .extraRead(CellExtraTypeEnum.COMMENT)
          .extraRead(CellExtraTypeEnum.HYPERLINK)
          .extraRead(CellExtraTypeEnum.MERGE)
          // 第三个 sheet有批注
          .sheet(2)
          .doRead();
    }
  }

  @DisplayName("数据转换等异常情况")
  static class ExceptionHandler {
    @Test
    @DisplayName("test_exception_handler")
    void test_exception_handler() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, ExceptionDemoData.class, new DemoExceptionListener()).sheet().doRead();
    }
  }

  @DisplayName("不创建对象的读")
  static class NoModelTest {
    @Test
    @DisplayName("test_no_model")
    void test_no_model() throws IOException {
      File file = new ClassPathResource("example.xlsx").getFile();
      EasyExcel.read(file, new NoModelDataListener()).sheet().doRead();
    }
  }
}
