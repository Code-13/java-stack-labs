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

package io.github.code13.tests.junit5.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * RandomParametersExtension.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 10:10 AM
 */
class RandomParametersExtension implements ParameterResolver {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  @interface Random {}

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(Random.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return getRandomValue(parameterContext.getParameter(), extensionContext);
  }

  private Object getRandomValue(Parameter parameter, ExtensionContext extensionContext) {
    Class<?> type = parameter.getType();

    java.util.Random random =
        extensionContext
            .getRoot()
            .getStore(Namespace.GLOBAL)
            .getOrComputeIfAbsent(java.util.Random.class);

    if (int.class.equals(type)) {
      return random.nextInt();
    }

    if (double.class.equals(type)) {
      return random.nextDouble();
    }

    return new ParameterResolutionException("No random generator implemented for " + type);
  }
}
