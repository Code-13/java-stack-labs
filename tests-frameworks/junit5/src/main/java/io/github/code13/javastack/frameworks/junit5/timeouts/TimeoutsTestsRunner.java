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

package io.github.code13.javastack.frameworks.junit5.timeouts;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Timeouts.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 5:24 PM
 */
class TimeoutsTestsRunner {

  @BeforeEach
  @Timeout(5)
  void setUp() {
    // fails if execution time exceeds 5 seconds
  }

  @Test
  @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
  void failsIfExecutionTimeExceeds100Milliseconds() {
    // fails if execution time exceeds 100 milliseconds
  }

  @Test
  @Timeout(5) // Poll at most 5 seconds
  void pollUntil() throws InterruptedException {
    while (asynchronousResultNotAvailable()) {
      Thread.sleep(250); // custom poll interval
    }
    // Obtain the asynchronous result and perform assertions
  }

  private boolean asynchronousResultNotAvailable() {
    return false;
  }
}
