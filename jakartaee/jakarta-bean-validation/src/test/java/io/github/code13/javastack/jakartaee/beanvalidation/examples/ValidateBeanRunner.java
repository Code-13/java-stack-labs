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

package io.github.code13.javastack.jakartaee.beanvalidation.examples;

import io.github.code13.javastack.jakartaee.beanvalidation.ValidationUtils;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ValidateBeanRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 14:17
 */
@DisplayName("校验Java Bean")
class ValidateBeanRunner {

  @Test
  @DisplayName("test")
  void test() {
    Person person = new Person();
    person.setAge(-1);

    Set<ConstraintViolation<Person>> violations =
        ValidationUtils.obtainValidator().validate(person);
    ValidationUtils.printViolations(violations);
  }

  @ToString
  @Setter
  @Getter
  static class Person {

    @NotNull public String name;

    @NotNull
    @Min(0)
    public Integer age;
  }
}
