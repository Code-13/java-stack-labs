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

package io.github.code13.javastack.javalabs.jnaf.jdk13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TextBlockRunner.
 *
 * <p>文本块.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 22:25
 */
public class TextBlockRunner {

  @Test
  @DisplayName("原始字符串")
  void test() {

    String json =
        "{\n"
            + "    \"id\": \"1\",\n"
            + "    \"ip\": \"192.168.1.1\",\n"
            + "    \"port\":\"1883\"\n"
            + "}\n";

    System.out.println(json);
  }

  @Test
  @DisplayName("Text Block for JSON")
  void testTextBlockJson() {

    //    String json =
    //        """
    //            {
    //                "id": "1",
    //                "ip": "192.168.1.1",
    //                "port":"1883"
    //            }
    //            """;
    //
    //    System.out.println(json);
  }

  @Test
  @DisplayName("关于 TextBlock 的基本使用")
  void test1() {
    //  以开始分隔符的行终止符后的第一个字符开始
    //  以结束分隔符的第一个双引号之前的最后一个字符结束

    //    String text1 = """
    //        abc""";
    //
    //    String text2 = """
    //        abc
    //        """;
    //
    //    assertEquals(text1.length(), 3);
    //    assertEquals(text2.length(), 4);
  }

  @Test
  @DisplayName("编译器在编译时会删除多余的空格")
  void test2() {}

  @Test
  @DisplayName("转义字符")
  void test3() {

    //    String json =
    //        """
    //            {
    //                "id": "1",\n
    //                "ip": "192.168.1.1",\n
    //                "port":"1883"\n
    //            }\n
    //            """;

  }

  @Test
  @DisplayName("可以使用双引号")
  void test4() {

    //    String json =
    //        """
    //            {
    //                "id": "1",\n
    //                "ip": "192.168.1.1",\n
    //                "port":"1883"\n
    //            }\n
    //            """;

  }

  @Test
  @DisplayName("可以使用三引号")
  void test5() {

    //    String json  = """
    //                String json =
    //                    \"""
    //                        {
    //                            "id": "1",
    //                            "ip": "192.168.1.1",
    //                            "port":"1883"
    //                        }
    //                        \""";
    //        """;
  }

  @Test
  @DisplayName("文本块")
  void test6() {

    String type = "";

    String code = """
        public static void run($type type) {
          system.out.printLn(type);
        }
        """.replace("$type", type);

    System.out.println(code);
  }
}
