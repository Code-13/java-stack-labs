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

package io.github.code13.javastack.libs.vavr.values;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import io.vavr.control.Option;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * vavr Option
 *
 * <p>https://docs.vavr.io/#_option
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 15:48
 */
@DisplayName("Option")
class OptionTest {

  @Test
  @DisplayName("java.util.Optional")
  void test1() {
    Optional<String> maybeFoo = Optional.of("foo");
    assertEquals(maybeFoo.get(), "foo");

    Optional<String> maybeFoobar =
        maybeFoo.map(s -> (String) null).map(s -> s.toUpperCase() + "bar");

    assertFalse(maybeFoobar.isPresent());
  }

  @Test
  @DisplayName("Option")
  void test2() {

    // Using Vavr’s Option, the same scenario will result in a NullPointerException.

    assertThrows(
        NullPointerException.class,
        () -> {
          Option<String> maybeFoo = Option.of("foo");
          maybeFoo.map(s -> (String) null).map(s -> s.toUpperCase() + "bar");
        });
  }
}
