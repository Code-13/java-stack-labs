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

import io.github.code13.jakartaee.beanvalidation.ValidationUtils;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import org.hibernate.validator.internal.engine.DefaultClockProvider;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
import org.hibernate.validator.internal.engine.ValidatorContextImpl;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ValidatorContextRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 16:06
 */
class ValidatorContextRunner {

  @Test
  @DisplayName("test1")
  void test1() {
    ValidatorFactoryImpl validatorFactory =
        (ValidatorFactoryImpl) ValidationUtils.obtainValidatorFactory();
    // 使用默认的Context上下文，并且初始化一个Validator实例
    // 必须传入一个校验器工厂实例哦
    ValidatorContext validatorContext =
        new ValidatorContextImpl(validatorFactory)
            .parameterNameProvider(new DefaultParameterNameProvider())
            .clockProvider(DefaultClockProvider.INSTANCE);

    // 通过该上下文，生成校验器实例（注意：调用多次，生成实例是多个哟）
    System.out.println(validatorContext.getValidator());
  }

  @Test
  @DisplayName("test2")
  void test2() {
    Validator validator =
        ValidationUtils.obtainValidatorFactory()
            .usingContext()
            .parameterNameProvider(new DefaultParameterNameProvider())
            .clockProvider(DefaultClockProvider.INSTANCE)
            .getValidator();
  }
}
