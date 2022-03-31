/*
 *
 *  *     Copyright $today.year-present the original author or authors.
 *  *
 *  *     Licensed under the Apache License, Version 2.0 (the "License");
 *  *     you may not use this file except in compliance with the License.
 *  *     You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *     Unless required by applicable law or agreed to in writing, software
 *  *     distributed under the License is distributed on an "AS IS" BASIS,
 *  *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *     See the License for the specific language governing permissions and
 *  *     limitations under the License.
 *
 */

package io.github.code13.javastack.libs.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * ItextPdfRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/17 2:49 PM
 */
class ItextPdfRunner {

  /** 生成pdf文件 */
  @Test
  void testCreatePdf() {
    try {
      // 1. new Document
      Document document = new Document();
      PdfWriter.getInstance(
          document, new FileOutputStream(new ClassPathResource("1.pdf").getFile()));
      // 2. 打开document
      document.open();
      // 3. 添加内容
      document.add(new Paragraph("hello world！"));
      // 4. 关闭 (如果未关闭则会生成无效的pdf文件)
      document.close();
    } catch (DocumentException | FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}