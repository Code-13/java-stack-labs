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

package io.github.code13.javastack.libs.jsoup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * InputTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/29 15:12
 */
class InputTest {

  @Test
  @DisplayName("Parse a document from a String")
  void test_a_document_from_a_string() {

    /*
     * Problem You have HTML in a Java String, and you want to parse that HTML to get at its
     * contents, or to make sure it's well formed, or to modify it. The String may have come from
     * user input, a file, or from the web.
     */

    String html =
        "<html><head><title>First parse</title></head>"
            + "<body><p>Parsed HTML into a doc.</p></body></html>";
    Document doc = Jsoup.parse(html);

    String title = doc.title();

    assertEquals("First parse", title);
  }

  @Test
  @DisplayName("test_parsing_a_body_fragment")
  void test_parsing_a_body_fragment() {
    String html = "<div><p>Lorem ipsum.</p>";
    Document doc = Jsoup.parseBodyFragment(html);
    Element body = doc.body();

    String data = body.toString();

    assertEquals(
        "<body>\n" + " <div>\n" + "  <p>Lorem ipsum.</p>\n" + " </div>\n" + "</body>", data);
  }

  @Test
  @DisplayName("test_load_a_document_from_a_url")
  void test_load_a_document_from_a_url() throws IOException {
    Document doc = Jsoup.connect("https://www.baidu.com").get();

    String title = doc.title();

    assertEquals("百度一下，你就知道",title);
  }

  @Test
  @DisplayName("test_load_a_document_from_file")
  void test_load_a_document_from_file() throws IOException {
    File file = new File("src/test/resources/input.html");

    Document doc = Jsoup.parse(file, "UTF-8", "https://www.baidu.com");

    assertEquals("Input",doc.title());
  }
}
