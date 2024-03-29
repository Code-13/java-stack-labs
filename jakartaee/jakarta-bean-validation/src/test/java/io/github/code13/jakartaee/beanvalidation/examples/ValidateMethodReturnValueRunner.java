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
import io.github.code13.jakartaee.beanvalidation.examples.ValidateBeanRunner.Person;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ValidateBeanRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 14:17
 */
@DisplayName("校验方法返回值")
class ValidateMethodReturnValueRunner {

  @Test
  @DisplayName("test")
  void test() throws NoSuchMethodException {
    PersonService personService = new PersonService();
    Assertions.assertThrowsExactly(
        IllegalArgumentException.class,
        () -> {
          ValidateBeanRunner.Person person = personService.getOne(1, "name");
        });
  }

  static class PersonService {

    /*
     * 1. id是必传（不为null）且最小值为1，但对name没有要求
     *
     * 2. 返回值不能为null
     */
    public @NotNull ValidateBeanRunner.Person getOne(@NotNull @Min(1) Integer id, String name)
        throws NoSuchMethodException {

      Person person = null;

      Method method = getClass().getMethod("getOne", Integer.class, String.class);

      Set<ConstraintViolation<PersonService>> violations =
          ValidationUtils.obtainExecutableValidator().validateReturnValue(this, method, person);

      if (!violations.isEmpty()) {
        ValidationUtils.printViolations(violations);
        throw new IllegalArgumentException("参数错误");
      }

      return person;
    }
  }
}
