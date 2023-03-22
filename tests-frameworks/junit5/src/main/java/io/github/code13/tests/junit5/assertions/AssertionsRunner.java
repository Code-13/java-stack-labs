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

package io.github.code13.tests.junit5.assertions;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.code13.tests.junit5.Calculator;
import io.github.code13.tests.junit5.Gender;
import io.github.code13.tests.junit5.Person;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Assertions.
 *
 * <p>{@link org.junit.jupiter.api.Assertions}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 12:44 PM
 */
class AssertionsRunner {

  private final Calculator calculator = new Calculator();

  private final Person person = new Person("Jane", "Doe", Gender.F, LocalDate.now());

  @Test
  void standardAssertions() {
    assertEquals(2, calculator.add(1, 1));
    assertEquals(
        4, calculator.multiply(2, 2), "The optional failure message is now the last parameter");
    assertTrue(
        'a' < 'b',
        () ->
            "Assertion messages can be lazily evaluated -- "
                + "to avoid constructing complex messages unnecessarily.");
  }

  @Test
  @DisplayName("groupedAssertions")
  void groupedAssertions() {
    // In a grouped assertion all assertions are executed, and all
    // failures will be reported together.
    assertAll(
        "person",
        () -> assertEquals("Jane", person.firstName()),
        () -> assertEquals("Doe", person.lastName()));
  }

  @Test
  @DisplayName("dependentAssertions")
  void dependentAssertions() {
    // Within a code block, if an assertion fails the
    // subsequent code in the same block will be skipped.

    assertAll(
        "properties",
        () -> {
          String firstName = person.firstName();
          assertNotNull(firstName);

          assertAll(
              "first name",
              () -> assertTrue(firstName.startsWith("J")),
              () -> assertTrue(firstName.endsWith("e")));
        },
        () -> {
          String lastName = person.lastName();
          assertNotNull(lastName);

          assertAll(
              "last name",
              () -> assertTrue(lastName.startsWith("D")),
              () -> assertTrue(lastName.endsWith("e")));
        });
  }

  @Test
  @DisplayName("exceptionTesting")
  void exceptionTesting() {
    Exception exception = assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));

    assertEquals("/ by zero", exception.getMessage());
  }

  @Test
  @DisplayName("timeoutNotExceeded")
  void timeoutNotExceeded() {
    assertTimeout(
        ofMinutes(2),
        () -> {
          // Perform task that takes less than 2 minutes
          System.out.println("2");
        });
  }

  @Test
  @DisplayName("timeoutNotExceededWithResult")
  void timeoutNotExceededWithResult() {
    String actualResult = assertTimeout(ofMinutes(2), () -> "a result");
    assertEquals("a result", actualResult);
  }

  @Test
  @DisplayName("timeoutNotExceededWithMethod")
  void timeoutNotExceededWithMethod() {
    String actualResult = assertTimeout(ofMillis(2), AssertionsRunner::greeting);
    assertEquals("Hello, World!", actualResult);
  }

  @Test
  @DisplayName("timeoutExceeded")
  void timeoutExceeded() {
    // The following assertion fails with an error message similar to:
    // execution exceeded timeout of 10 ms by 91 ms
    assertTimeout(
        ofMillis(10),
        () -> {
          // Simulate task that takes more than 10 ms.
          Thread.sleep(100);
        });
  }

  @Test
  void timeoutExceededWithPreemptiveTermination() {
    // The following assertion fails with an error message similar to:
    // execution timed out after 10 ms
    assertTimeoutPreemptively(
        ofMillis(10),
        () -> {
          // Simulate task that takes more than 10 ms.
          new CountDownLatch(1).await();
        });
  }

  private static String greeting() {
    return "Hello, World!";
  }
}
