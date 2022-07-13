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

package io.github.code13.javastack.libs.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.ImageData.ImageType;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * WriteTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/15/2022 9:25 AM
 */
@DisplayName("EasyExcel 写测试")
class WriteTest {

  private static final Logger logger = LoggerFactory.getLogger(WriteTest.class);

  /** 生成数据 */
  private static List<DemoData> demoData() {
    List<DemoData> list = ListUtils.newArrayList();
    for (int i = 0; i < 10; i++) {
      DemoData data = new DemoData();
      data.setString("字符串" + i);
      data.setDate(LocalDateTime.now());
      data.setDoubleData(0.56);
      list.add(data);
    }
    return list;
  }

  private static String buildFile(String type) {
    String fileName =
        Objects.requireNonNull(WriteTest.class.getResource("/")).getPath()
            + type
            + System.currentTimeMillis()
            + ".xlsx";
    logger.info("文件地址： {}", fileName);
    return fileName;
  }

  private static String buildImgFile() {
    String fileName =
        Objects.requireNonNull(WriteTest.class.getResource("/")).getFile()
            + "converter"
            + File.separator
            + "img.jpg";
    logger.info("图片地址： {}", fileName);
    return fileName;
  }

  @DisplayName("简单读测试")
  static class SimpleWriteRunner {

    @Test
    @DisplayName("写法一")
    void test_simple_write_1() {
      String fileName = buildFile("simpleWrite");

      EasyExcel.write(fileName, DemoData.class)
          .sheet("模板")
          // 此处传入查询数据的函数
          .doWrite(WriteTest::demoData);
    }

    @Test
    @DisplayName("写法2")
    void test_simple_write_2() {
      String fileName = buildFile("simpleWrite");

      EasyExcel.write(fileName, DemoData.class)
          .sheet("模板")
          // 此处直接获取数据
          .doWrite(demoData());
    }

    @Test
    @DisplayName("写法3")
    void test_simple_write() {
      String fileName = buildFile("simpleWrite");

      ExcelWriter excelWriter = null;
      try {
        excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        excelWriter.write(WriteTest::demoData, writeSheet);
      } finally {
        if (excelWriter != null) {
          excelWriter.finish();
        }
      }
    }
  }

  @Test
  @DisplayName("根据参数只导出指定列")
  void excludeOrIncludeWrite() {
    String fileName = buildFile("excludeOrIncludeWrite");

    Set<String> includeColumnFiledNames = new HashSet<>();
    // 根据用户传入字段 假设我们只要导出 date
    includeColumnFiledNames.add("date");

    EasyExcel.write(fileName, DemoData.class)
        .includeColumnFiledNames(includeColumnFiledNames)
        .sheet("模板")
        .doWrite(WriteTest::demoData);
  }

  @Test
  @DisplayName("指定写入的列")
  void indexWrite() {
    String fileName = buildFile("indexWrite");

    EasyExcel.write(fileName, IndexData.class).sheet("模板").doWrite(WriteTest::demoData);
  }

  @Test
  @DisplayName("复杂头写入")
  void complexHeadWrite() {
    String fileName = buildFile("complexHeadWrite");
    EasyExcel.write(fileName, ComplexHeadData.class).sheet("模板").doWrite(WriteTest::demoData);
  }

  @DisplayName("重复多次写入(写到单个或者多个Sheet)")
  static class RepeatedWrite {

    @Test
    @DisplayName("方法1 写到同一个 sheet")
    void test_repeated_write_1() {
      String fileName = buildFile("repeatedWrite");

      ExcelWriter excelWriter = null;
      try {
        excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        for (int i = 0; i < 5; i++) {
          excelWriter.write(WriteTest::demoData, writeSheet);
        }
      } finally {
        if (excelWriter != null) {
          excelWriter.finish();
        }
      }
    }

    @Test
    @DisplayName("方法2 写到不同的sheet，同一个对象")
    void test_repeated_write_2() {
      String fileName = buildFile("repeatedWrite");

      ExcelWriter excelWriter = null;
      try {
        excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        for (int i = 0; i < 5; i++) {
          WriteSheet writeSheet = EasyExcel.writerSheet("模板" + i).build();
          excelWriter.write(WriteTest::demoData, writeSheet);
        }
      } finally {
        if (excelWriter != null) {
          excelWriter.finish();
        }
      }
    }

    @Test
    @DisplayName("方法3 如果写到不同的sheet 不同的对象")
    void test_repeated_write_3() {
      String fileName = buildFile("repeatedWrite");

      ExcelWriter excelWriter = null;
      try {
        // 此处指定文件，不指定类型
        excelWriter = EasyExcel.write(fileName).build();
        for (int i = 0; i < 5; i++) {
          // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。
          // 这里注意DemoData.class 可以每次都变，这里为了方便所以用的同一个class 实际上可以一直变
          WriteSheet writeSheet = EasyExcel.writerSheet("模板" + i).head(DemoData.class).build();
          excelWriter.write(WriteTest::demoData, writeSheet);
        }
      } finally {
        if (excelWriter != null) {
          excelWriter.finish();
        }
      }
    }
  }

  @Test
  @DisplayName("日期、数字或者自定义格式转换")
  void converterWrite() {
    String fileName = buildFile("converterWrite");
    EasyExcel.write(fileName, ConverterData.class).sheet("模板").doWrite(WriteTest::demoData);
  }

  @Test
  @DisplayName("图片导出")
  void imageWrite() throws IOException {
    String fileName = buildFile("imageWrite");
    String imagePath = new ClassPathResource("converter/img.jpg").getFile().getAbsolutePath();

    try (FileInputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
      List<ImageDemoData> list = ListUtils.newArrayList();
      ImageDemoData imageDemoData = new ImageDemoData();
      list.add(imageDemoData);

      // 放入五种类型的图片 实际使用只要选一种即可
      imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
      imageDemoData.setFile(new File(imagePath));
      imageDemoData.setString(imagePath);
      imageDemoData.setInputStream(inputStream);
      //      imageDemoData.setUrl(
      //          new URL(
      //
      // "https://raw.githubusercontent.com/alibaba/easyexcel/master/src/test/resources/converter/img.jpg"));

      // 这里演示
      // 需要额外放入文字
      // 而且需要放入2个图片
      // 第一个图片靠左
      // 第二个靠右 而且要额外的占用他后面的单元格
      WriteCellData<Void> writeCellData = new WriteCellData<>();
      imageDemoData.setWriteCellDataFile(writeCellData);
      // 这里可以设置为 EMPTY 则代表不需要其他数据了
      writeCellData.setType(CellDataTypeEnum.STRING);
      writeCellData.setStringValue("额外的放一些文字");

      // 可以放入多个图片
      List<ImageData> imageDataList = new ArrayList<>();
      ImageData imageData = new ImageData();
      imageDataList.add(imageData);
      writeCellData.setImageDataList(imageDataList);
      // 放入2进制图片
      imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
      // 图片类型
      imageData.setImageType(ImageType.PICTURE_TYPE_PNG);
      // 上 右 下 左 需要留空
      // 这个类似于 css 的 margin
      // 这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
      imageData.setTop(5);
      imageData.setRight(40);
      imageData.setBottom(5);
      imageData.setLeft(5);

      // 放入第二个图片
      imageData = new ImageData();
      imageDataList.add(imageData);
      writeCellData.setImageDataList(imageDataList);
      imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
      imageData.setImageType(ImageType.PICTURE_TYPE_PNG);
      imageData.setTop(5);
      imageData.setRight(5);
      imageData.setBottom(5);
      imageData.setLeft(50);
      // 设置图片的位置 假设 现在目标 是 覆盖 当前单元格 和当前单元格右边的单元格
      // 起点相对于当前单元格为0 当然可以不写
      imageData.setRelativeFirstRowIndex(0);
      imageData.setRelativeFirstColumnIndex(0);
      imageData.setRelativeLastRowIndex(0);
      // 前面3个可以不写  下面这个需要写 也就是 结尾 需要相对当前单元格 往右移动一格
      // 也就是说 这个图片会覆盖当前单元格和 后面的那一格
      imageData.setRelativeLastColumnIndex(1);

      EasyExcel.write(fileName, ImageDemoData.class).sheet().doWrite(list);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
