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

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Item04RenderToImgTest.
 *
 * <p>从 PDF 文档生成图像
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/28 17:19
 */
class Item04RenderToImgTest {

  @Test
  @DisplayName("test_add_img_to_pdf")
  void test_add_img_to_pdf() throws IOException {

    // 第 1 步：加载现有 PDF 文档
    // 使用PDDocument类的静态方法load()加载现有的 PDF 文档。此方法接受文件对象作为参数，因为这是一个静态方法，您可以使用类名调用它，如下所示。
    try (PDDocument document = PDDocument.load(new File("src/test/resources/04/original.pdf"))) {
      // 第 2 步：实例化 PDFRenderer 类
      // 名为PDFRenderer的类将 PDF 文档呈现为AWT
      // BufferedImage。因此，您需要实例化此类，如下所示。此类的构造函数接受一个文档对象；传递上一步中创建的文档对象，如下所示。

      PDFRenderer pdfRenderer = new PDFRenderer(document);

      // 第 3 步：从 PDF 文档渲染图像
      // 您可以使用 Renderer 类的方法renderImage()在特定页面中渲染图像，您需要将要渲染图像的页面的索引传递给此方法。
      BufferedImage image = pdfRenderer.renderImageWithDPI(1, 100, ImageType.ARGB);

      // 第 4 步：将图像写入文件
      // 您可以使用write()方法将上一步中渲染的图像写入文件。对于此方法，您需要传递三个参数 -
      //
      // 渲染的图像对象。
      // 表示图像类型（jpg 或 png）的字符串。
      // 您需要将提取的图像保存到的文件对象。
      ImageIO.write(image, "JPEG", new File("src/test/resources/04/image.jpg"));
    }

    assertTrue(new File("src/test/resources/04/image.jpg").exists());
  }
}
