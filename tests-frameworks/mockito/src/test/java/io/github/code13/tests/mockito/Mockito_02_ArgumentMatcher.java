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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Mockito_02_ArgumentMatcher.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @see org.mockito.ArgumentMatcher
 * @see org.mockito.ArgumentMatchers
 * @since 2024/7/17 23:17
 */
public class Mockito_02_ArgumentMatcher extends BaseMockito_TestRunner {

  @Mock private List<String> mockStringList;

  @Test
  @DisplayName("精确匹配")
  public void eq01() {

    mockStringList.add("a");

    when(mockStringList.get(0)).thenReturn("a");
    when(mockStringList.get(1)).thenReturn("b");

    when(mockStringList.get(eq(0))).thenReturn("a");
    when(mockStringList.get(eq(1))).thenReturn("b");

    assertEquals("a", mockStringList.get(0));
    assertEquals("b", mockStringList.get(1));
  }

  /** 更多请查看：{@link org.mockito.ArgumentMatchers} */
  @Test
  @DisplayName("模糊匹配")
  void fuzzyMatching() {

    mockStringList.add("a");

    when(mockStringList.get(anyInt())).thenReturn("a"); // 使用 Mockito.anyInt() 匹配所有的 int

    assertEquals("a", mockStringList.get(0));
    assertEquals("a", mockStringList.get(1));
  }

  @Mock private List<String> testList;

  /**
   * 如果参数匹配即生命了精确匹配，也声明了模糊匹配；又或者同一个值的精确匹配出现了两次，使用时会匹配哪一个？
   *
   * <p>会匹配符合匹配条件的最新声明的匹配。
   */
  @Test
  public void test01() {

    // 精确匹配 0
    when(testList.get(0)).thenReturn("a");

    assertEquals("a", testList.get(0));

    // 精确匹配 0
    when(testList.get(0)).thenReturn("b");

    assertEquals("b", testList.get(0));

    // 模糊匹配
    when(testList.get(anyInt())).thenReturn("c");

    assertEquals("c", testList.get(0));
    assertEquals("c", testList.get(1));
  }
}
