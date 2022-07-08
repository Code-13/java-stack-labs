/*
 *     Copyright 2021-present the original author or authors.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package io.github.code13.javastack.frameworks.junit5.parameterized;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ArgumentConversionTestsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 4:11 PM
 */
@DisplayName("ArgumentConversionTestsRunner")
class ArgumentConversionTestsRunner {

  private ArgumentConversionTestsRunner() {}

  /**
   * JUnit Jupiter supports Widening Primitive Conversion for arguments supplied to
   * a @ParameterizedTest. For example, a parameterized test annotated with @ValueSource(ints = { 1,
   * 2, 3 }) can be declared to accept not only an argument of type int but also an argument of type
   * long, float, or double.
   */
  @DisplayName("WideningConversion")
  static class WideningConversionRunner {}

  /**
   * To support use cases like @CsvSource, JUnit Jupiter provides a number of built-in implicit type
   * converters. The conversion process depends on the declared type of each method parameter.
   *
   * <p>For example, if a @ParameterizedTest declares a parameter of type TimeUnit and the actual
   * type supplied by the declared source is a String, the string will be automatically converted
   * into the corresponding TimeUnit enum constant.
   *
   * @see <a
   *     href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion-implicit">ImplicitConversion</a>
   */
  @DisplayName("ImplicitConversion")
  static class ImplicitConversionRunner {}

  /**
   * Fallback String-to-Object Conversion
   *
   * <p>In addition to implicit conversion from strings to the target types listed in the above
   * table, JUnit Jupiter also provides a fallback mechanism for automatic conversion from a String
   * to a given target type if the target type declares exactly one suitable factory method or a
   * factory constructor as defined below.
   *
   * <p>factory method: a non-private, static method declared in the target type that accepts a
   * single String argument and returns an instance of the target type. The name of the method can
   * be arbitrary and need not follow any particular convention.
   *
   * <p>factory constructor: a non-private constructor in the target type that accepts a single
   * String argument. Note that the target type must be declared as either a top-level class or as a
   * static nested class.
   */
  @DisplayName("FallbackStringToObjectConversion")
  static class FallbackStringToObjectConversionRunner {

    @ParameterizedTest
    @ValueSource(strings = "42 Cats")
    void testWithImplicitFallbackArgumentConversion(Book book) {
      assertEquals("42 Cats", book.getTitle());
    }

    public static class Book {

      private final String title;

      private Book(String title) {
        this.title = title;
      }

      public static Book fromTitle(String title) {
        return new Book(title);
      }

      public String getTitle() {
        return title;
      }
    }
  }

  /**
   * Explicit Conversion.
   *
   * <p>Instead of relying on implicit argument conversion you may explicitly specify an
   * ArgumentConverter to use for a certain parameter using the @ConvertWith annotation like in the
   * following example. Note that an implementation of ArgumentConverter must be declared as either
   * a top-level class or as a static nested class.
   */
  @DisplayName("Explicit Conversion")
  static class ExplicitConversionRunner {

    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithExplicitArgumentConversion(
        @ConvertWith(ToStringArgumentConverter.class) String argument) {

      assertNotNull(ChronoUnit.valueOf(argument));
    }

    static class ToStringArgumentConverter extends SimpleArgumentConverter {

      @Override
      protected Object convert(Object source, Class<?> targetType) {
        assertEquals(String.class, targetType, "Can only convert to String");
        if (source instanceof Enum<?>) {
          return ((Enum<?>) source).name();
        }
        return String.valueOf(source);
      }
    }

    /**
     * If the converter is only meant to convert one type to another, you can extend
     * TypedArgumentConverter to avoid boilerplate type checks.
     */
    static class ToLengthArgumentConverter extends TypedArgumentConverter<String, Integer> {

      protected ToLengthArgumentConverter() {
        super(String.class, Integer.class);
      }

      @Override
      protected Integer convert(String source) {
        return (source != null ? source.length() : 0);
      }
    }

    /**
     * Explicit argument converters are meant to be implemented by test and extension authors. Thus,
     * junit-jupiter-params only provides a single explicit argument converter that may also serve
     * as a reference implementation: JavaTimeArgumentConverter. It is used via the composed
     * annotation JavaTimeConversionPattern.
     *
     * @param argument .
     */
    @ParameterizedTest
    @ValueSource(strings = {"01.01.2017", "31.12.2017"})
    void testWithExplicitJavaTimeConverter(
        @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {

      assertEquals(2017, argument.getYear());
    }
  }
}
