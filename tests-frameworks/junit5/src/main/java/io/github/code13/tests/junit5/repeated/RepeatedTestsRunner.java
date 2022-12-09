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

package io.github.code13.tests.junit5.repeated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInfo;

/**
 * RepeatedTestsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 11:23 AM
 */
class RepeatedTestsRunner {

  private final Logger logger = Logger.getLogger(RepeatedTestsRunner.class.getSimpleName());

  @BeforeEach
  void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
    int currentRepetition = repetitionInfo.getCurrentRepetition();
    int totalRepetitions = repetitionInfo.getTotalRepetitions();
    String methodName = testInfo.getTestMethod().get().getName();
    logger.info(
        String.format(
            "About to execute repetition %d of %d for %s", //
            currentRepetition, totalRepetitions, methodName));
  }

  @RepeatedTest(10)
  void repeatedTest() {}

  @RepeatedTest(5)
  void repeatedTestWithRepetitionInfo(RepetitionInfo repetitionInfo) {
    assertEquals(5, repetitionInfo.getTotalRepetitions());
  }

  @RepeatedTest(value = 1, name = "{displayName} {currentRepetition}/{totalRepetitions}")
  @DisplayName("Repeat!")
  void customDisplayName(TestInfo testInfo) {
    assertEquals("Repeat! 1/1", testInfo.getDisplayName());
  }

  @RepeatedTest(value = 1, name = RepeatedTest.LONG_DISPLAY_NAME)
  @DisplayName("Details...")
  void customDisplayNameWithLongPattern(TestInfo testInfo) {
    assertEquals("Details... :: repetition 1 of 1", testInfo.getDisplayName());
  }

  @RepeatedTest(value = 5, name = "Wiederholung {currentRepetition} von {totalRepetitions}")
  void repeatedTestInGerman() {
    // ...
  }
}
