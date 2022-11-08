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

import java.nio.charset.Charset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Converter_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/7 22:16
 */
public class Converter_Runner {

  /*
   * Converter用于解决1:1的任意类型转换，因此它必然存在一个不足：解决1:N转换问题需要写N遍，造成重复冗余代码。
   */

  ConversionService conversionService = DefaultConversionService.getSharedInstance();

  @Test
  @DisplayName("stringToBooleanConverter")
  void stringToBooleanConverter() {
    System.out.println("----------------StringToBooleanConverter---------------");
    //    Converter<String, Boolean> converter2 = new StringToBooleanConverter();

    // trueValues.add("true");
    // trueValues.add("on");
    // trueValues.add("yes");
    // trueValues.add("1");
    System.out.println(conversionService.convert("true", Boolean.class));
    System.out.println(conversionService.convert("1", Boolean.class));

    // falseValues.add("false");
    // falseValues.add("off");
    // falseValues.add("no");
    // falseValues.add("0");
    System.out.println(conversionService.convert("FalSe", Boolean.class));
    System.out.println(conversionService.convert("off", Boolean.class));
    // 注意：空串返回的是null
    System.out.println(conversionService.convert("", Boolean.class));

    System.out.println("----------------StringToCharsetConverter---------------");
    //    Converter<String, Charset> converter2 = new StringToCharsetConverter();

    // 中间横杠非必须，但强烈建议写上   不区分大小写
    System.out.println(conversionService.convert("uTf-8", Charset.class));
    System.out.println(conversionService.convert("utF8", Charset.class));
  }
}
