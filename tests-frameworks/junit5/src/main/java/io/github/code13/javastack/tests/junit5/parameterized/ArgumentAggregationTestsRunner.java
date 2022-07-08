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

package io.github.code13.javastack.tests.junit5.parameterized;

import static org.junit.jupiter.api.Assertions.assertEquals;
import io.github.code13.javastack.tests.junit5.Gender;
import io.github.code13.javastack.tests.junit5.Person;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Argument Aggregation.
 *
 * <p>By default, each argument provided to a {@link ParameterizedTest} method corresponds to a
 * single method parameter. Consequently, argument sources which are expected to supply a large
 * number of arguments can lead to large method signatures.
 *
 * <p>In such cases, an {@link ArgumentsAccessor} can be used instead of multiple parameters. Using
 * this API, you can access the provided arguments through a single argument passed to your test
 * method. In addition, type conversion is supported as discussed in Implicit Conversion.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 4:37 PM
 */
@DisplayName("Argument Aggregation")
class ArgumentAggregationTestsRunner {

  /**
   * An instance of ArgumentsAccessor is automatically injected into any parameter of type
   * ArgumentsAccessor.
   *
   * @param arguments .
   */
  @ParameterizedTest
  @CsvSource({"Jane, Doe, F, 1990-05-20", "John, Doe, M, 1990-10-22"})
  void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
    Person person =
        new Person(
            arguments.getString(0),
            arguments.getString(1),
            arguments.get(2, Gender.class),
            arguments.get(3, LocalDate.class));

    if (person.firstName().equals("Jane")) {
      assertEquals(Gender.F, person.gender());
    } else {
      assertEquals(Gender.M, person.gender());
    }
    assertEquals("Doe", person.lastName());
    assertEquals(1990, person.dateOfBirth().getYear());
  }

  /**
   * To use a custom aggregator, implement the ArgumentsAggregator interface and register it via
   * the @AggregateWith annotation on a compatible parameter in the @ParameterizedTest method. The
   * result of the aggregation will then be provided as an argument for the corresponding parameter
   * when the parameterized test is invoked. Note that an implementation of ArgumentsAggregator must
   * be declared as either a top-level class or as a static nested class.
   *
   * @param person .
   */
  @ParameterizedTest
  @CsvSource({"Jane, Doe, F, 1990-05-20", "John, Doe, M, 1990-10-22"})
  void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
    // perform assertions against person
  }

  static class PersonAggregator implements ArgumentsAggregator {
    @Override
    public Person aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
      return new Person(
          arguments.getString(0),
          arguments.getString(1),
          arguments.get(2, Gender.class),
          arguments.get(3, LocalDate.class));
    }
  }

  /**
   * If you find yourself repeatedly declaring @AggregateWith(MyTypeAggregator.class) for multiple
   * parameterized test methods across your codebase, you may wish to create a custom composed
   * annotation such as @CsvToMyType that is meta-annotated
   * with @AggregateWith(MyTypeAggregator.class). The following example demonstrates this in action
   * with a custom @CsvToPerson annotation.
   *
   * @param person .
   */
  @ParameterizedTest
  @CsvSource({"Jane, Doe, F, 1990-05-20", "John, Doe, M, 1990-10-22"})
  void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
    // perform assertions against person
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  @AggregateWith(PersonAggregator.class)
  public @interface CsvToPerson {}
}
