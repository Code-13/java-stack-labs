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

package io.github.code13.javastack.tests.junit5;

/**
 * Calculator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/4/2021 3:35 PM
 */
public final class Calculator {

  public int add(int i, int i1) {
    return i + i1;
  }

  public int multiply(int i, int i1) {
    return i * i1;
  }

  public int divide(int i, int i1) {
    return i / i1;
  }

  public int subtract(int i, int i1) {
    return i - i1;
  }
}
