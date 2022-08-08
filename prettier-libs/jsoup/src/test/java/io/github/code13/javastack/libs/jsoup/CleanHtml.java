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
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CleanHtml.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/8/8 09:42
 */
class CleanHtml {

  @Test
  @DisplayName("SanitizeUntrustedHTML")
  void sanitizeUntrustedHTML() {

    String unsafe = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";

    String safe = Jsoup.clean(unsafe, Safelist.basic());

    // <p><a href="http://example.com/" rel="nofollow">Link</a></p>

    System.out.println(safe);

    assertEquals("<p><a href=\"http://example.com/\" rel=\"nofollow\">Link</a></p>", safe);
  }
}
