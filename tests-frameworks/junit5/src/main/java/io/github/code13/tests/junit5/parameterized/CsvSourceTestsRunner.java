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

package io.github.code13.tests.junit5.parameterized;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * {@link CsvSource} allows you to express argument lists as comma-separated values (i.e., CSV
 * String literals). Each string provided via the value attribute in @CsvSource represents a CSV
 * line and results in one invocation of the parameterized test.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 3:42 PM
 */
class CsvSourceTestsRunner {

  @ParameterizedTest
  @CsvSource({
    "apple,         1",
    "banana,        2",
    "'lemon, lime', 0xF1",
    "strawberry,    700000"
  })
  void testWithCsvSource(String fruit, int rank) {
    assertNotNull(fruit);
    assertNotEquals(0, rank);
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
  void testWithCsvFileSourceFromClasspath(String country, int reference) {
    assertNotNull(country);
    assertNotEquals(0, reference);
  }

  @ParameterizedTest
  @CsvFileSource(files = "src/main/resources/two-column.csv", numLinesToSkip = 1)
  void testWithCsvFileSourceFromFile(String country, int reference) {
    assertNotNull(country);
    assertNotEquals(0, reference);
  }
}
