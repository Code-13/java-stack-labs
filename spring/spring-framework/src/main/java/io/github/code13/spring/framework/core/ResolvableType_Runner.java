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

package io.github.code13.spring.framework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * ResolvableType_Runner for {@link org.springframework.core.ResolvableType}
 *
 * @see org.springframework.core.ResolvableType
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 11:27
 */
public class ResolvableType_Runner {

  interface Service<N, M> {}

  class ServiceImpl<A, B> implements Service<String, Integer> {
    public ServiceImpl(List<List<String>> list, Map<Double, Map<Float, Integer>> map) {}

    public Map<String, List<Double>> method() {
      return Collections.emptyMap();
    }
  }

  @Test
  @DisplayName("forClassTest")
  void forClassTest() {
    ResolvableType resolvableType = ResolvableType.forClass(ServiceImpl.class);
    // getType 保存原始的 Type 类型
    assertEquals(ServiceImpl.class, resolvableType.getType());

    // resolve 将 Type 解析为 Class， 如果无法解析返回 null
    assertEquals(ServiceImpl.class, resolvableType.resolve());
  }

  private Service<Double, Float> service;
  private List<List<String>> list;
  private Map<String, Map<String, Integer>> map;
  private List<String>[] array;

  @Test
  @DisplayName("forFieldTest")
  void forFieldTest() {
    // 1. Service<Double, Float> service;
    Field field = ReflectionUtils.findField(ResolvableType_Runner.class, "service");
    ResolvableType resolvableType = ResolvableType.forField(field);

    // getType() 保存原始的 Type 类型
    System.out.println(resolvableType.getType());
    System.out.println(field.getGenericType());
    assertEquals(field.getGenericType(), resolvableType.getType());

    // resolve() 对于 ParameterizedType 类型保存的是 <> 之前的类型，即 Service.class
    System.out.println(resolvableType.resolve());
    assertEquals(
        ((ParameterizedType) field.getGenericType()).getRawType(), resolvableType.resolve());

    Class<?> clazz = resolvableType.getGeneric(0).resolve();
    assertEquals(Double.class, clazz);

    // 2. List<List<String>> list
    resolvableType =
        ResolvableType.forField(ReflectionUtils.findField(ResolvableType_Runner.class, "list"));

    clazz = resolvableType.getGeneric(0).getGeneric(0).resolve();
    assertEquals(String.class, clazz);
    // or
    clazz = resolvableType.getGeneric(0, 0).resolve();
    assertEquals(String.class, clazz);

    // 3. Map<String, Map<String, Integer>> map
    resolvableType =
        ResolvableType.forField(ReflectionUtils.findField(ResolvableType_Runner.class, "map"));
    clazz = resolvableType.getGeneric(1).getGeneric(1).resolve();
    assertEquals(Integer.class, clazz);

    // 4. List<String>[] array
    resolvableType =
        ResolvableType.forField(ReflectionUtils.findField(ResolvableType_Runner.class, "array"));
    assertTrue(resolvableType.isArray());
    assertEquals(List.class, resolvableType.getComponentType().resolve());
    assertEquals(String.class, resolvableType.getComponentType().getGeneric(0).resolve());
  }

  @Test
  @DisplayName("forMethod")
  void forMethod() {
    // 1. 方法的返回值类型
    ResolvableType resolvableType =
        ResolvableType.forMethodReturnType(ReflectionUtils.findMethod(ServiceImpl.class, "method"));
    assertEquals(Double.class, resolvableType.getGeneric(1, 0).resolve());

    // 2. 构造器 ServiceImpl(List<List<String>> list, Map<Double, Map<Float, Integer>> map)
    resolvableType =
        ResolvableType.forConstructorParameter(
            ClassUtils.getConstructorIfAvailable(ServiceImpl.class, List.class, Map.class), 0);
    assertEquals(String.class, resolvableType.getGeneric(0, 0).resolve());

    resolvableType =
        ResolvableType.forConstructorParameter(
            ClassUtils.getConstructorIfAvailable(ServiceImpl.class, List.class, Map.class), 1);
    assertEquals(Double.class, resolvableType.getGeneric(0).resolve());
    assertEquals(Float.class, resolvableType.getGeneric(1, 0).resolve());
    assertEquals(Integer.class, resolvableType.getGeneric(1, 1).resolve());
  }

  @Test
  @DisplayName("otherMethodInResolvableType")
  void otherMethodInResolvableType() {
    ResolvableType resolvableType = ResolvableType.forClass(HashMap.class);
    // 1. getInterfaces 获取接口
    assertEquals(Map.class, resolvableType.getInterfaces()[0].resolve());

    // 2. getSuperType 获取父类
    assertEquals(AbstractMap.class, resolvableType.getSuperType().resolve());

    // 3. as 向上转型 Map<K,V>
    ResolvableType mapResolvableType = resolvableType.as(Map.class);
    assertEquals(Map.class, mapResolvableType.resolve());

    // 4. getRawClass 当 type 是 ParameterizedType 时有效
    assertEquals(Map.class, mapResolvableType.getRawClass());
    assertEquals(HashMap.class.getGenericInterfaces()[0], mapResolvableType.getType());

    // 5. getGeneric 获取泛型 class ServiceImpl<A, B> implements Service<String, Integer>
    resolvableType = ResolvableType.forClass(ServiceImpl.class);
    // 当 Type 无法找到具体的 class 类型时返回 null
    assertEquals("A", resolvableType.getGeneric(0).getType().getTypeName());
    assertEquals(null, resolvableType.getGeneric(0).resolve());
    // 以下两种获取泛型的 Class 类型方式等价
    assertEquals(String.class, resolvableType.as(Service.class).getGeneric(0).resolve());
    assertEquals(String.class, resolvableType.as(Service.class).resolveGeneric(0));

    // 6. getComponentType 获取数组泛型 List<String>[] array
    resolvableType =
        ResolvableType.forField(ReflectionUtils.findField(ResolvableType_Runner.class, "array"));
    assertEquals(List.class, resolvableType.getComponentType().resolve());
  }

  @Test
  @DisplayName("createResolvableType")
  void createResolvableType() {
    ResolvableType resolvableType1 = ResolvableType.forClassWithGenerics(List.class, String.class);
    ResolvableType resolvableType2 = ResolvableType.forArrayComponent(resolvableType1);

    // List<String>[] array
    ResolvableType resolvableType3 =
        ResolvableType.forField(ReflectionUtils.findField(ResolvableType_Runner.class, "array"));
    assertTrue(resolvableType3.isAssignableFrom(resolvableType2));

    assertTrue(
        ResolvableType.forClass(Object.class)
            .isAssignableFrom(ResolvableType.forClass(String.class)));
  }
}
