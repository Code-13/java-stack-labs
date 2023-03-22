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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.github.code13.tests.junit5.Calculator;
import org.junit.jupiter.api.Test;

/**
 * HamcrestAssertionsRunner.
 *
 * <p>hamcrest
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 4:12 PM
 */
class HamcrestAssertionsRunner {

  private final Calculator calculator = new Calculator();

  @Test
  void assertWithHamcrestMatcher() {
    assertThat(calculator.subtract(4, 1), is(equalTo(3)));
  }
}
