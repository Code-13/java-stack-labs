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

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * CglibMethodInterceptor.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/12 10:57
 */
public class CglibMethodInterceptor implements MethodInterceptor {

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    //    System.out.println(o);
    //    System.out.println(method);
    //    System.out.println(Arrays.toString(objects));

    System.out.println("======插入前置通知======");

    Object result = methodProxy.invokeSuper(o, objects);

    System.out.println("======插入后者通知======");

    System.out.println();

    return result;
  }
}
