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

package io.github.code13.spring.framework.object_provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

/**
 * ObtainingAnInstanceOfObjectProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/23 17:34
 */
public class ObtainingAnInstanceOfObjectProvider {

  @Test
  @DisplayName("testObtainingAnInstanceOfObjectProviderFromBeanFactory")
  void testObtainingAnInstanceOfObjectProviderFromBeanFactory() {
    try (var context = new AnnotationConfigApplicationContext()) {
      context.register(HelloService.class);
      context.refresh();

      ObjectProvider<HelloService> beanProvider = context.getBeanProvider(HelloService.class);
      beanProvider.ifAvailable(HelloService::sayHello);
    }
  }

  @Test
  @DisplayName("testObtainingAnInstanceOfObjectProviderFromBeanFactoryWithResolvableType")
  void testObtainingAnInstanceOfObjectProviderFromBeanFactoryWithResolvableType() {
    try (var context = new AnnotationConfigApplicationContext()) {
      context.register(HelloService.class);
      context.refresh();

      ResolvableType resolvableType = ResolvableType.forClass(HelloService.class);
      ObjectProvider<HelloService> beanProvider = context.getBeanProvider(resolvableType);
      beanProvider.ifAvailable(HelloService::sayHello);
    }
  }

  static class HelloService {

    public void sayHello() {
      System.out.println("hello, " + ObjectProvider.class.getName());
    }
  }
}
