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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Examples for {@link org.springframework.core.annotation.AnnotatedElementUtils}.
 *
 * <p>General utility methods for finding annotations, meta-annotations, and repeatable annotations
 * on AnnotatedElements.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/16 12:22
 */
public class AnnotatedElementUtils_Runner {

  @Target({ElementType.METHOD, ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  @RequestMapping // 特意注解上放一个注解，方面测试看结果
  @Inherited
  private @interface Driver {

    @AliasFor(attribute = "name")
    String value() default "I am old driver";

    Class<? extends Number> clazz() default Integer.class;
    // 注解类型的属性
    Component anno() default @Component;

    @AliasFor(attribute = "value")
    String name() default "I am old driver";

    String car();
  }

  @Driver(car = "Benz", name = "Benz")
  private static class Benz {}

  private static class A7 extends Benz {}

  @Driver(car = "Benz-A8", name = "A8")
  private static class A8 extends Benz {}

  @Driver(car = "Unknown", name = "Unknown")
  private interface Drivable {}

  private static class Bmw implements Drivable {}

  private static class X5 extends Bmw {}

  @Driver(car = "X7", name = "X7")
  private static class X7 extends Bmw {}

  /*
   * 底层调用 getAnnotations 默认 SearchStrategy.INHERITED_ANNOTATIONS，所以不会搜寻接口
   */

  /**
   * {@link AnnotatedElementUtils#getMetaAnnotationTypes(AnnotatedElement, String)} {@link
   * AnnotatedElementUtils#getMetaAnnotationTypes(AnnotatedElement, Class)} {@link
   * AnnotatedElementUtils#getMetaAnnotationTypes(AnnotatedElement, Annotation)}
   *
   * <p>返回指定 element 上的类型为 annotationType 的 annotation 的所有元注解 (不包含 java 的元注解)
   */
  @Test
  @DisplayName("testGetMetaAnnotationTypes")
  void testGetMetaAnnotationTypes() {

    Set<String> metaAnnotationTypes =
        AnnotatedElementUtils.getMetaAnnotationTypes(A7.class, Driver.class.getName());
    System.out.println(metaAnnotationTypes);
    // [org.springframework.web.bind.annotation.RequestMapping,
    // org.springframework.web.bind.annotation.Mapping]

    metaAnnotationTypes = AnnotatedElementUtils.getMetaAnnotationTypes(A7.class, Driver.class);
    System.out.println(metaAnnotationTypes);
    // [org.springframework.web.bind.annotation.RequestMapping,
    // org.springframework.web.bind.annotation.Mapping]

  }

  /**
   * {@link AnnotatedElementUtils#hasMetaAnnotationTypes(AnnotatedElement, Class)} {@link
   * AnnotatedElementUtils#hasMetaAnnotationTypes(AnnotatedElement, String)}
   */
  @Test
  @DisplayName("testHasMetaAnnotationTypes")
  void testHasMetaAnnotationTypes() {

    // X5 的 Driver 注解是直接继承过来的，所以不是元注解
    boolean hasMetaAnnotationTypes =
        AnnotatedElementUtils.hasMetaAnnotationTypes(A7.class, Driver.class);
    assertFalse(hasMetaAnnotationTypes);

    // RequestMapping 是 Driver 的注解，是元注解
    hasMetaAnnotationTypes =
        AnnotatedElementUtils.hasMetaAnnotationTypes(A7.class, RequestMapping.class);
    assertTrue(hasMetaAnnotationTypes);
  }

  /**
   * {@link AnnotatedElementUtils#isAnnotated(AnnotatedElement, Class)} {@link
   * AnnotatedElementUtils#isAnnotated(AnnotatedElement, String)}
   */
  @Test
  @DisplayName("testIsAnnotated")
  void testIsAnnotated() {
    boolean annotated = AnnotatedElementUtils.isAnnotated(A7.class, Driver.class);
    assertTrue(annotated);

    annotated = AnnotatedElementUtils.isAnnotated(A7.class, RequestMapping.class);
    assertTrue(annotated);

    annotated = AnnotatedElementUtils.isAnnotated(Bmw.class, RequestMapping.class);
    assertFalse(annotated);
  }

  /**
   * {@link AnnotatedElementUtils#getMergedAnnotationAttributes(AnnotatedElement, Class)} {@link
   * AnnotatedElementUtils#getMergedAnnotationAttributes(AnnotatedElement, String)} {@link
   * AnnotatedElementUtils#getMergedAnnotationAttributes(AnnotatedElement, String, boolean,
   * boolean)}
   *
   * <p>classValuesAsString：true表示把Class类型的都转换为String类型；默认false
   *
   * <p>nestedAnnotationsAsMap:true表示连内嵌的注解也解析出来（默认false）
   */
  @Test
  @DisplayName("testGetMergedAnnotationAttributes")
  void testGetMergedAnnotationAttributes() {
    AnnotationAttributes attributes =
        AnnotatedElementUtils.getMergedAnnotationAttributes(A7.class, Driver.class);
    System.out.println(attributes);
    // {anno=@org.springframework.stereotype.Component(""), car=Benz, clazz=class java.lang.Integer,
    // name=Benz, value=Benz}

    attributes =
        AnnotatedElementUtils.getMergedAnnotationAttributes(A7.class, Driver.class.getName());
    System.out.println(attributes);
    // {anno=@org.springframework.stereotype.Component(""), car=Benz, clazz=class java.lang.Integer,
    // name=Benz, value=Benz}

    attributes =
        AnnotatedElementUtils.getMergedAnnotationAttributes(
            A7.class, Driver.class.getName(), true, true);
    System.out.println(attributes);
    // {anno={value=}, car=Benz, clazz=java.lang.Integer, name=Benz, value=Benz}
  }

  /** {@link AnnotatedElementUtils#getMergedAnnotation(AnnotatedElement, Class)} */
  @Test
  @DisplayName("testGetMergedAnnotation")
  void testGetMergedAnnotation() {
    Driver driver = AnnotatedElementUtils.getMergedAnnotation(A7.class, Driver.class);
    System.out.println(driver);
  }

  @Test
  @DisplayName("testGetAllMergedAnnotations")
  void testGetAllMergedAnnotations() {
    Set<Driver> drivers = AnnotatedElementUtils.getAllMergedAnnotations(A8.class, Driver.class);
    System.out.println(drivers);
  }

  @Test
  @DisplayName("testGetAllMergedAnnotationAttributes")
  void testGetAllMergedAnnotationAttributes() {
    MultiValueMap<String, Object> attributes =
        AnnotatedElementUtils.getAllAnnotationAttributes(A7.class, Driver.class.getName());
    System.out.println(attributes);
    // {anno=[@org.springframework.stereotype.Component("")], car=[Benz], clazz=[class
    // java.lang.Integer], name=[Benz], value=[Benz]}

    attributes =
        AnnotatedElementUtils.getAllAnnotationAttributes(
            A7.class, Driver.class.getName(), false, true);
    System.out.println(attributes);
    // {anno=[{value=}], car=[Benz], clazz=[class java.lang.Integer], name=[Benz], value=[Benz]}

    attributes =
        AnnotatedElementUtils.getAllAnnotationAttributes(
            X7.class, Driver.class.getName(), false, true);
    System.out.println(attributes);
    // {anno=[{value=}], car=[X7], clazz=[class java.lang.Integer], name=[X7], value=[X7]}
  }

  /*
   * 底层调用 getAnnotations 默认 SearchStrategy.TYPE_HIERARCHY，所以会搜寻接口
   */

  @Test
  @DisplayName("testHasAnnotation")
  void testHasAnnotation() {

    boolean hasAnnotation = AnnotatedElementUtils.hasAnnotation(A7.class, Driver.class);
    assertTrue(hasAnnotation);

    hasAnnotation = AnnotatedElementUtils.hasAnnotation(Bmw.class, Driver.class);
    assertTrue(hasAnnotation);
  }

  @Test
  @DisplayName("testFindMergedAnnotationAttributes")
  void testFindMergedAnnotationAttributes() {
    AnnotationAttributes attributes =
        AnnotatedElementUtils.findMergedAnnotationAttributes(
            X5.class, Driver.class.getName(), true, true);
    System.out.println(attributes);
    // {anno={value=}, car=Unknown, clazz=java.lang.Integer, name=Unknown, value=Unknown}

    attributes =
        AnnotatedElementUtils.findMergedAnnotationAttributes(X5.class, Driver.class, true, true);
    System.out.println(attributes);
    // {anno={value=}, car=Unknown, clazz=java.lang.Integer, name=Unknown, value=Unknown}

    attributes =
        AnnotatedElementUtils.findMergedAnnotationAttributes(X7.class, Driver.class, true, true);
    System.out.println(attributes);
    // {anno={value=}, car=X7, clazz=java.lang.Integer, name=X7, value=X7}
  }

  @Test
  @DisplayName("testFindAllMergedAnnotations")
  void testFindAllMergedAnnotations() {
    Set<Driver> drivers = AnnotatedElementUtils.findAllMergedAnnotations(X5.class, Driver.class);
    System.out.println(drivers);
    // [@io.github.code13.javastack.spring.framework.core.annotation.AnnotatedElementUtils_Runner.Driver(anno=@org.springframework.stereotype.Component(""), car="Unknown", clazz=java.lang.Integer.class, name="Unknown", value="Unknown")]

    drivers = AnnotatedElementUtils.findAllMergedAnnotations(X7.class, Driver.class);
    System.out.println(drivers);
    // [@io.github.code13.javastack.spring.framework.core.annotation.AnnotatedElementUtils_Runner.Driver(anno=@org.springframework.stereotype.Component(""), car="Unknown", clazz=java.lang.Integer.class, name="Unknown", value="Unknown"), @io.github.code13.javastack.spring.framework.core.annotation.AnnotatedElementUtils_Runner.Driver(anno=@org.springframework.stereotype.Component(""), car="X7", clazz=java.lang.Integer.class, name="X7", value="X7")]
  }
}
