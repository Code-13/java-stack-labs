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

package io.github.code13.javastack.jakartaee.beanvalidation.customer;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import io.github.code13.javastack.jakartaee.beanvalidation.ValidationUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ClassLevelValidation.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 20:51
 */
class ClassLevelValidation {

  @Test
  @DisplayName("test")
  void test() {
    Room room = new Room();
    room.setStudentNames(Collections.singletonList("AAAA"));

    ValidationUtils.validate(() -> ValidationUtils.obtainValidator().validate(room));
  }

  static class ValidStudentCountConstraintValidator
      implements ConstraintValidator<ValidStudentCount, Room> {

    @Override
    public boolean isValid(Room room, ConstraintValidatorContext context) {
      if (room == null) {
        return true;
      }
      boolean isValid = false;
      if (room.getStudentNames().size() <= room.getMaxStuNum()) {
        isValid = true;
      }

      // 自定义提示语（当然你也可以不自定义，那就使用注解里的message字段的值）
      if (!isValid) {
        context.disableDefaultConstraintViolation();
        context
            .buildConstraintViolationWithTemplate("校验失败xxx")
            .addPropertyNode("studentNames")
            .addConstraintViolation();
      }
      return isValid;
    }
  }

  @Target({TYPE, ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Constraint(validatedBy = {ValidStudentCountConstraintValidator.class})
  @interface ValidStudentCount {
    String message() default "学生人数超过最大限额";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  @Data
  static class Room {
    @Positive private int maxStuNum;
    @NotNull private List<String> studentNames;
  }
}
