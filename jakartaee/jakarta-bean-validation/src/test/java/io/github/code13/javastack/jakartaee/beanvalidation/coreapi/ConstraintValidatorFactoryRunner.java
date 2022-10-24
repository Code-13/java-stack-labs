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

/**
 * ConstraintValidatorFactoryRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 16:38
 */
class ConstraintValidatorFactoryRunner {

  /*
   * 约束校验器工厂。
   * ConstraintValidator约束校验器我们应该不陌生：每个约束注解都得指定一个/多个约束校验器，
   * 形如这样：@Constraint(validatedBy = { xxx.class })。
   *
   * ConstraintValidatorFactory就是工厂：可以根据Class生成对象实例。
   */

  /*
   * Hibernate提供了唯一实现ConstraintValidatorFactoryImpl：使用空构造器生成实例 clazz.getConstructor().newInstance();。
   */
}
