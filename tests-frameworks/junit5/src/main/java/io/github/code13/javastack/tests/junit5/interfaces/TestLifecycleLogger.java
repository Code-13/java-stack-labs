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

package io.github.code13.javastack.tests.junit5.interfaces;

import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * TestLifecycleLogger.
 *
 * <p>JUnit Jupiter
 * allows @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, @TestTemplate, @BeforeEach,
 * and @AfterEach to be declared on interface default methods. @BeforeAll and @AfterAll can either
 * be declared on static methods in a test interface or on interface default methods if the test
 * interface or test class is annotated with @TestInstance(Lifecycle.PER_CLASS) (see Test Instance
 * Lifecycle
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 10:53 AM
 */
@TestInstance(Lifecycle.PER_CLASS)
interface TestLifecycleLogger {

  static final Logger logger = Logger.getLogger(TestLifecycleLogger.class.getName());

  @BeforeAll
  default void beforeAllTests() {
    logger.info("Before all tests");
  }

  @AfterAll
  default void afterAllTests() {
    logger.info("After all tests");
  }

  @BeforeEach
  default void beforeEachTest(TestInfo testInfo) {
    logger.info(() -> String.format("About to execute [%s]", testInfo.getDisplayName()));
  }

  @AfterEach
  default void afterEachTest(TestInfo testInfo) {
    logger.info(() -> String.format("Finished executing [%s]", testInfo.getDisplayName()));
  }
}
