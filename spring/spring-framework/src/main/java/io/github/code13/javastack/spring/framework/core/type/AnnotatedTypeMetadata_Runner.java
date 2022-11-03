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

package io.github.code13.javastack.spring.framework.core.type;

/**
 * AnnotatedTypeMetadata_Runner.
 *
 * @see org.springframework.core.type.AnnotatedTypeMetadata
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 21:05
 */
public class AnnotatedTypeMetadata_Runner {

  /*
   * 顶层接口，可被注解标注类型（类、方法）元数据的抽象，提供了两个核心方法：
   *
   * 根据 全类名 判断是否被指定注解标注 根据 全类名 返回指定注解的属性集合（包括元注解）
   */

  /*
   // 根据“全类名”判断是否被指定 直接注解或元注解 标注
   boolean isAnnotated(String annotationName);

   // 根据”全类名“获取所有注解属性（包括元注解）
   @Nullable
   Map<String, Object> getAnnotationAttributes(String annotationName);

   @Nullable
   // 同上，但是第二个参数传 true 时会把属性中对应值为 Class 的值转为 字符串，避免需要预先加载对应 Class
   Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString);

   @Nullable
   // 同上，MultiValueMap 是一个 key 可以对应多个 value 的变种 map
   MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName);
   @Nullable
   MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString);
  */

}
