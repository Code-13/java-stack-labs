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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Lifecycle and Interoperability.
 *
 * <p>Each invocation of a parameterized test has the same lifecycle as a regular @Test method. For
 * example, @BeforeEach methods will be executed before each invocation. Similar to Dynamic Tests,
 * invocations will appear one by one in the test tree of an IDE. You may at will mix regular @Test
 * methods and @ParameterizedTest methods within the same test class.
 *
 * <p>You may use ParameterResolver extensions with @ParameterizedTest methods. However, method
 * parameters that are resolved by argument sources need to come first in the argument list. Since a
 * test class may contain regular tests as well as parameterized tests with different parameter
 * lists, values from argument sources are not resolved for lifecycle methods (e.g. @BeforeEach) and
 * test class constructors.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 4:57 PM
 */
class LifecycleInteroperabilityTestsRunner {

  @BeforeEach
  void beforeEach(TestInfo testInfo) {
    // ...
  }

  @ParameterizedTest
  @ValueSource(strings = "apple")
  void testWithRegularParameterResolver(String argument, TestReporter testReporter) {
    testReporter.publishEntry("argument", argument);
  }

  @AfterEach
  void afterEach(TestInfo testInfo) {
    // ...
  }
}
