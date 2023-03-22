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

package io.github.code13.tests.junit5.std;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * StandardAnnotations.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 11:35 AM
 */
class StandardAnnotationsRunner {

  @BeforeAll
  static void initAll() {}

  @BeforeEach
  void init() {}

  @Test
  void succeedingTest() {}

  @Test
  void failingTest() {
    fail("a failing test");
  }

  @Test
  @Disabled("for demonstration purposes")
  void skippedTest() {
    // not executed
  }

  @Test
  void abortedTest() {
    assumeTrue("abc".contains("Z"));
    fail("test should have been aborted");
  }

  @AfterEach
  void tearDown() {}

  @AfterAll
  static void tearDownAll() {}
}
