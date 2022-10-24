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

package io.github.code13.javastack.jakartaee.beanvalidation.coreapi;

import java.util.Arrays;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ParameterNameProviderRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 16:40
 */
class ParameterNameProviderRunner {

  /*
   * 获取方法/构造器的参数名
   */

  @Test
  @DisplayName("test")
  void test() {

    ParameterNameProvider parameterNameProvider = new DefaultParameterNameProvider();

    // 拿到Person的无参构造和有参构造（@NoArgsConstructor和@AllArgsConstructor）
    Arrays.stream(String.class.getConstructors())
        .forEach(c -> System.out.println(parameterNameProvider.getParameterNames(c)));
  }
}
