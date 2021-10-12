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

package io.github.code13.javastack.javalabs.jnaf.jdk10;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Jdk10Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 17:09
 */
public class Jdk10Runner {

  @Test
  @DisplayName("copyOf")
  void collectionCopyOf() {

    List<String> strings = List.of("1", "2", "3");

    List<String> strings1 = List.copyOf(strings);

    System.out.println(strings);
    System.out.println(strings1);
  }

  @Test
  @DisplayName("Stream归纳为不可变集合")
  void test1() {
    Stream.of("1", "2", "3").collect(Collectors.toUnmodifiableList()).forEach(System.out::println);
  }

  @Test
  @DisplayName("Optional.orElseThrow()")
  void optionalOrElseThrow() {
    Optional.ofNullable(null).orElseThrow();
    Optional.ofNullable(null).orElseThrow(RuntimeException::new);
  }
}
