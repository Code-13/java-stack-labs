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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MethodHandlerInvokeRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/19 13:47
 */
public class MethodHandlerInvokeRunner {

  Lookup publicLookup = MethodHandles.publicLookup();
  Lookup lookup = MethodHandles.lookup();

  /*
   * MethodHandle类提供了三种执行方法句柄的方法：invoke（），invokeWithArugments（）和invokeExact（）。
   */

  /**
   * 当使用invoke（）方法时，我们强制执行固定数量的参数（arity），但允许对参数和返回类型进行强制转换和装箱/拆箱。
   *
   * <p>让我们看一下如何在框内的参数中使用invoke（）：
   */
  @Test
  @DisplayName("invoke")
  void invoke() throws Throwable {
    MethodType methodType = MethodType.methodType(String.class, char.class, char.class);
    MethodHandle replaceMh = publicLookup.findVirtual(String.class, "replace", methodType);

    String res = (String) replaceMh.invoke("jovo", Character.valueOf('o'), 'a');

    assertEquals("java", res);
  }

  /**
   * 使用invokeWithArguments方法调用方法句柄是这三个选项中限制最少的。
   *
   * <p>实际上，除了对参数和返回类型进行强制转换和装箱/拆箱外，它还允许可变参数数组传入作为方法参数集合调用。
   */
  @Test
  @DisplayName("invokeWithArguments")
  void invokeWithArguments() throws Throwable {
    MethodType mt = MethodType.methodType(List.class, Object[].class);
    MethodHandle asList = publicLookup.findStatic(Arrays.class, "asList", mt);

    List<Integer> integers = (List<Integer>) asList.invokeWithArguments(1, 2);

    assertThat(Arrays.asList(1, 2), Is.is(integers));
  }

  /** 如果我们希望在执行方法句柄（参数数量及其类型）方面受到更多限制，则必须使用invokeExact（）方法。 实际上，它不提供对提供的类的任何强制转换，并且需要固定数量的参数。 */
  @Test
  @DisplayName("invokeExact")
  void invokeExact() throws Throwable {
    //    让我们看看如何使用方法句柄求和两个int值：
    MethodType methodType = MethodType.methodType(int.class, int.class, int.class);
    MethodHandle sumMh = publicLookup.findStatic(Integer.class, "sum", methodType);
    int i = (int) sumMh.invokeExact(1, 2);

    assertEquals(3, i);
  }
}
