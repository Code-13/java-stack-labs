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
import static org.mockito.Mockito.when;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Mockito_04_InjectMocks.
 *
 * <pre>
 * mockito 会将 @Mock、@Spy 修饰的对象自动注入到 @InjectMocks 修饰的对象中。
 *
 * 注入方式有多种，mockito 会按照下面的顺序尝试注入：
 *
 * 构造函数注入
 * 设值函数注入（set函数）
 * 属性注入
 * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/17 23:33
 * @see org.mockito.InjectMocks
 */
public class Mockito_04_InjectMocks extends BaseMockito_TestRunner {

  public static class HttpService {

    public int queryStatus() {
      // 发起网络请求，提取返回结果
      // 这里用随机数模拟结果
      return new Random().nextInt(2);
    }
  }

  public static class ExampleService {

    private HttpService httpService;

    public String hello() {
      int status = httpService.queryStatus();
      if (status == 0) {
        return "你好";
      } else if (status == 1) {
        return "Hello";
      } else {
        return "未知状态";
      }
    }
  }

  @Mock private HttpService httpService;

  @InjectMocks private ExampleService exampleService = new ExampleService(); // 会将 httpService 注入进去

  @Test
  public void test01() {
    when(httpService.queryStatus()).thenReturn(0);

    assertEquals("你好", exampleService.hello());
  }
}
