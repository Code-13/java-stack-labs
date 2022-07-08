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

package io.github.code13.javastack.frameworks.junit5.di;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * DependencyInjection
 *
 * <p>{@link ParameterResolver}
 *
 * <p>{@link ParameterResolver} defines the API for test extensions that wish to dynamically resolve
 * parameters at runtime. If a test class constructor, a test method, or a lifecycle method (see
 * Test Classes and Methods) accepts a parameter, the parameter must be resolved at runtime by a
 * registered {@link ParameterResolver}.
 *
 * <p>There are currently three built-in resolvers that are registered automatically.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 9:20 AM
 */
@DisplayName("DependencyInjectionRunner")
class DependencyInjectionRunner {

  /**
   * {@see org.junit.jupiter.engine.extension.TestInfoParameterResolver} and {@link TestInfo}
   *
   * <p>TestInfoParameterResolver: if a constructor or method parameter is of type TestInfo, the
   * TestInfoParameterResolver will supply an instance of TestInfo corresponding to the current
   * container or test as the value for the parameter. The TestInfo can then be used to retrieve
   * information about the current container or test such as the display name, the test class, the
   * test method, and associated tags. The display name is either a technical name, such as the name
   * of the test class or test method, or a custom name configured via @DisplayName.
   *
   * <p>TestInfo acts as a drop-in replacement for the TestName rule from JUnit 4. The following
   * demonstrates how to have TestInfo injected into a test constructor, @BeforeEach method,
   * and @Test method.
   */
  @DisplayName("TestInfo Demo")
  static class TestInfoRunner {

    TestInfoRunner(TestInfo testInfo) {
      assertEquals("TestInfo Demo", testInfo.getDisplayName());
    }

    @BeforeEach
    void init(TestInfo testInfo) {
      String displayName = testInfo.getDisplayName();
      assertTrue(displayName.equals("TEST 1") || displayName.equals("test2"));
    }

    @Test
    @DisplayName("TEST 1")
    @Tag("my-tag")
    void test1(TestInfo testInfo) {
      assertEquals("TEST 1", testInfo.getDisplayName());
      assertTrue(testInfo.getTags().contains("my-tag"));
    }

    @Test
    void test2() {}
  }

  /**
   * RepetitionInfo
   *
   * <p>RepetitionInfoParameterResolver: if a method parameter in a @RepeatedTest, @BeforeEach,
   * or @AfterEach method is of type RepetitionInfo, the RepetitionInfoParameterResolver will supply
   * an instance of RepetitionInfo. RepetitionInfo can then be used to retrieve information about
   * the current repetition and the total number of repetitions for the corresponding @RepeatedTest.
   * Note, however, that RepetitionInfoParameterResolver is not registered outside the context of
   * a @RepeatedTest. See Repeated Test Examples.
   */
  @DisplayName("RepetitionInfo Demo")
  static class RepetitionInfoRunner {}

  /**
   * TestReporterParameterResolver and TestReporter
   *
   * <p>TestReporterParameterResolver: if a constructor or method parameter is of type TestReporter,
   * the TestReporterParameterResolver will supply an instance of TestReporter. The TestReporter can
   * be used to publish additional data about the current test run. The data can be consumed via the
   * reportingEntryPublished() method in a TestExecutionListener, allowing it to be viewed in IDEs
   * or included in reports.
   *
   * <p>In JUnit Jupiter you should use TestReporter where you used to print information to stdout
   * or stderr in JUnit 4. Using @RunWith(JUnitPlatform.class) will output all reported entries to
   * stdout. In addition, some IDEs print report entries to stdout or display them in the user
   * interface for test results.
   */
  @DisplayName("TestReporter Demo")
  static class TestReporterRunner {

    @Test
    void reportSingleValue(TestReporter testReporter) {
      testReporter.publishEntry("a status message");
    }

    @Test
    void reportKeyValuePair(TestReporter testReporter) {
      testReporter.publishEntry("a key", "a value");
    }

    @Test
    void reportMultipleValuePairs(TestReporter testReporter) {
      Map<String, String> values = Map.of("user name", "dk38", "award year", "1974");
      testReporter.publishEntry(values);
    }
  }
}
