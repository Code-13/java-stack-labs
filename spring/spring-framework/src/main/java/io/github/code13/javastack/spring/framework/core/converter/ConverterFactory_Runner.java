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

package io.github.code13.javastack.spring.framework.core.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * ConverterFactory_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/7 22:42
 */
public class ConverterFactory_Runner {

  /*
   * 从名称上看它代表一个转换工厂：可以将对象S转换为R的所有子类型，从而形成1:N的关系。
   *
   * 既然有了1:1、1:N，自然就有N:N。
   * 比如集合转换、数组转换、Map到Map的转换等等，这些N:N的场景，就需要借助下一个接口GenericConverter来实现。
   */

  ConversionService conversionService = DefaultConversionService.getSharedInstance();

  @Test
  @DisplayName("stringToNumberConverterFactory")
  void stringToNumberConverterFactory() {
    System.out.println("----------------StringToNumberConverterFactory---------------");
    //    ConverterFactory<String, Number> converterFactory = new StringToNumberConverterFactory();
    // 注意：这里不能写基本数据类型。如int.class将抛错
    System.out.println(conversionService.convert("1", Integer.class).getClass());
    System.out.println(conversionService.convert("1.1", Double.class).getClass());
    System.out.println(conversionService.convert("0x11", Byte.class).getClass());
  }
}
