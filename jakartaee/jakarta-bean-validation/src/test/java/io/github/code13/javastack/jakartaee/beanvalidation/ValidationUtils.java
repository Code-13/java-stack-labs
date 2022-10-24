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

package io.github.code13.javastack.jakartaee.beanvalidation;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

/**
 * ValidationUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 14:08
 */
public abstract class ValidationUtils {

  private ValidationUtils() {
    // no instance
  }

  public static ValidatorFactory obtainValidatorFactory() {
    return Validation.buildDefaultValidatorFactory();
  }

  public static Validator obtainValidator() {
    return obtainValidatorFactory().getValidator();
  }

  public static ExecutableValidator obtainExecutableValidator() {
    return obtainValidator().forExecutables();
  }

  public static <T> String printViolations(
      Set<? extends ConstraintViolation<? extends T>> violations) {
    String msg =
        violations.stream()
            .map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue())
            .collect(Collectors.joining("\\n"));

    System.out.println(msg);

    return msg;
  }

  public static <T> String validate(
      Supplier<Set<? extends ConstraintViolation<? extends T>>> violationSuppliers) {
    Set<? extends ConstraintViolation<? extends T>> violations = violationSuppliers.get();

    String msg =
        violations.stream()
            .map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue())
            .collect(Collectors.joining("\\n"));

    System.out.println(msg);

    return msg;
  }
}
