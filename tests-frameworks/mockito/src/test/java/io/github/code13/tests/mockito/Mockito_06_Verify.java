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

package io.github.code13.tests.mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

/**
 * Mockito_06_Verify.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/18 00:01
 * @see org.mockito.Mockito#verify(Object)
 */
public class Mockito_06_Verify {

  static class ExampleService {

    public int add(int a, int b) {
      return a + b;
    }
  }

  @Test
  public void test01() {
    // 使用 verify 可以校验 mock 对象是否发生过某些操作

    ExampleService exampleService = mock(ExampleService.class);

    // 设置让 add(1,2) 返回 100
    when(exampleService.add(1, 2)).thenReturn(100);

    exampleService.add(1, 2);

    // 校验是否调用过 add(1, 2) -> 校验通过
    verify(exampleService).add(1, 2);

    // 校验是否调用过 add(2, 2) -> 校验不通过
    verify(exampleService).add(2, 2);
  }

  @Test
  public void test02() {
    // verify 配合 time 方法，可以校验某些操作发生的次数

    ExampleService exampleService = mock(ExampleService.class);

    // 第1次调用
    exampleService.add(1, 2);

    // 校验是否调用过一次 add(1, 2) -> 校验通过
    verify(exampleService, times(1)).add(1, 2);

    // 第2次调用
    exampleService.add(1, 2);

    // 校验是否调用过两次 add(1, 2) -> 校验通过
    verify(exampleService, times(2)).add(1, 2);
  }
}
