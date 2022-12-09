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

package io.github.code13.libs.pdfbox;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Item02DocumentAddTextTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/28 16:20
 */
class Item02DocumentAddTextTest {

  /**
   * 向现有 PDF 文档添加文本 您可以使用 PDFBox 库将内容添加到文档中， 它为您提供了一个名为 PDPageContentStream 的类，其中包含在 PDFDocument
   * 的页面中插入文本、图像和其他类型的内容所需的方法。
   *
   * @throws IOException .
   */
  @Test
  @DisplayName("test_add_text")
  void test_add_text() throws IOException {
    // 第 1 步：加载现有文档
    // 您可以使用PDDocument 类的load()方法加载现有文档。因此，实例化该类并加载所需的文档，如下所示。
    File file = new File("src/test/resources/02/test_add_text.pdf");
    try (PDDocument document = PDDocument.load(file); ) {
      // 第 2 步：获取所需页面
      // 您可以使用getPage()方法在文档中获取所需的页面。通过将其索引传递给此方法来检索所需页面的对象，如下所示。
      PDPage page = document.getPage(1);

      // 第 3 步：准备内容流
      // 您可以使用PDPageContentStream类的对象插入各种数据元素。您需要将文档对象和页面对象传递给此类的构造函数，因此，通过传递前面步骤中创建的这两个对象来实例化此类，如下所示。

      try (PDPageContentStream contentStream = new PDPageContentStream(document, page); ) {
        // 第 4 步：开始文本
        // 在 PDF 文档中插入文本时，您可以使用 PDPageContentStream 类的 beginText() 和 endText() 方法指定文本的起点和终点，如下所示。

        contentStream.beginText();

        // 第 5 步：设置文本的位置
        // 使用newLineAtOffset()方法，您可以在页面中的内容流上设置位置。

        contentStream.newLineAtOffset(25, 500);

        // 第 6 步：设置字体
        // 您可以使用PDPageContentStream类的setFont()方法将文本的字体设置为所需的样式，如下所示。对于此方法，您需要传递字体的类型和大小。

        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

        // 第 7 步：插入文本
        // 您可以使用PDPageContentStream类的ShowText()方法将文本插入页面，如下所示。此方法接受字符串形式的所需文本。

        contentStream.showText("This is the sample document and we are adding content to it.");

        // 第 8 步：结束文本
        // 插入文本后，您需要使用PDPageContentStream类的endText()方法结束文本，如下所示。

        contentStream.endText();

        // 第 9 步：关闭 PDPageContentStream
        // 使用close()方法关闭PDPageContentStream对象。
      }

      // 第 10 步：保存文档
      // 添加所需内容后，使用PDDocument类的save()方法保存 PDF 文档，如以下代码块所示。

      document.save("src/test/resources/02/test_add_text_added.pdf");

      // 第 11 步：关闭文档
      // 最后，使用PDDocument类的close()方法关闭文档。
    }

    assertTrue(new File("src/test/resources/02/test_add_text_added.pdf").exists());
  }

  @Test
  @DisplayName("test_add_multiline_text")
  void test_add_multiline_text() throws IOException {
    // 第 1 步：加载现有文档
    // 您可以使用PDDocument 类的load()方法加载现有文档。因此，实例化该类并加载所需的文档，如下所示。
    File file = new File("src/test/resources/02/test_add_text.pdf");
    try (PDDocument document = PDDocument.load(file); ) {
      // 第 2 步：获取所需页面
      // 您可以使用getPage()方法在文档中获取所需的页面。通过将其索引传递给此方法来检索所需页面的对象，如下所示。
      PDPage page = document.getPage(1);

      // 第 3 步：准备内容流
      // 您可以使用PDPageContentStream类的对象插入各种数据元素。您需要将文档对象和页面对象传递给此类的构造函数，因此，通过传递前面步骤中创建的这两个对象来实例化此类，如下所示。

      try (PDPageContentStream contentStream = new PDPageContentStream(document, page); ) {
        // 第 4 步：开始文本
        // 在 PDF 文档中插入文本时，您可以使用 PDPageContentStream 类的 beginText() 和 endText() 方法指定文本的起点和终点，如下所示。

        contentStream.beginText();

        // 第 5 步：设置文本的位置
        // 使用newLineAtOffset()方法，您可以在页面中的内容流上设置位置。

        contentStream.newLineAtOffset(25, 500);

        // 第 6 步：设置字体
        // 您可以使用PDPageContentStream类的setFont()方法将文本的字体设置为所需的样式，如下所示。对于此方法，您需要传递字体的类型和大小。

        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

        // 第 7 步：设置文本前导
        // 您可以使用setLeading()方法设置文本前导，如下所示。

        contentStream.setLeading(14.5f);

        // 第 8 步：使用 newline() 插入多个字符串
        // 您可以使用PDPageContentStream类的ShowText()方法插入多个字符串，方法是使用newline()方法将每个字符串分开，如下所示。

        String text1 =
            "This is an example of adding 02 to a page in the pdf document. we can add as many lines";
        String text2 =
            "as we want like this using the ShowText()  method of the ContentStream class";

        contentStream.showText(text1);
        contentStream.newLine();
        contentStream.showText(text2);

        // 第 9 步：结束文本
        // 插入文本后，您需要使用PDPageContentStream类的endText()方法结束文本，如下所示。

        contentStream.endText();

        // 第 10 步：关闭 PDPageContentStream
        // 使用close()方法关闭PDPageContentStream对象。
      }

      // 第 11 步：保存文档
      // 添加所需内容后，使用PDDocument类的save()方法保存 PDF 文档，如以下代码块所示。

      document.save("src/test/resources/02/test_add_multiline_text_added.pdf");

      // 第 12 步：关闭文档
      // 最后，使用PDDocument类的close()方法关闭文档。
    }

    assertTrue(new File("src/test/resources/02/test_add_multiline_text_added.pdf").exists());
  }
}
