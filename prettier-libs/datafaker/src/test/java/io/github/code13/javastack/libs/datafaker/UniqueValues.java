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

package io.github.code13.javastack.libs.datafaker;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * UniqueValues.
 *
 * @see https://www.datafaker.net/documentation/unique-values/
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/5 13:39
 */
class UniqueValues {

  /**
   * Unique values can be retrieved from the YAML files by key, if the key references a hard-coded
   * list of values.
   */
  @Test
  @DisplayName("test1")
  void test1() {
    Faker faker = new Faker();

    // The values returned in the following lines will never be the same.
    String firstUniqueInstrument = faker.unique().fetchFromYaml("music.instruments"); // "Flute"
    String secondUniqueInstrument = faker.unique().fetchFromYaml("music.instruments"); // "Clarinet"

    System.out.println(firstUniqueInstrument);
    System.out.println(secondUniqueInstrument);
  }

  /** If all possible values have been returned, an exception will be thrown. */
  @Test
  @DisplayName("test2")
  void test2() {
    Faker faker = new Faker();
    String firstUniqueInstrument = faker.unique().fetchFromYaml("music.instruments"); // "Ukelele"

    String nthUniqueInstrument =
        faker.unique().fetchFromYaml("music.instruments"); // throws NoSuchElementException
  }

  /** Any non-string values will be converted. */
  @Test
  @DisplayName("test3")
  void test3() {
    Faker faker = new Faker();
    String successCode = faker.unique().fetchFromYaml("sip.response.codes.success"); // "200"
  }
}
