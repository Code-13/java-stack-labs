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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Examples for {@link org.springframework.core.annotation.MergedAnnotation}.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/16 20:38
 */
public class MergedAnnotation_Runner {

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

  @Driver(car = "Unknown", name = "Unknown")
  private interface Drivable {}

  private static class Bmw implements Drivable {}

  private static class X5 extends Bmw {}

  @Driver(car = "X7", name = "X7")
  private static class X7 extends Bmw {}

  @Test
  @DisplayName("factoryMethods")
  void factoryMethods() {
    Driver driver = X7.class.getAnnotation(Driver.class);
    MergedAnnotation<Driver> mergedAnnotation = MergedAnnotation.from(driver);
    System.out.println(mergedAnnotation);
    System.out.println(mergedAnnotation.synthesize());

    mergedAnnotation = MergedAnnotation.from(null, driver);
    System.out.println(mergedAnnotation);
    System.out.println(mergedAnnotation.synthesize());

    mergedAnnotation = MergedAnnotation.of(Driver.class);
    System.out.println(mergedAnnotation);

    mergedAnnotation = MergedAnnotation.of(Driver.class, null);
    System.out.println(mergedAnnotation);
    //    System.out.println(mergedAnnotation.synthesize());

    mergedAnnotation = MergedAnnotation.of(null, Driver.class, null);
    System.out.println(mergedAnnotation);
    //    System.out.println(mergedAnnotation.synthesize());

    mergedAnnotation = MergedAnnotation.of(null, null, Driver.class, null);
    System.out.println(mergedAnnotation);
    //    System.out.println(mergedAnnotation.synthesize());
  }
}
