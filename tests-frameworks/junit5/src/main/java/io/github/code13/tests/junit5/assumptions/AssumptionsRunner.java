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

package io.github.code13.tests.junit5.assumptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import io.github.code13.tests.junit5.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * AssumptionsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 4:21 PM
 */
class AssumptionsRunner {

  private final Calculator calculator = new Calculator();

  @Test
  @DisplayName("testOnlyOnCiServer")
  void testOnlyOnCiServer() {
    assumeTrue("CI".equals(System.getenv("ENV")));
  }

  @Test
  void testOnlyOnDeveloperWorkstation() {
    assumeTrue(
        "DEV".equals(System.getenv("ENV")), () -> "Aborting test: not on developer workstation");
    // remainder of test
  }

  @Test
  void testInAllEnvironments() {
    assumingThat(
        "CI".equals(System.getenv("ENV")),
        () -> {
          // perform these assertions only on the CI server
          assertEquals(2, calculator.divide(4, 2));
        });

    // perform these assertions in all environments
    assertEquals(42, calculator.multiply(6, 7));
  }
}
