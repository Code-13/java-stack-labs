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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Customizing Display Names.
 *
 * <p>By default, the display name of a parameterized test invocation contains the invocation index
 * and the String representation of all arguments for that specific invocation. Each of them is
 * preceded by the parameter name (unless the argument is only available via an ArgumentsAccessor or
 * ArgumentAggregator), if present in the bytecode (for Java, test code must be compiled with the
 * -parameters compiler flag).
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 4:50 PM
 */
class CustomizingDisplayNamesTestsRunner {

  /**
   * you can customize invocation display names via the name attribute of the @ParameterizedTest
   * annotation like in the following example.
   *
   * @param fruit .
   * @param rank .
   */
  @DisplayName("Display name of container")
  @ParameterizedTest(name = "{index} ==> the rank of ''{0}'' is {1}")
  @CsvSource({"apple, 1", "banana, 2", "'lemon, lime', 3"})
  void testWithCustomDisplayNames(String fruit, int rank) {
    //
  }
}
