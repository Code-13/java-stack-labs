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

package io.github.code13.javastack.javalabs.agent.demo03_bytebuddy_monitor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

/**
 * MethodCostTime.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/13 23:13
 */
public class MethodCostTime {

  @RuntimeType
  public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable)
      throws Exception {
    long start = System.currentTimeMillis();
    try {
      return callable.call();
    } finally {
      System.out.println(method + " 方法耗时:  " + (System.currentTimeMillis() - start) + "ms");
    }
  }
}
