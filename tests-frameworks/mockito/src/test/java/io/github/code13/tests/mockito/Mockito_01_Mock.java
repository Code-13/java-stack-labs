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
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Mockito_01_Mock.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/17 23:01
 * @see org.mockito.Mock
 * @see org.mockito.Mockito#mock(Class)
 */
public class Mockito_01_Mock extends BaseMockito_TestRunner {

  @Mock private ArrayList<String> mockList;

  @Test
  public void mockClass() {
    // mock 类

    Random mockRandom = mock(Random.class);
    when(mockRandom.nextInt()).thenReturn(100); // 指定调用 nextInt 方法时，永远返回 100

    assertEquals(100, mockRandom.nextInt());
    assertEquals(100, mockRandom.nextInt());

    // 注意，mock 对象的方法的返回值默认都是返回类型的默认值。例如，返回类型是 int，默认返回值是 0；返回类型是一个类，默认返回值是 null。
  }

  @Test
  @DisplayName("mockInterface")
  void mockInterface() {
    List mockList = mock(List.class);

    assertEquals(0, mockList.size());
    assertEquals(null, mockList.get(0));

    mockList.add("a"); // 调用 mock 对象的写方法，是没有效果的

    assertEquals(0, mockList.size()); // 没有指定 size() 方法返回值，这里结果是默认值
    assertEquals(null, mockList.get(0)); // 没有指定 get(0) 返回值，这里结果是默认值

    when(mockList.get(0)).thenReturn("a"); // 指定 get(0)时返回 a

    assertEquals(0, mockList.size()); // 没有指定 size() 方法返回值，这里结果是默认值
    assertEquals("a", mockList.get(0)); // 因为上面指定了 get(0) 返回 a，所以这里会返回 a

    assertEquals(null, mockList.get(1)); // 没有指定 get(1) 返回值，这里结果是默认值
  }

  @Test
  @DisplayName("mockAnnotation")
  void mockAnnotation() {
    // @Mock 注解可以理解为对 mock 方法的一个替代。
    // 使用该注解时，要使用MockitoAnnotations.openMocks 方法，让注解生效。

    when(mockList.get(0)).thenReturn("abc");

    assertEquals(3, mockList.get(0).length());
  }

  @Test
  public void test() {
    ArrayList<String> mockList = mock(ArrayList.class); // 这种写法不够精确，IDE也会警告

    when(mockList.get(0)).thenReturn("abc");

    assertEquals(3, mockList.get(0).length());
  }

  // 下面这种用 @Mock 注解的方法，IDE 不会警告：
  @Mock private ArrayList<String> mockList01;

  @Test
  public void test01() {

    when(mockList01.get(0)).thenReturn("abc");

    assertEquals(3, mockList.get(0).length());
  }
}
