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

package io.github.code13.jakartaee.beanvalidation.coreapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.github.code13.jakartaee.beanvalidation.ValidationUtils;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.MethodType;
import javax.validation.metadata.PropertyDescriptor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ValidatorApiRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 15:04
 */
@DisplayName("javax.validation.Validator 标准接口")
class ValidatorApiRunner {

  @Test
  @DisplayName("validate: 校验Java Bean")
  void validate() {

    /*
     *  校验Java Bean所有约束中的所有包括： 1、属性上的约束 2、类上的约束
     */

    User user = new User();
    user.setName("aaa");

    Set<ConstraintViolation<User>> violations = ValidationUtils.obtainValidator().validate(user);
    assertNotNull(ValidationUtils.printViolations(violations));
  }

  @Test
  @DisplayName("validateProperty: 校验指定属性")
  void validateProperty() {
    User user = new User();
    user.setFullName("aaa");

    assertNotNull(
        ValidationUtils.validate(
            () -> ValidationUtils.obtainValidator().validateProperty(user, "fullName")));
  }

  @Test
  @DisplayName("validateValue: 校验value值")
  void validateValue() {
    /*
     * 校验某个value值，是否符合指定属性上的所有约束。可理解为：若我把这个value值赋值给这个属性，是否合法？
     * 这个校验方法比较特殊：不用先存在对象实例，直接校验某个值是否满足某个属性的所有约束，所以它可以做事钱校验判断，还是挺好用的。
     */

    assertNotNull(
        ValidationUtils.validate(
            () -> ValidationUtils.obtainValidator().validateValue(User.class, "fullName", "aaaa")));
  }

  @Test
  @DisplayName("getConstraintsForClass: 获取Class类型描述信息")
  void getConstraintsForClass() {
    BeanDescriptor beanDescriptor =
        ValidationUtils.obtainValidator().getConstraintsForClass(User.class);

    System.out.println("此类是否需要校验：" + beanDescriptor.isBeanConstrained());

    // 获取属性、方法、构造器的约束
    Set<PropertyDescriptor> constrainedProperties = beanDescriptor.getConstrainedProperties();
    Set<MethodDescriptor> constrainedMethods =
        beanDescriptor.getConstrainedMethods(MethodType.GETTER);
    Set<ConstructorDescriptor> constrainedConstructors =
        beanDescriptor.getConstrainedConstructors();
    System.out.println("需要校验的属性：" + constrainedProperties);
    System.out.println("需要校验的方法：" + constrainedMethods);
    System.out.println("需要校验的构造器：" + constrainedConstructors);

    PropertyDescriptor fullNameDesc = beanDescriptor.getConstraintsForProperty("fullName");
    System.out.println(fullNameDesc);
    System.out.println("fullName属性的约束注解个数：" + fullNameDesc.getConstraintDescriptors().size());
  }

  /**
   * @see javax.validation.executable.ExecutableValidator
   */
  @Test
  @DisplayName("forExecutables: 获得Executable校验器")
  void forExecutables() {
    /*
     * Validator 这个API是1.0就提出的，它只能校验 Java Bean，对于方法、构造器的参数、返回值等校验还无能为力。
     * 1.1版本就提供了 ExecutableValidator 这个API解决这类需求，它的实例可通过调用 Validator 的该方法获得，非常方便。
     */
  }

  @Data
  static class User {

    @NotNull private String name;

    @Length(min = 20)
    @NotNull
    private String fullName;
  }
}
