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

package io.github.code13.jakartaee.beanvalidation.examples;

import io.github.code13.jakartaee.beanvalidation.ValidationUtils;
import java.lang.reflect.Constructor;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ValidateBeanRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 14:17
 */
@DisplayName("校验构造方法")
class ValidateConstructorRunner {

  @Test
  @DisplayName("test")
  void test() throws NoSuchMethodException {
    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () -> {
          new Person("name", -1);
        });
  }

  record Person(@NotNull String name, @NotNull @Min(0) Integer age) {

    Person(@NotNull String name, @NotNull @Min(0) Integer age) {

      try {
        Constructor<? extends Person> constructor =
            getClass().getDeclaredConstructor(String.class, Integer.class);
        Set<? extends ConstraintViolation<? extends Person>> violations =
            ValidationUtils.obtainExecutableValidator()
                .validateConstructorParameters(constructor, new Object[] {name, age});

        if (!violations.isEmpty()) {
          ValidationUtils.printViolations(violations);
          throw new IllegalArgumentException("参数错误");
        }

      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }

      this.name = name;
      this.age = age;
    }
  }
}
