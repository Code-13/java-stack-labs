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

package io.github.code13.javastack.libs.zxing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 文件工具类.
 *
 * <p>提供 {@link File} 与 {@link MultipartFile} 的转换 提供 {@link InputStream} 与 {@link MultipartFile} 的转换
 *
 * @author <a href="https://github.com/Code-13/">Code13</a>
 * @date 2020-09-15 14:17
 */
final class FileUtils {

  /**
   * 创建临时文件.
   *
   * @param inputStream 输入文件流
   * @param name 文件名
   * @param ext 扩展名
   * @param tmpDirFile 临时文件夹目录
   * @return 返回文件
   * @throws IOException 异常
   */
  static File createTmpFile(InputStream inputStream, String name, String ext, File tmpDirFile)
      throws IOException {
    File resultFile = File.createTempFile(name, '.' + ext, tmpDirFile);

    resultFile.deleteOnExit();
    org.apache.commons.io.FileUtils.copyToFile(inputStream, resultFile);
    return resultFile;
  }

  /**
   * 创建临时文件.
   *
   * @param inputStream 输入文件流
   * @param name 文件名
   * @param ext 扩展名
   * @return 返回文件
   * @throws IOException 异常
   */
  static File createTmpFile(InputStream inputStream, String name, String ext) throws IOException {
    return createTmpFile(
        inputStream,
        name,
        ext,
        Files.createTempDirectory("tjyun-biz-marketing-qrcode-temp").toFile());
  }

  /**
   * 创建临时文件.
   *
   * @param inputStream 输入文件流
   * @param ext 扩展名
   * @return 返回文件
   * @throws IOException 异常
   */
  static File createTmpFile(InputStream inputStream, String ext) throws IOException {
    return createTmpFile(
        inputStream,
        UUID.randomUUID().toString().replace("-", ""),
        ext,
        Files.createTempDirectory("tjyun-biz-marketing-qrcode-temp").toFile());
  }

  /**
   * inputStream转为 MultipartFile.
   *
   * @param inputStream InputStream
   * @param ext 文件拓展名 如 png/gif
   * @return {@link MultipartFile}
   * @throws IOException 异常
   */
  static MultipartFile inputStreamToMultipartFile(InputStream inputStream, String ext)
      throws IOException {
    File tmpFile = createTmpFile(inputStream, UUID.randomUUID().toString().replace("-", ""), ext);
    return fileToMultipartFile(tmpFile);
  }

  /**
   * file 转为 MultipartFile.
   *
   * @param file File
   * @return {@link MultipartFile}
   */
  static MultipartFile fileToMultipartFile(File file) {
    DiskFileItem fileItem =
        (DiskFileItem)
            new DiskFileItemFactory()
                .createItem("file", MediaType.IMAGE_PNG_VALUE, true, file.getName());

    try (InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream()) {
      IOUtils.copy(input, os);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid file: " + e, e);
    }

    return new CommonsMultipartFile(fileItem);
  }
}
