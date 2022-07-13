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

package io.github.code13.javastack.javalabs.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StringTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/8/10 16:25
 */
@DisplayName("java.lang.string")
class StringTest {

  @Test
  @DisplayName("字符串测试")
  void test1() {

    String a = "Programming";
    String b = new String("Programming");
    String c = "Program" + "ming";

    System.out.println(a == b);
    System.out.println(a == c);

    System.out.println(a.equals(b));
    System.out.println(a.equals(c));

    System.out.println(a.intern() == b.intern());
  }
}
