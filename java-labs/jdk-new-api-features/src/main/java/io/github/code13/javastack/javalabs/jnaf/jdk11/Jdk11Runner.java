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

package io.github.code13.javastack.javalabs.jnaf.jdk11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Jdk11Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 17:28
 */
@DisplayName("jdk11 新特性")
class Jdk11Runner {

  @DisplayName("字符串增强")
  static class StringEnhance {

    @Test
    @DisplayName("用来判断字符串是不是空字符\"\"或者trim()之后(\" \")为空字符")
    void isBlank() {
      String str = "   ";
      System.out.println(str.isBlank());
    }

    @Test
    @DisplayName("将一个字符串按照行终止符（换行符\\n或者回车符\\r）进行分割，并将分割为Stream流")
    void lines() {
      String newStr = "Hello Java 11 \n felord.cn \r 2021-09-28";
      newStr.lines().forEach(System.out::println);
    }

    @Test
    @DisplayName("去除字符串前后的“全角和半角”空白字符")
    void strip() {
      String str = "HELLO\u3000";
      // str = 6
      System.out.println("str = " + str.length());
      // trim = 6
      System.out.println("trim = " + str.trim().length());
      // strip = 5
      System.out.println("strip = " + str.strip().length());
    }

    @Test
    @DisplayName("按照给定的次数重复串联字符串的内容")
    void repeat() {
      String str = "HELLO";
      // 空字符
      String empty = str.repeat(0);
      System.out.println(empty);
      // HELLO
      String repeatOne = str.repeat(1);
      System.out.println(repeatOne);
      // HELLOHELLO
      String repeatTwo = str.repeat(2);
      System.out.println(repeatTwo);
    }
  }

  @Test
  @DisplayName("toArray")
  void toArray() {
    List<String> sampleList = Arrays.asList("felord.cn", "java 11");
    // array = {"felord.cn", "java 11"};
    String[] array = sampleList.toArray(String[]::new);
  }

  @Test
  @DisplayName("PredicateNot")
  void predicateNot() {

    List<String> sampleList = Arrays.asList("felord.cn", "java 11", "jack");
    // [jack]
    List<String> result =
        sampleList.stream()
            // 过滤以j开头的字符串
            .filter(s -> s.startsWith("j"))
            // 同时不包含11的字符串
            .filter(Predicate.not(s -> s.contains("11")))
            .collect(Collectors.toList());

    System.out.println(result);
  }

  @Test
  @DisplayName("Files")
  void files() throws IOException {
    String dir = "D://download";
    // 写入文件
    Path path =
        Files.writeString(Files.createTempFile(Path.of(dir), "hello", ".txt"), "hello java 11");
    // 读取文件
    String fileContent = Files.readString(path);

    System.out.println(fileContent);
  }
}
