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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.EnumSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ValueSourceTestsRunner.
 *
 * <p>{@link ValueSource} is one of the simplest possible sources. It lets you specify a single
 * array of literal values and can only be used for providing a single argument per parameterized
 * test invocation.
 *
 * <pre>
 *    * The following types of literal values are supported by @ValueSource.
 *    * short
 *    * byte
 *    * int
 *    * long
 *    * float
 *    * double
 *    * char
 *    * boolean
 *    * java.lang.String
 *    * java.lang.Class
 *    * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 3:32 PM
 */
@DisplayName("ValueSource")
public class ValueSourceTestsRunner {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void testWithValueSource(int argument) {
    assertTrue(argument > 0 && argument < 4);
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void nullEmptyAndBlankStrings(String text) {
    assertTrue(text == null || text.trim().isEmpty());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void nullEmptyAndBlankStrings1(String text) {
    assertTrue(text == null || text.trim().isEmpty());
  }

  @ParameterizedTest
  @EnumSource(ChronoUnit.class)
  void testWithEnumSource(TemporalUnit unit) {
    assertNotNull(unit);
  }

  /**
   * The annotation’s value attribute is optional. When omitted, the declared type of the first
   * method parameter is used. The test will fail if it does not reference an enum type. Thus, the
   * value attribute is required in the above example because the method parameter is declared as
   * TemporalUnit, i.e. the interface implemented by ChronoUnit, which isn’t an enum type. Changing
   * the method parameter type to ChronoUnit allows you to omit the explicit enum type from the
   * annotation as follows.
   *
   * @param unit .
   */
  @ParameterizedTest
  @EnumSource
  void testWithEnumSourceWithAutoDetection(ChronoUnit unit) {
    assertNotNull(unit);
  }

  /**
   * The annotation provides an optional names attribute that lets you specify which constants shall
   * be used, like in the following example. If omitted, all constants will be used.
   *
   * @param unit .
   */
  @ParameterizedTest
  @EnumSource(names = {"DAYS", "HOURS"})
  void testWithEnumSourceInclude(ChronoUnit unit) {
    assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
  }

  @ParameterizedTest
  @EnumSource(
      mode = EXCLUDE,
      names = {"ERAS", "FOREVER"})
  void testWithEnumSourceExclude(ChronoUnit unit) {
    assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
  }

  @ParameterizedTest
  @EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
  void testWithEnumSourceRegex(ChronoUnit unit) {
    assertTrue(unit.name().endsWith("DAYS"));
  }
}
