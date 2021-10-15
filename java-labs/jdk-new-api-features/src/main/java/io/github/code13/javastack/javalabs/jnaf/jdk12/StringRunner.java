/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.javalabs.jnaf.jdk12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StringRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 22:01
 */
@DisplayName("Jdk 12  String 的新特性")
class StringRunner {

  /** 类似于 map. */
  @Test
  @DisplayName("String.transform")
  void testTransform() {

    String transform =
        "hello"
            .transform(info -> info + ",world")
            .transform(String::strip)
            .transform(String::toUpperCase);

    System.out.println(transform);
  }

  @Test
  @DisplayName("indent")
  void testIndent() {

    String indent = "C\nC++\nJava\nPython\nGo\n".indent(3);
    System.out.println(indent);
  }
}
