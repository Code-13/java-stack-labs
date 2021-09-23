/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.springlabs.utils;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.ClassUtils;

/**
 * ClassUtilsExample.
 *
 * <p>https://juejin.cn/post/7009919121210998815
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/23 10:57
 */
@DisplayName("spring-core utils ClassUtils 示例")
class ClassUtilsExample {

  @Test
  @DisplayName("""
      ClassLoader overrideThreadContextClassLoader(@Nullable ClassLoader classLoaderToUse) 
      获取当前线程上下文的类加载器
      """)
  void getDefaultClassLoader() {
    var defaultClassLoader = ClassUtils.getDefaultClassLoader();
    print(defaultClassLoader);
  }

  @Test
  @DisplayName("用特定的类加载器覆盖当前线程上下文的类加载器")
  void overrideThreadContextClassLoader() {
    print(ClassUtils.getDefaultClassLoader());
    ClassUtils.overrideThreadContextClassLoader(ClassLoader.getSystemClassLoader().getParent());
    print(ClassUtils.getDefaultClassLoader());
  }

  @Test
  @DisplayName("""
      forName(String name, @Nullable ClassLoader classLoader)
      通过类名返回类实例，类似于Class.forName()，但功能更强，可以用于原始类型，内部类等
      """)
  void forName() throws ClassNotFoundException {
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    print(ClassUtils.forName("int", classLoader));
    print(ClassUtils.forName("java.lang.String[]", classLoader));
    print(ClassUtils.forName("java.lang.Thread$State", classLoader));
  }

  @Test
  @DisplayName("""
      boolean isPresent(String className, @Nullable ClassLoader classLoader)
      判断当前classLoader是否包含目标类型（包括它的所有父类和接口）
      """)
  void isPresent() {
    ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
    print(ClassUtils.isPresent("int", classLoader));
    print(ClassUtils.isPresent("intt", classLoader));
  }

  @Test
  @DisplayName("Class<?> resolvePrimitiveClassName(@Nullable String name) 过给定类名获取原始类")
  void resolvePrimitiveClassName() {
    print(ClassUtils.resolvePrimitiveClassName("int"));
    print(ClassUtils.resolvePrimitiveClassName("java.lang.Integer"));
  }

  @Test
  @DisplayName("""
      boolean isPrimitiveWrapper(Class<?> clazz)
      判断给定类是否为包装类，如Boolean, Byte, Character, Short, Integer, Long, Float, Double 或者 Void
      """)
  void isPrimitiveWrapper() {
    print(ClassUtils.isPrimitiveWrapper(Integer.class));
    print(ClassUtils.isPrimitiveWrapper(Character.class));
    print(ClassUtils.isPrimitiveWrapper(Void.class));
    print(ClassUtils.isPrimitiveWrapper(String.class));

    // 类似的方法还有
    // isPrimitiveOrWrapper判断是否为原始类或者包装类、
    // isPrimitiveWrapperArray判断是否为包装类数组、
    // isPrimitiveArray判断是否为原始类数组。
  }

  @Test
  @DisplayName("""
      Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) 
      如果给定类是原始类，则返回对应包装类，否则直接返回给定类
      """)
  void resolvePrimitiveIfNecessary() {
    print(ClassUtils.resolvePrimitiveIfNecessary(int.class));
    print(ClassUtils.resolvePrimitiveIfNecessary(Object.class));
  }

  @Test
  @DisplayName("""
      boolean isAssignable(Class<?> lhsType, Class<?> rhsType)
      通过反射检查，是否可以将rhsType赋值给lhsType（注意，包装类型可以赋值给相应的原始类型，自动拆装箱机制）
      """)
  void isAssignable() {
    print(ClassUtils.isAssignable(Integer.class, int.class));
    print(ClassUtils.isAssignable(Object.class, String.class));
    print(
        ClassUtils.isAssignable(
            BeanPostProcessor.class, InstantiationAwareBeanPostProcessor.class));
    print(ClassUtils.isAssignable(double.class, Double.class)); // consider this
    print(ClassUtils.isAssignable(Integer.class, Long.class));
  }
  
  @Test
  @DisplayName("""
      boolean isAssignableValue(Class<?> type, @Nullable Object value)
      判断给定的值是否符合给定的类型
      """)
  void isAssignableValue() {
    print(ClassUtils.isAssignableValue(Integer.class, 1));
    print(ClassUtils.isAssignableValue(Integer.class, 1L));
    print(ClassUtils.isAssignableValue(int.class,  Integer.valueOf(1)));
    print(ClassUtils.isAssignableValue(Object.class, 1));
    print(ClassUtils.isAssignableValue(String.class, 1));
  }

  @Test
  @DisplayName("String convertResourcePathToClassName(String resourcePath)将类路径转换为全限定类名：")
  void convertResourcePathToClassName() {
    print(ClassUtils.convertResourcePathToClassName("java/lang/String"));
  }
  
  @Test
  @DisplayName("String classNamesToString(Class<?>... classes)")
  void classNamesToString() {
    print(ClassUtils.classNamesToString(String.class, Integer.class, BeanPostProcessor.class));
  }
  
  @Test
  @DisplayName("Class<?>[] getAllInterfaces(Object instance)返回给定实例对象所实现接口类型集合")
  void getAllInterfaces() {
    AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
    Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(processor);
    Arrays.stream(allInterfaces).forEach(ClassUtilsExample::print);
  }
  
  @Test
  @DisplayName("""
      Class<?> determineCommonAncestor(@Nullable Class<?> clazz1, @Nullable Class<?> clazz2)
      
      寻找给定类型的共同祖先（所谓共同祖先指的是给定类型调用class.getSuperclass获得的共同类型，
      如果给定类型是Object.class，接口，原始类型或者Void，直接返回null）
      """)
   void determineCommonAncestor() {
    // 它两都是接口
    print(ClassUtils.determineCommonAncestor(
        AutowireCapableBeanFactory.class, ListableBeanFactory.class));
    print(ClassUtils.determineCommonAncestor(Long.class, Integer.class));
    print(ClassUtils.determineCommonAncestor(String.class, Integer.class));
  }
  
  @Test
  @DisplayName("boolean isInnerClass(Class<?> clazz)判断给定类型是否为内部类（非静态）")
  void isInnerClass() {
    class A {
      class B {

      }
    }

    print(ClassUtils.isInnerClass(A.B.class));
  }
  
  @Test
  @DisplayName("boolean isCglibProxy(Object object)是否为Cglib代理对象")
  void isCglibProxy() {

  }
  
  private static void print(Object value) {
    System.out.println(value);
  }
}