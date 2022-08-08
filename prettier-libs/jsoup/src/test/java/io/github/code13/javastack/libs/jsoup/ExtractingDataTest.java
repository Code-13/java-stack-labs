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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ExtractingDataTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/8/3 15:18
 */
class ExtractingDataTest {

  @Test
  @DisplayName("test_Use DOM methods to navigate a document")
  void test_Use_DOM_methods_to_navigate_a_document() throws IOException {

    File input = new File("src/test/resources/input.html");
    Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

    Element content = doc.getElementById("content");

    assertNotNull(content);

    Elements links = content.getElementsByTag("a");
    for (Element link : links) {
      String linkHref = link.attr("href");
      String linkText = link.text();

      System.out.println(linkHref);
      System.out.println(linkText);
    }
  }
}
