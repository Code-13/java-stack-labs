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

package io.github.code13.javastack.frameworks.junit5;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * TestExecutionOrderRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 4:58 PM
 */
class TestExecutionOrderRunner {

  @TestMethodOrder(OrderAnnotation.class)
  static class MethodOrder {

    @Test
    @Order(1)
    void nullValues() {
      // perform assertions against null values
    }

    @Test
    @Order(2)
    void emptyValues() {
      // perform assertions against empty values
    }

    @Test
    @Order(3)
    void validValues() {
      // perform assertions against valid values
    }
  }

  //  @TestClassOrder(ClassOrderer.OrderAnnotation.class)
  //  static class OrderedNestedTestClassesRunner {}
}
