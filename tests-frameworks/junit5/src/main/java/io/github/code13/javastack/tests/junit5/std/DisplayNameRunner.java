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

package io.github.code13.javastack.tests.junit5.std;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * DisplayNameRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 11:57 AM
 */
class DisplayNameRunner {

  @DisplayName("A special test case")
  static class DisplayNameDemo {

    @Test
    @DisplayName("Custom test name containing spaces")
    void testWithDisplayNameContainingSpaces() {}

    @Test
    @DisplayName("╯°□°）╯")
    void testWithDisplayNameContainingSpecialCharacters() {}

    @Test
    @DisplayName("😱")
    void testWithDisplayNameContainingEmoji() {}
  }

  static class DisplayNameGeneratorDemo {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class A_year_is_not_supported {

      @Test
      void if_it_is_zero() {}

      @DisplayName("A negative value for year is not supported by the leap year computation.")
      @ParameterizedTest(name = "For example, year {0} is not supported.")
      @ValueSource(ints = {-1, -4})
      void if_it_is_negative(int year) {}
    }

    @Nested
    @IndicativeSentencesGeneration(
        separator = " -> ",
        generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class A_year_is_a_leap_year {

      @Test
      void if_it_is_divisible_by_4_but_not_by_100() {}

      @ParameterizedTest(name = "Year {0} is a leap year.")
      @ValueSource(ints = {2016, 2020, 2048})
      void if_it_is_one_of_the_following_years(int year) {}
    }
  }
}
