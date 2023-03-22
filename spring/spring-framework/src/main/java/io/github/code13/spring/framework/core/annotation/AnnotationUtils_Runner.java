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

package io.github.code13.spring.framework.core.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Examples for {@link org.springframework.core.annotation.AnnotationUtils}.
 *
 * @see org.springframework.core.annotation.AnnotationUtils
 * @see org.springframework.core.annotation.MergedAnnotations
 * @see org.springframework.core.annotation.MergedAnnotation
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/15 22:04
 */
public class AnnotationUtils_Runner {

  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @RequestMapping // 特意注解上放一个注解，方面测试看结果
  @Inherited
  private @interface Driver {
    String value() default "I am old driver";

    Class<? extends Number> clazz() default Integer.class;
    // 注解类型的属性
    Component anno() default @Component;

    String bbb();
  }

  @Driver(bbb = "111")
  private interface Driveable {}

  private static class Bmw implements Driveable {}

  private static class X3 extends Bmw {}

  // ==============================
  //      getAnnotation start
  // ==============================

  /**
   * 从提供的注解(annotation)中获取annotationType的单个注释：给定annotation本身或其直接元注解。
   *
   * <p>{@link AnnotationUtils#getAnnotation(Annotation, Class)}
   *
   * <p>{@link AnnotationUtils#getAnnotation(AnnotatedElement, Class)}
   *
   * <p>{@link AnnotationUtils#getAnnotation(Method, Class)}
   */
  @Test
  @DisplayName("testGetAnnotation")
  void testGetAnnotation() {
    Driver driver = Driveable.class.getAnnotation(Driver.class);

    // 获取 annotation 本身，相当于就会被Spring代理了 (具体会不会代理请查看 {@link MergedAnnotation#synthesize})
    Driver driver1 = AnnotationUtils.getAnnotation(driver, Driver.class);
    assertSame(driver1, driver);

    // 这样前后类型不一致的话，会把这个注解上面的注解给获取出来; 也就是获取直接元注解
    RequestMapping requestMapping = AnnotationUtils.getAnnotation(driver, RequestMapping.class);
    assertNotNull(requestMapping);

    // 如果获取的是注解的元注解的元注解的话(非直接元注解)，是获取不到的
    Documented documented = AnnotationUtils.getAnnotation(driver, Documented.class);
    assertNull(documented);

    // annotatedElement 也就是 annotation.annotationType()
    RequestMapping requestMapping1 =
        AnnotationUtils.getAnnotation(driver.annotationType(), RequestMapping.class);
    assertNotNull(requestMapping1);
  }

  /**
   * 获取提供的AnnotatedElement上存在的所有注解。 已废弃，请使用 MergedAnnotations
   *
   * <p>{@link AnnotationUtils#getAnnotations(AnnotatedElement)}
   *
   * @see org.springframework.core.annotation.MergedAnnotations
   * @see MergedAnnotations_Runner
   */
  @Test
  @DisplayName("testGetAnnotations")
  void testGetAnnotations() {
    Driver driver = Driveable.class.getAnnotation(Driver.class);

    // 获取 X3 上的所有注解, 该方法 Deprecated，使用 MergedAnnotations Api
    Annotation[] annotations = AnnotationUtils.getAnnotations(X3.class);
    assertNotNull(annotations);
    assertEquals(0, annotations.length);

    // 默认的 SearchStrategy 是 SearchStrategy.Direct; 所以搜寻不到。
    // 关于 MergedAnnotations 的使用详情，查看 MergedAnnotations_Runner
    long count = MergedAnnotations.from(X3.class).stream().count();
    assertEquals(0L, count);

    // 获取 Driver 上的所有注解
    annotations = AnnotationUtils.getAnnotations(driver.annotationType());
    assertNotNull(annotations);
    assertEquals(4, annotations.length);
    System.out.println(Arrays.toString(annotations));
  }

  // ==============================
  //       findAnnotation
  // ==============================

  /**
   * 在提供的类上查找annotationType的单个Annotation，如果该Annotation不直接存在于给定类本身，则遍历其接口、注释和超类。
   * 此方法显式处理未声明为继承的类级注释以及接口上的元注释和注释。
   *
   * <p>算法操作如下：#1在给定类上搜索注释，如果找到则返回。 #2递归搜索给定类声明的所有注释。 #3递归搜索给定类声明的所有接口。 #4递归搜索给定类的超类层次结构。
   * 注意：在此上下文中，术语递归地表示搜索过程通过返回到步骤#1继续，将当前接口、注释或超类作为查找注释的类。
   *
   * <p>{@link AnnotationUtils#findAnnotation(AnnotatedElement, Class)}
   *
   * <p>{@link AnnotationUtils#findAnnotation(Class, Class)}
   *
   * <p>{@link AnnotationUtils#findAnnotation(Method, Class)}
   */
  @Test
  @DisplayName("testFindAnnotation")
  void testFindAnnotation() {
    // 本来，接口上的注解我们无论如何都继承不了了，但是这个方法就可以
    Driver driver = AnnotationUtils.findAnnotation(X3.class, Driver.class);
    assertNotNull(driver);
  }

  /**
   * 在指定的clazz （包括指定的clazz本身）的继承层次中找到第一个直接存在指定annotationType的注解的Class。
   * 如果提供的clazz是接口，则只检查接口本身；不会遍历接口的继承层次结构。 不会搜索元注释。
   *
   * <p><strong>标准的Class API 不提供一种机制来确定继承层次结构中的哪个类实际声明了Annotation ，因此我们需要明确地处理这个问题</strong>
   *
   * <p>{@link AnnotationUtils#findAnnotationDeclaringClass(Class, Class)}
   *
   * <p>{@link AnnotationUtils#findAnnotationDeclaringClassForTypes(List, Class)}}
   */
  @Test
  @DisplayName("testFindAnnotationDeclaringClass")
  void testFindAnnotationDeclaringClass() {

    // 因为本身和父类都没有，接口不找，所以为 null
    Class<?> clazz = AnnotationUtils.findAnnotationDeclaringClass(Driver.class, X3.class);
    assertNull(clazz);

    // 接口只查找自身并且标注，所以找到
    clazz = AnnotationUtils.findAnnotationDeclaringClass(Driver.class, Driveable.class);
    assertNotNull(clazz);
    assertSame(clazz, Driveable.class);

    // 与 findAnnotationDeclaringClass 的区别就是在 annotationTypes 中至少有一个找到就返回
    clazz =
        AnnotationUtils.findAnnotationDeclaringClassForTypes(
            List.of(Driver.class, Order.class), X3.class);
    assertNull(clazz);

    clazz =
        AnnotationUtils.findAnnotationDeclaringClassForTypes(
            List.of(Driver.class, Order.class), Driveable.class);
    assertNotNull(clazz);
    assertSame(clazz, Driveable.class);
  }

  /**
   * {@link AnnotationUtils#isAnnotationDeclaredLocally(Class, Class)}
   *
   * <p>确定指定annotationType的注释是否在提供的clazz上本地声明（即直接存在）。 提供的类可以表示任何类型。不会搜索元注释。 注意：此方法不确定注释是否被继承。
   */
  @Test
  @DisplayName("testIsAnnotationDeclaredLocally")
  void testIsAnnotationDeclaredLocally() {
    // 本来，接口上的注解我们无论如何都继承不了了，但用了Spring的，就可以
    Driver driver = AnnotationUtils.findAnnotation(X3.class, Driver.class);

    // 简单的说就是自己本身Class是否含有指定的这个注解
    assertTrue(
        AnnotationUtils.isAnnotationDeclaredLocally(driver.annotationType(), Driveable.class));
    assertFalse(AnnotationUtils.isAnnotationDeclaredLocally(driver.annotationType(), Bmw.class));
    assertFalse(AnnotationUtils.isAnnotationDeclaredLocally(driver.annotationType(), X3.class));
  }

  /**
   * {@link AnnotationUtils#isAnnotationInherited(Class, Class)}
   *
   * <p>确定指定的annotationType的注释是否存在于提供的clazz上并被继承（即不直接存在）。 不会搜索元注释。如果提供的clazz是一个接口，则只检查接口本身。
   * 根据Java中的标准元注释语义，不会遍历接口的继承层次结构。
   *
   * <p>有关注释继承的更多详细信息，请参见@Inherited元注释的javadoc。
   *
   * @see java.lang.annotation.Inherited
   */
  @Test
  @DisplayName("testIsAnnotationInherited")
  void testIsAnnotationInherited() {

    boolean annotationInherited = AnnotationUtils.isAnnotationInherited(Driver.class, Bmw.class);
    assertFalse(annotationInherited);

    annotationInherited = AnnotationUtils.isAnnotationInherited(Driver.class, X3.class);
    assertFalse(annotationInherited);
  }

  /**
   * {@link AnnotationUtils#isAnnotationMetaPresent(Class, Class)}
   *
   * <p>确定提供的annotationType上是否存在metaAnnotationType类型的注释。
   */
  @Test
  @DisplayName("testIsAnnotationMetaPresent")
  void testIsAnnotationMetaPresent() {
    boolean annotationMetaPresent =
        AnnotationUtils.isAnnotationMetaPresent(Driver.class, RequestMapping.class);
    assertTrue(annotationMetaPresent);

    annotationMetaPresent = AnnotationUtils.isAnnotationMetaPresent(Driver.class, Component.class);
    assertFalse(annotationMetaPresent);
  }

  /**
   * {@link AnnotationUtils#isInJavaLangAnnotationPackage(Annotation)} {@link
   * AnnotationUtils#isInJavaLangAnnotationPackage(String)}
   *
   * <p>是否是JDK的注解
   */
  @Test
  @DisplayName("isInJavaLangAnnotationPackage")
  void testisInJavaLangAnnotationPackage() {
    boolean inJavaLangAnnotationPackage =
        AnnotationUtils.isInJavaLangAnnotationPackage(Driveable.class.getAnnotation(Driver.class));
    assertFalse(inJavaLangAnnotationPackage);

    inJavaLangAnnotationPackage =
        AnnotationUtils.isInJavaLangAnnotationPackage(Driver.class.getAnnotation(Retention.class));
    assertTrue(inJavaLangAnnotationPackage);
  }

  /**
   * {@link AnnotationUtils#getAnnotationAttributes(AnnotatedElement, Annotation, boolean, boolean)}
   *
   * <p>以AnnotationAttributes映射的形式检索给定 annotation 的属性。此方法提供了完全递归的注释读取功能
   *
   * <p>annotatedElement表示被标注了后面这个注解的元素，如果不知道，就传null吧
   * classValuesAsString：true表示把Class类型的都转换为String类型；默认false
   * nestedAnnotationsAsMap:true表示连内嵌的注解也解析出来（默认false）
   */
  @Test
  @DisplayName("testGetAnnotationAttributes")
  void testGetAnnotationAttributes() {
    Driver driver = AnnotationUtils.findAnnotation(X3.class, Driver.class);
    assertNotNull(driver);

    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(driver);
    System.out.println(annotationAttributes); // 此处 class 是 Class 类型，anno 没有被解析
    // {anno=@org.springframework.stereotype.Component(""), clazz=class java.lang.Integer,
    //  value=I am old driver}

    annotationAttributes = AnnotationUtils.getAnnotationAttributes(driver, true, true);
    System.out.println(annotationAttributes); // 此处 class 是 string，且 anno 已经被解析
    // {anno={value=}, clazz=java.lang.Integer, value=I am old driver}

    annotationAttributes = AnnotationUtils.getAnnotationAttributes(driver, false, true);
    System.out.println(annotationAttributes); // 此处 class 是 Class，且 anno 已经被解析
    // {anno={value=}, clazz=class java.lang.Integer, value=I am old driver}

    // annotatedElement 传 null 也可以
    annotationAttributes = AnnotationUtils.getAnnotationAttributes(null, driver, true, true);
    System.out.println(annotationAttributes); // 此处 class 是 string，且 anno 已经被解析
    // {anno={value=}, clazz=java.lang.Integer, value=I am old driver}
  }

  // ==============================
  //      getValue
  // ==============================

  /**
   * {@link AnnotationUtils#getValue(Annotation)}
   *
   * <p>{@link AnnotationUtils#getValue(Annotation, String)}
   *
   * <p>{@link AnnotationUtils#getDefaultValue(Annotation)}
   *
   * <p>{@link AnnotationUtils#getDefaultValue(Annotation, String)}
   *
   * <p>{@link AnnotationUtils#getDefaultValue(Class)}
   *
   * <p>{@link AnnotationUtils#getDefaultValue(Class, String)}
   *
   * <p>{@link AnnotationUtils#getDefaultValues(Class)}
   */
  @Test
  @DisplayName("testGetValue")
  void testGetValue() {
    Driver driver = AnnotationUtils.findAnnotation(X3.class, Driver.class);
    assertNotNull(driver);

    Object value = AnnotationUtils.getValue(driver);
    System.out.println(value); // I am old driver

    value = AnnotationUtils.getValue(driver, "clazz");
    System.out.println(value); // class java.lang.Integer

    // 不存在 aaa 属性，返回 null
    value = AnnotationUtils.getValue(driver, "aaa");
    System.out.println(value); // null

    // getDefaultValue 与 getValue 比只拿默认值。若没有设置默认值，那就返回null

    value = AnnotationUtils.getDefaultValue(driver, "bbb");
    System.out.println(value); // null
  }
}
