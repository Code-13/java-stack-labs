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

package io.github.code13.javastack.libs.pdfbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Item03ReadTextFromPdfTest.
 *
 * <p>提取文本是 PDF 框库的主要功能之一。您可以使用PDFTextStripper类的getText()方法提取文本。此类从给定的 PDF 文档中提取所有文本。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/28 16:51
 */
class Item03ReadTextFromPdfTest {

  static String TEXT =
      """
          This is an example of adding text to a page in the pdf document. we can add as many lines
          as we want like this using the ShowText()  method of the ContentStream class
          """;

  @Test
  @DisplayName("test_read_text_from_pdf")
  void test_read_text_from_pdf() throws IOException {
    // 第 1 步：加载现有 PDF 文档
    // 使用PDDocument类的静态方法load()加载现有的 PDF 文档。此方法接受文件对象作为参数，因为这是一个静态方法，您可以使用类名调用它，如下所示。
    File file = new File("src/test/resources/03/new.pdf");
    try (PDDocument document = PDDocument.load(file); ) {

      // 第 2 步：实例化 PDFTextStripper 类
      // PDFTextStripper类提供了从 PDF 文档中检索文本的方法，因此，如下所示实例化此类

      PDFTextStripper pdfTextStripper = new PDFTextStripper();

      // 第 3 步：检索文本
      // 您可以使用PDFTextStripper类的getText()方法从 PDF 文档中读取/检索页面的内容。对于此方法，您需要将文档对象作为参数传递。此方法检索给定文档中的文本并以
      // String 对象的形式返回

      String text = pdfTextStripper.getText(document);

      System.out.println(text);

      assertEquals(TEXT, text);

      // 第 4 步：关闭文档
      // 最后，使用 PDDocument 类的close()方法关闭文档，
    }
  }
}
