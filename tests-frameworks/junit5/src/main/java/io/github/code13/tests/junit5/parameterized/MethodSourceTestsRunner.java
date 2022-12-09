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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * {@link MethodSource} allows you to refer to one or more factory methods of the test class or
 * external classes.
 *
 * <p>Factory methods within the test class must be static unless the test class is annotated
 * with @TestInstance(Lifecycle.PER_CLASS); whereas, factory methods in external classes must always
 * be static. In addition, such factory methods must not accept any arguments.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 3:32 PM
 */
class MethodSourceTestsRunner {

  /**
   * If you only need a single parameter, you can return a Stream of instances of the parameter type
   * as demonstrated in the following example.
   *
   * @param argument .
   */
  @ParameterizedTest
  @MethodSource("stringProvider")
  void testWithExplicitLocalMethodSource(String argument) {
    assertNotNull(argument);
  }

  static Stream<String> stringProvider() {
    return Stream.of("apple", "banana");
  }

  /**
   * If you do not explicitly provide a factory method name via @MethodSource, JUnit Jupiter will
   * search for a factory method that has the same name as the current @ParameterizedTest method by
   * convention. This is demonstrated in the following example.
   *
   * @param argument .
   */
  @ParameterizedTest
  @MethodSource
  void testWithDefaultLocalMethodSource(String argument) {
    assertNotNull(argument);
  }

  static Stream<String> testWithDefaultLocalMethodSource() {
    return Stream.of("apple", "banana");
  }

  /**
   * Streams for primitive types (DoubleStream, IntStream, and LongStream) are also supported as
   * demonstrated by the following example.
   *
   * @param argument .
   */
  @ParameterizedTest
  @MethodSource("range")
  void testWithRangeMethodSource(int argument) {
    assertNotEquals(9, argument);
  }

  static IntStream range() {
    return IntStream.range(0, 20).skip(10);
  }

  /**
   * If a parameterized test method declares multiple parameters, you need to return a collection,
   * stream, or array of Arguments instances or object arrays as shown below (see the Javadoc
   * for @MethodSource for further details on supported return types). Note that arguments(Object…)
   * is a static factory method defined in the Arguments interface. In addition,
   * Arguments.of(Object…) may be used as an alternative to arguments(Object…).
   *
   * @param str .
   * @param num .
   * @param list .
   */
  @ParameterizedTest
  @MethodSource("stringIntAndListProvider")
  void testWithMultiArgMethodSource(String str, int num, List<String> list) {
    assertEquals(5, str.length());
    assertTrue(num >= 1 && num <= 2);
    assertEquals(2, list.size());
  }

  static Stream<Arguments> stringIntAndListProvider() {
    return Stream.of(
        arguments("apple", 1, Arrays.asList("a", "b")),
        arguments("lemon", 2, Arrays.asList("x", "y")));
  }

  /**
   * An external, static factory method can be referenced by providing its fully qualified method
   * name as demonstrated in the following example.
   *
   * @param tinyString .
   */
  @ParameterizedTest
  @MethodSource("io.github.code13.frameworks.junit5.parameterized.StringsProviders#tinyStrings")
  void testWithExternalMethodSource(String tinyString) {
    // test with tiny string
  }
}

class StringsProviders {

  private StringsProviders() {}

  static Stream<String> tinyStrings() {
    return Stream.of(".", "oo", "OOO");
  }
}
