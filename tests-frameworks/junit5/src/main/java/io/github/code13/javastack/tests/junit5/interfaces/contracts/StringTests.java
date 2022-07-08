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

package io.github.code13.javastack.tests.junit5.interfaces.contracts;

/**
 * StringTests.
 *
 * <p>In your test class you can then implement both contract interfaces thereby inheriting the
 * corresponding tests. Of course you’ll have to implement the abstract methods.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 11:14 AM
 */
class StringTests implements ComparableContract<String>, EqualsContract<String> {

  @Override
  public String createSmallerValue() {
    return "apple"; // 'a' < 'b' in "banana"
  }

  @Override
  public String createNotEqualValue() {
    return "cherry";
  }

  @Override
  public String createValue() {
    return "banana";
  }
}
