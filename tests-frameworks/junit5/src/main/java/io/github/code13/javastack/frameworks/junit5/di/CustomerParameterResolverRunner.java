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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import io.github.code13.javastack.frameworks.junit5.di.RandomParametersExtension.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * CustomerParameterResolverRunner.
 *
 * <p>Other parameter resolvers must be explicitly enabled by registering appropriate extensions *
 * via @ExtendWith.
 *
 * <p>When the type of the parameter to inject is the only condition for your ParameterResolver, you
 * can use the generic TypeBasedParameterResolver base class. The supportsParameters method is
 * implemented behind the scenes and supports parameterized types.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 10:07 AM
 */
@DisplayName("customer ParameterResolver")
@ExtendWith(RandomParametersExtension.class)
class CustomerParameterResolverRunner {

  @Test
  void injectsInteger(@Random int i, @Random int j) {
    assertNotEquals(i, j);
  }

  @Test
  void injectsDouble(@Random double d) {
    assertEquals(0.0, d, 1.0);
  }
}
