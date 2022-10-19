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

package io.github.code13.javastack.javalabs.reflect.methodhandler;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MethodHandlerRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/19 13:30
 */
public class MethodHandlerRunner {

  @Test
  @DisplayName("test1")
  void test1() {

    //    创建提供对公共方法的访问的查找
    Lookup publicLookup = MethodHandles.publicLookup();

    //    希望访问私有方法和受保护的方法，则可以使用lookup（）方法：
    Lookup lookup = MethodHandles.lookup();

    MethodType methodType = MethodType.methodType(List.class, Object[].class);
  }

  @Test
  @DisplayName("findVirtual / 使用findVirtual（）方法允许我们为对象方法创建MethodHandle。让我们基于String类的concat（）方法创建一个")
  void findVirtual() throws NoSuchMethodException, IllegalAccessException {
    Lookup publicLookup = MethodHandles.publicLookup();
    MethodType mt = MethodType.methodType(String.class, String.class);
    MethodHandle concatMh = publicLookup.findVirtual(String.class, "concat", mt);
  }

  @Test
  @DisplayName("findStatic/ 当我们想访问静态方法时，可以改用findStatic（）方法：")
  void findStatic() throws NoSuchMethodException, IllegalAccessException {
    Lookup publicLookup = MethodHandles.publicLookup();
    MethodType methodType = MethodType.methodType(List.class, Object[].class);
    MethodHandle asLitMh = publicLookup.findStatic(Arrays.class, "asLit", methodType);
  }

  @Test
  @DisplayName("findConstructor / 可以使用findConstructor（）方法来访问构造函数。")
  void findConstructor() throws NoSuchMethodException, IllegalAccessException {
    Lookup publicLookup = MethodHandles.publicLookup();
    MethodType methodType = MethodType.methodType(void.class, String.class);
    MethodHandle constructorMh = publicLookup.findConstructor(Integer.class, methodType);
  }

  @Test
  @DisplayName("findField / 使用方法句柄，还可以访问字段。")
  void findField() throws NoSuchFieldException, IllegalAccessException {
    Lookup publicLookup = MethodHandles.publicLookup();
    MethodHandle titleMh = publicLookup.findGetter(Book.class, "title", String.class);
  }

  @Test
  @DisplayName("findPrivate / 可以在java.lang.reflect API 的帮助下为私有方法创建方法句柄。")
  void findPrivate() throws NoSuchMethodException, IllegalAccessException {
    Method method = Book.class.getDeclaredMethod("formatBook");
    method.setAccessible(true);
    MethodHandle formatBookMh = MethodHandles.lookup().unreflect(method);
  }

  record Book(String id, String title) {
    private String formatBook() {
      return id + " > " + title;
    }
  }
}
