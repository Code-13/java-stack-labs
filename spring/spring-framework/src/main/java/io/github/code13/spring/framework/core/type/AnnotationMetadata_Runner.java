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

package io.github.code13.spring.framework.core.type;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * AnnotationMetadata_Runner.
 *
 * @see org.springframework.core.type.AnnotationMetadata
 * @see org.springframework.core.type.StandardAnnotationMetadata
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 21:08
 */
public class AnnotationMetadata_Runner {

  /*
   * 注解元数据，同时继承了 ClassMetadata 和 AnnotatedTypeMetadata，还拓展了以下方法：
   *
   * 返回目标上所有 直接注解 的全类名集合
   * 返回目标指定注解上所有 元注解 的全类名集合
   * 目标是否被指定 直接注解 标注
   * 目标是否被指定 元注解 标注
   * 目标是否含有被 直接注解或元注解标注的方法
   * 返回上述方法的 MethodMetadata 集合
   */

  /*
   // 返回目标所有 直接注解 全限定名的集合
   Set<String> getAnnotationTypes();

   // 返回目标指定注解上 元注解 的全限定名集合
   Set<String> getMetaAnnotationTypes(String annotationName);

   // 是否被指定 直接注解 标注
   boolean hasAnnotation(String annotationName);

   // 是否被 指定元注解 标注
   boolean hasMetaAnnotation(String metaAnnotationName);

   // 是否存在被指定 直接注解或元注解 标注的方法
   boolean hasAnnotatedMethods(String annotationName);

   // 返回上述方法的 MethodMetadata 集合
   Set<MethodMetadata> getAnnotatedMethods(String annotationName);
  */

  /*
   * StandardAnnotationMetadata 是 AnnotationMetadata 的标准实现，同时也继承了 StandardClassMetadata，
   * 所以针对 ClassMetadata 方法的实现则由 StandardClassMetadata 来完成，
   * 同样 AnnotationMetadata 相关方法委托 AnnotatedElementUtils 实现。
   */

  @Test
  @DisplayName("annotationMetadata")
  void annotationMetadata() {
    StandardAnnotationMetadata metadata =
        new StandardAnnotationMetadata(StandardAnnotationMetadataRunner.class);

    System.out.println(metadata.getAnnotationTypes());
    System.out.println(metadata.getMetaAnnotationTypes("org.springframework.stereotype.Component"));

    assertTrue(metadata.hasAnnotation("org.springframework.stereotype.Component"));
    assertTrue(metadata.hasMetaAnnotation("org.springframework.stereotype.Indexed"));
    assertTrue(metadata.hasAnnotatedMethods("org.springframework.scheduling.annotation.Async"));
    System.out.println(
        metadata.getAnnotatedMethods("org.springframework.scheduling.annotation.Async"));
  }

  @Order
  @Component
  static class StandardAnnotationMetadataRunner {

    @Async
    public void methodWithAnnotation() {}
  }
}
