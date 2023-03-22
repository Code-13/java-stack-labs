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

package io.github.code13.tests.junit5.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.github.code13.tests.junit5.Calculator;
import io.github.code13.tests.junit5.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

/**
 * DynamicTestsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 5:20 PM
 */
class DynamicTestsRunner {

  private final Calculator calculator = new Calculator();

  // This will result in a JUnitException!
  @TestFactory
  List<String> dynamicTestsWithInvalidReturnType() {
    return Arrays.asList("Hello");
  }

  @TestFactory
  Collection<DynamicTest> dynamicTestsFromCollection() {
    return Arrays.asList(
        dynamicTest(
            "1st dynamic test", () -> Assertions.assertTrue(StringUtils.isPalindrome("madam"))),
        dynamicTest("2nd dynamic test", () -> assertEquals(4, calculator.multiply(2, 2))));
  }

  @TestFactory
  Iterable<DynamicTest> dynamicTestsFromIterable() {
    return Arrays.asList(
        dynamicTest(
            "3rd dynamic test", () -> Assertions.assertTrue(StringUtils.isPalindrome("madam"))),
        dynamicTest("4th dynamic test", () -> assertEquals(4, calculator.multiply(2, 2))));
  }

  @TestFactory
  Iterator<DynamicTest> dynamicTestsFromIterator() {
    return Arrays.asList(
            dynamicTest(
                "5th dynamic test", () -> Assertions.assertTrue(StringUtils.isPalindrome("madam"))),
            dynamicTest("6th dynamic test", () -> assertEquals(4, calculator.multiply(2, 2))))
        .iterator();
  }

  @TestFactory
  DynamicTest[] dynamicTestsFromArray() {
    return new DynamicTest[] {
      dynamicTest(
          "7th dynamic test", () -> Assertions.assertTrue(StringUtils.isPalindrome("madam"))),
      dynamicTest("8th dynamic test", () -> assertEquals(4, calculator.multiply(2, 2)))
    };
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestsFromStream() {
    return Stream.of("racecar", "radar", "mom", "dad")
        .map(
            text -> dynamicTest(text, () -> Assertions.assertTrue(StringUtils.isPalindrome(text))));
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestsFromIntStream() {
    // Generates tests for the first 10 even integers.
    return IntStream.iterate(0, n -> n + 2)
        .limit(10)
        .mapToObj(n -> dynamicTest("test" + n, () -> assertTrue(n % 2 == 0)));
  }

  @TestFactory
  Stream<DynamicTest> generateRandomNumberOfTestsFromIterator() {

    // Generates random positive integers between 0 and 100 until
    // a number evenly divisible by 7 is encountered.
    Iterator<Integer> inputGenerator =
        new Iterator<Integer>() {

          Random random = new Random();
          int current;

          @Override
          public boolean hasNext() {
            current = random.nextInt(100);
            return current % 7 != 0;
          }

          @Override
          public Integer next() {
            return current;
          }
        };

    // Generates display names like: input:5, input:37, input:85, etc.
    Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

    // Executes tests based on the current input value.
    ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

    // Returns a stream of dynamic tests.
    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestsFromStreamFactoryMethod() {
    // Stream of palindromes to check
    Stream<String> inputStream = Stream.of("racecar", "radar", "mom", "dad");

    // Generates display names like: racecar is a palindrome
    Function<String, String> displayNameGenerator = text -> text + " is a palindrome";

    // Executes tests based on the current input value.
    ThrowingConsumer<String> testExecutor =
        text -> Assertions.assertTrue(StringUtils.isPalindrome(text));

    // Returns a stream of dynamic tests.
    return DynamicTest.stream(inputStream, displayNameGenerator, testExecutor);
  }

  //  @TestFactory
  //  Stream<DynamicTest> dynamicTestsFromStreamFactoryMethodWithNames() {
  //    // Stream of palindromes to check
  //    Stream<Named<String>> inputStream =
  //        Stream.of(
  //            named("racecar is a palindrome", "racecar"),
  //            named("radar is also a palindrome", "radar"),
  //            named("mom also seems to be a palindrome", "mom"),
  //            named("dad is yet another palindrome", "dad"));
  //
  //    // Returns a stream of dynamic tests.
  //    return DynamicTest.stream(inputStream, text -> assertTrue(isPalindrome(text)));
  //  }

  @TestFactory
  Stream<DynamicNode> dynamicTestsWithContainers() {
    return Stream.of("A", "B", "C")
        .map(
            input ->
                dynamicContainer(
                    "Container " + input,
                    Stream.of(
                        dynamicTest("not null", () -> assertNotNull(input)),
                        dynamicContainer(
                            "properties",
                            Stream.of(
                                dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
                                dynamicTest("not empty", () -> assertFalse(input.isEmpty())))))));
  }

  @TestFactory
  DynamicNode dynamicNodeSingleTest() {
    return dynamicTest(
        "'pop' is a palindrome", () -> Assertions.assertTrue(StringUtils.isPalindrome("pop")));
  }

  @TestFactory
  DynamicNode dynamicNodeSingleContainer() {
    return dynamicContainer(
        "palindromes",
        Stream.of("racecar", "radar", "mom", "dad")
            .map(
                text ->
                    dynamicTest(
                        text, () -> Assertions.assertTrue(StringUtils.isPalindrome(text)))));
  }
}
