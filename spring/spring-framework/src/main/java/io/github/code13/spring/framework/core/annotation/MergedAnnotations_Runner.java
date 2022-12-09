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

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotationFilter;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.core.annotation.RepeatableContainers;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Examples for {@link org.springframework.core.annotation.MergedAnnotations}.
 *
 * @see org.springframework.core.annotation.MergedAnnotations
 * @see org.springframework.core.annotation.MergedAnnotation
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/16 10:18
 */
public class MergedAnnotations_Runner {

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

  /**
   * 创建一个新的MergedAnnotations实例，其中包含来自指定元素的所有注释和元注释，以及相关的继承元素，具体取决于MergedAnnotations.SearchStrategy
   *
   * <p>{@link MergedAnnotations#from(AnnotatedElement)}
   *
   * <p>{@link MergedAnnotations#from(AnnotatedElement, SearchStrategy)}
   *
   * <p>{@link MergedAnnotations#from(AnnotatedElement, SearchStrategy, RepeatableContainers)}
   *
   * <p>{@link MergedAnnotations#from(AnnotatedElement, SearchStrategy, RepeatableContainers,
   * AnnotationFilter)}
   */
  @Test
  @DisplayName("factoryMethod")
  void factoryMethod() {
    MergedAnnotations annotations =
        MergedAnnotations.from(
            X7.class,
            SearchStrategy.TYPE_HIERARCHY,
            RepeatableContainers.standardRepeatables(),
            AnnotationFilter.PLAIN);

    annotations.forEach(
        annotationMergedAnnotation ->
            System.out.println(
                "annotationMergedAnnotation.getType().getName() = "
                    + annotationMergedAnnotation.getType().getName()));
  }

  @Test
  @DisplayName("test")
  void test() {
    MergedAnnotations annotations =
        MergedAnnotations.from(
            X5.class,
            SearchStrategy.TYPE_HIERARCHY,
            RepeatableContainers.standardRepeatables(),
            AnnotationFilter.PLAIN);

    boolean present = annotations.isPresent(Driver.class);
    assertTrue(present);

    present = annotations.isPresent(Driver.class.getName());
    assertTrue(present);

    boolean directlyPresent = annotations.isDirectlyPresent(Driver.class);
    assertTrue(directlyPresent);

    directlyPresent = annotations.isDirectlyPresent(Driver.class.getName());
    assertTrue(directlyPresent);
  }

  @Test
  @DisplayName("testGetMethods")
  void testGetMethods() {
    MergedAnnotations annotations =
        MergedAnnotations.from(
            X5.class,
            SearchStrategy.TYPE_HIERARCHY,
            RepeatableContainers.standardRepeatables(),
            AnnotationFilter.PLAIN);

    // Get 获取的都是 MergedAnnotation

    System.out.println(
        annotations.get(Driver.class.getName(), MergedAnnotation::isPresent).synthesize());

    String car = annotations.get(Driver.class).getString("car");
    System.out.println(car);
  }
}
