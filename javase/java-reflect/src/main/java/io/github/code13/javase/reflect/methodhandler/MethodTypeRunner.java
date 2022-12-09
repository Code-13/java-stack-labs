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

package io.github.code13.javase.reflect.methodhandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MethodTypeRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/19 14:42
 */
public class MethodTypeRunner {

  // 创建

  @Test
  @DisplayName("genericMethodType")
  void genericMethodType() {

    /*
     * 该方法会创建一个参数值及返回值类型都是Object的特殊方法类型，
     * 使用时只需要指定参数个数，以及最后一个参数是否是Object[]的参数两个部分就可以了
     */

    MethodType methodType = MethodType.genericMethodType(2);
  }

  @Test
  @DisplayName("methodType")
  void methodType() {

    /*
     * 该方法显示指定参数类型及返回值类型，
     * 第一个参数用于指定方法的返回值类型，如果是表示静态方法的，第一个可以不填。
     * 后面的参数用于指定表示方法的参数类型，接受不定长参数
     */

    MethodType.methodType(int.class, int.class, int.class);
  }

  @Test
  @DisplayName("fromMethodDescriptorString")
  void fromMethodDescriptorString() throws NoSuchMethodException, IllegalAccessException {

    /*
     * 该方法接受一个代表方法返回值类型和参数类型的字符串，将会解析这个字符串并生成对应的方法类型实例，第二个参数可以为null
     */

    MethodType type = MethodType.fromMethodDescriptorString("(II)I", null);
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle maxHandle = lookup.findStatic(Math.class, "max", type);
  }
}
