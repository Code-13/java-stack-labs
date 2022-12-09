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

package io.github.code13.spring.framework.aop.use;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * SpringAopUseConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/9 15:03
 */
public class SpringAopUseRunner {

  @Test
  @DisplayName("testAop")
  void testAop() {
    try (var applicationContext =
        new AnnotationConfigApplicationContext(SpringAopUseConfig.class)) {
      HelloService helloService = applicationContext.getBean(HelloService.class);
      helloService.sayHello();

      // AOP Before Advice...
      // hello world !
      // AOP AfterReturning Advice:null
      // AOP After Advice...
    }
  }

  @ComponentScan(basePackages = "io.github.code13.javastack.spring.framework.aop.use")
  @EnableAspectJAutoProxy
  static class SpringAopUseConfig {}
}
