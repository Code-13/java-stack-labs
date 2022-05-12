/*
 *
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

package io.github.code13.javastack.spring.framework.aop.cglib;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

/**
 * SpringCglibRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/12 11:18
 */
public class SpringCglibRunner {

  @Test
  @DisplayName("test_cglib")
  void test_cglib() {
    // 代理类class文件存入本地磁盘方便我们反编译查看源码
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/u0039724/cglib");
    // 通过CGLIB动态代理获取代理对象的过程
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(HelloCglibService.class);
    enhancer.setCallback(new CglibMethodInterceptor());
    HelloCglibService proxy = (HelloCglibService) enhancer.create();
    proxy.sayHello();
  }
}
