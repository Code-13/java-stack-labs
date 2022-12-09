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

package io.github.code13.spring.framework.core.converter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * GenericConverter_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/7 22:51
 */
public class GenericConverter_Runner {

  /*
   * 如果说它的优点是功能强大，能够处理复杂类型的转换（PropertyEditor和前2个接口都只能转换单元素类型），那么缺点就是使用、自定义实现起来比较复杂。
   * 这不官方也给出了使用指导意见：在Converter/ConverterFactory接口能够满足条件的情况下，可不使用此接口就不使用。
   */

  ConversionService conversionService = DefaultConversionService.getSharedInstance();

  @Test
  @DisplayName("test")
  void test() {
    System.out.println("----------------CollectionToCollectionConverter---------------");
    //    ConditionalGenericConverter conditionalGenericConverter =
    //        new CollectionToCollectionConverter(new DefaultConversionService());
    // 将Collection转为Collection（注意：没有指定泛型类型哦）
    //    System.out.println(conditionalGenericConverter.getConvertibleTypes());

    List<String> sourceList = Arrays.asList("1", "2", "2", "3", "4");
    TypeDescriptor sourceTypeDesp =
        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class));
    TypeDescriptor targetTypeDesp =
        TypeDescriptor.collection(Set.class, TypeDescriptor.valueOf(Integer.class));

    System.out.println(conversionService.canConvert(sourceTypeDesp, targetTypeDesp));

    Object convert = conversionService.convert(sourceList, sourceTypeDesp, targetTypeDesp);
    System.out.println(convert.getClass());
    System.out.println(convert);
  }
}
