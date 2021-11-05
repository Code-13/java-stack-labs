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

package io.github.code13.javastack.frameworks.junit5.interfaces;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * TestInterfaceDynamicTestsDemo.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 11:00 AM
 */
interface TestInterfaceDynamicTestsDemo {

  @TestFactory
  default Stream<DynamicTest> dynamicTestsForPalindromes() {
    return Stream.of("racecar", "radar", "mom", "dad")
        .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text))));
  }

  default boolean isPalindrome(String input) {

    StringBuilder strb = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {

      if (input.charAt(i) != ' ') {
        strb.append(input.charAt(i));
      }
    }

    int strlength = strb.length();
    StringBuilder str = new StringBuilder();
    for (int i = strlength - 1; i >= 0; i--) {

      str.append(strb.charAt(i));
    }

    // System.out.println(str + "--" + input);

    if (str.toString().equals(strb.toString())) {
      return true;
    } else {
      return false;
    }

    //  String strrev = input.reverse();

    //    return true;
    // Palindrome code here
  }
}
