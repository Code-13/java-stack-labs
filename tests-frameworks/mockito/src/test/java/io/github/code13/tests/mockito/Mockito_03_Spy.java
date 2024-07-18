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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;

/**
 * Mockito_03_Spy.
 *
 * <pre>
 * spy 和 mock不同，不同点是：
 *
 * spy 的参数是对象示例，mock 的参数是 class。
 * 被 spy 的对象，调用其方法时默认会走真实方法。mock 对象不会。
 * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/17 23:26
 * @see org.mockito.Spy
 * @see org.mockito.Mockito#spy(Object)
 */
public class Mockito_03_Spy extends BaseMockito_TestRunner {

  static class ExampleService {

    int add(int a, int b) {
      return a + b;
    }
  }

  // 测试 spy
  @Test
  public void test_spy() {

    ExampleService spyExampleService = spy(new ExampleService());

    // 默认会走真实方法
    assertEquals(3, spyExampleService.add(1, 2));

    // 打桩后，不会走了
    when(spyExampleService.add(1, 2)).thenReturn(10);
    assertEquals(10, spyExampleService.add(1, 2));

    // 但是参数比匹配的调用，依然走真实方法
    assertEquals(3, spyExampleService.add(2, 1));
  }

  // 测试 mock
  @Test
  public void test_mock() {

    ExampleService mockExampleService = mock(ExampleService.class);

    // 默认返回结果是返回类型int的默认值
    assertEquals(0, mockExampleService.add(1, 2));
  }

  // 测试 @Spy 注解

  @Spy private ExampleService spyExampleService;

  // 和上面的写法相等
  // @Spy private ExampleService spyExampleService = new ExampleService();

  @Test
  public void test_spy_annotation() {

    assertEquals(3, spyExampleService.add(1, 2));

    when(spyExampleService.add(1, 2)).thenReturn(10);
    assertEquals(10, spyExampleService.add(1, 2));
  }

  // 如何没有无参构造函数，则必须显示初始化

  static class ExampleService1 {

    private final int a;

    public ExampleService1(int a) {
      this.a = a;
    }

    int add(int b) {
      return a + b;
    }
  }

  @Spy private ExampleService1 spyExampleService1 = new ExampleService1(1);

  @Test
  public void test_spy_02() {
    assertEquals(3, spyExampleService1.add(2));
  }
}
