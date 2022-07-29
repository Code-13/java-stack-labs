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
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Item01DocumentCreationTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/28 15:35
 */
class Item01DocumentCreationTest {

  @Test
  @DisplayName("test_creation_and_save_to_disk")
  void test_creation_and_save_to_disk() throws IOException {

    try (PDDocument document = new PDDocument(); ) {
      document.save("src/test/resources/01/test_creation_and_save_to_disk.pdf");
    }

    // 由于这是一个空文档，如果您尝试打开此文档，则会提示您显示错误消息
    assertTrue(new File("src/test/resources/01/test_creation_and_save_to_disk.pdf").exists());
  }

  @Test
  @DisplayName("test_add_page_to_pdf_doc")
  void test_add_page_to_pdf_doc() throws IOException {
    try (PDDocument document = new PDDocument(); ) {
      for (int i = 0; i < 10; i++) {
        PDPage page = new PDPage();
        document.addPage(page);
      }

      document.save("src/test/resources/01/test_add_page_to_pdf_doc.pdf");
    }

    assertTrue(new File("src/test/resources/01/test_add_page_to_pdf_doc.pdf").exists());
  }

  @Test
  @DisplayName("test_load_exist_pdf_and_save_to_another_pdf")
  void test_load_exist_pdf_and_save_to_another_pdf() throws IOException {
    File file = new File("src/test/resources/01/example_01.pdf");
    try (PDDocument document = PDDocument.load(file); ) {
      document.addPage(new PDPage());
      document.save("src/test/resources/01/test_load_exist_pdf_and_save_to_another_pdf.pdf");
    }

    assertTrue(
        new File("src/test/resources/01/test_load_exist_pdf_and_save_to_another_pdf.pdf").exists());
  }
}
