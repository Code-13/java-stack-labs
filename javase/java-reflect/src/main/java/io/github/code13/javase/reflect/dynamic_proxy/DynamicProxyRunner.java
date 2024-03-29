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

package io.github.code13.javase.reflect.dynamic_proxy;

import java.lang.reflect.Proxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DynamicProxyRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/9 15:59
 */
public class DynamicProxyRunner {

  @Test
  @DisplayName("helloServiceProxy")
  void helloServiceProxy() {

    HelloService helloService =
        (HelloService)
            Proxy.newProxyInstance(
                HelloService.class.getClassLoader(),
                new Class[] {HelloService.class},
                new HelloInvocationHandler(new HelloServiceImpl()));

    helloService.sayHello();
    // method :sayHello is invoked!
    // hello
  }
}
