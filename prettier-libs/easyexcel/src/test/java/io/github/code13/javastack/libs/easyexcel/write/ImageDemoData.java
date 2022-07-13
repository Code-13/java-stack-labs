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

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.converters.string.StringImageConverter;
import com.alibaba.excel.metadata.data.WriteCellData;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import lombok.Data;

/**
 * ImageDemoData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/15/2022 10:31 AM
 */
@Data
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
public class ImageDemoData {

  private File file;

  private InputStream inputStream;

  /** 如果string类型 必须指定转换器，string默认转换成string */
  @ExcelProperty(converter = StringImageConverter.class)
  private String string;

  private byte[] byteArray;

  /**
   * 根据url导出
   *
   * @since 2.1.1
   */
  private URL url;

  /**
   * 根据文件导出 并设置导出的位置。
   *
   * @since 3.0.0-beta1
   */
  private WriteCellData<Void> writeCellDataFile;
}
