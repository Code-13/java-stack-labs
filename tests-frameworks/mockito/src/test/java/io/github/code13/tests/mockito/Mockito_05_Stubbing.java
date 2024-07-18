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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mockito_05_Stubbing.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/17 23:36
 * @see org.mockito.stubbing.OngoingStubbing;
 * @see org.mockito.Mockito#when(Object)
 * @see org.mockito.Mockito#doReturn(Object)(Object)
 * @see org.mockito.Mockito#doThrow(Throwable...)
 * @see org.mockito.Mockito#doNothing()
 * @see org.mockito.Mockito#doCallRealMethod()
 * @see org.mockito.Mockito#doAnswer(org.mockito.stubbing.Answer)
 */
public class Mockito_05_Stubbing extends BaseMockito_TestRunner {

  @Test
  @DisplayName("thenReturn")
  void thenReturn() {
    // thenReturn 用来指定特定函数和参数调用的返回值。

    Random mockRandom = mock(Random.class);

    when(mockRandom.nextInt()).thenReturn(1);

    assertEquals(1, mockRandom.nextInt());

    // thenReturn 中可以指定多个返回值。在调用时返回值依次出现。若调用次数超过返回值的数量，再次调用时返回最后一个返回值。

    mockRandom = mock(Random.class);

    when(mockRandom.nextInt()).thenReturn(1, 2, 3);

    assertEquals(1, mockRandom.nextInt());
    assertEquals(2, mockRandom.nextInt());
    assertEquals(3, mockRandom.nextInt());
    assertEquals(3, mockRandom.nextInt());
    assertEquals(3, mockRandom.nextInt());
  }

  @Test
  @DisplayName("thenThrow")
  void thenThrow() {
    //   thenThrow 用来让函数调用抛出异常。

    Random mockRandom = mock(Random.class);

    when(mockRandom.nextInt()).thenThrow(new RuntimeException("异常"));

    try {
      mockRandom.nextInt();
      fail(); // 上面会抛出异常，所以不会走到这里
    } catch (Exception ex) {
      assertTrue(ex instanceof RuntimeException);
      assertEquals("异常", ex.getMessage());
    }

    //   thenThrow 中可以指定多个异常。在调用时异常依次出现。若调用次数超过异常的数量，再次调用时抛出最后一个异常。

    mockRandom = mock(Random.class);

    when(mockRandom.nextInt()).thenThrow(new RuntimeException("异常1"), new RuntimeException("异常2"));

    try {
      mockRandom.nextInt();
      fail();
    } catch (Exception ex) {
      assertTrue(ex instanceof RuntimeException);
      assertEquals("异常1", ex.getMessage());
    }

    try {
      mockRandom.nextInt();
      fail();
    } catch (Exception ex) {
      assertTrue(ex instanceof RuntimeException);
      assertEquals("异常2", ex.getMessage());
    }

    //  对应返回类型是 void 的函数，thenThrow 是无效的，要使用 doThrow
  }

  static class ThenAndThenAnswerService {

    public int add(int a, int b) {
      return a + b;
    }
  }

  @Mock private ThenAndThenAnswerService thenAndThenAnswerService;

  @Test
  @DisplayName("thenAndThenAnswer")
  void thenAndThenAnswer() {
    when(thenAndThenAnswerService.add(anyInt(), anyInt()))
        .thenAnswer(
            (Answer<Integer>)
                invocation -> {
                  Object[] args = invocation.getArguments();
                  // 获取参数
                  Integer a = (Integer) args[0];
                  Integer b = (Integer) args[1];

                  // 根据第1个参数，返回不同的值
                  if (a == 1) {
                    return 9;
                  }
                  if (a == 2) {
                    return 99;
                  }
                  if (a == 3) {
                    throw new RuntimeException("异常");
                  }
                  return 999;
                });

    assertEquals(9, thenAndThenAnswerService.add(1, 100));
    assertEquals(99, thenAndThenAnswerService.add(2, 100));

    try {
      thenAndThenAnswerService.add(3, 100);
      fail();
    } catch (RuntimeException ex) {
      assertEquals("异常", ex.getMessage());
    }
  }

  @Test
  @DisplayName("doReturn")
  void doReturn_test() {
    // doReturn 的作用和 Mockito 使用 thenReturn 设置方法的返回值 相同，但使用方式不同：

    Random random = mock(Random.class);
    doReturn(1).when(random).nextInt();

    assertEquals(1, random.nextInt());
  }

  static class DoThrowService {
    public void hello() {
      System.out.println("Hello");
    }
  }

  @Test
  public void doThrow_test() {
    DoThrowService doThrowService = mock(DoThrowService.class);

    // 这种写法可以达到效果
    doThrow(new RuntimeException("异常")).when(doThrowService).hello();

    try {
      doThrowService.hello();
      fail();
    } catch (RuntimeException ex) {
      assertEquals("异常", ex.getMessage());
    }

    // 也可以用 doThrow 让返回非void的函数抛出异常

    Random random = mock(Random.class);

    // 下面这句等同于 when(random.nextInt()).thenThrow(new RuntimeException("异常"));
    doThrow(new RuntimeException("异常")).when(random).nextInt();

    try {
      random.nextInt();
      fail();
    } catch (RuntimeException ex) {
      assertEquals("异常", ex.getMessage());
    }
  }

  @Test
  @DisplayName("doAnswer_test")
  void doAnswer_test() {
    Random random = mock(Random.class);
    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) throws Throwable {
                return 1;
              }
            })
        .when(random)
        .nextInt();

    assertEquals(1, random.nextInt());
  }

  static class DoNothingService {

    public void hello() {
      System.out.println("Hello");
    }
  }

  @Test
  public void doNothing_test() {

    DoNothingService doNothingService = spy(new DoNothingService());
    doNothingService.hello(); // 会输出 Hello

    // 让 hello 什么都不做
    doNothing().when(doNothingService).hello();
    doNothingService.hello(); // 什么都不输出
  }

  static class ThenCallRealMethodService {
    public int add(int a, int b) {
      return a + b;
    }
  }

  @Test
  @DisplayName("thenCallRealMethod")
  void thenCallRealMethod() {
    ThenCallRealMethodService thenCallRealMethodService = spy(new ThenCallRealMethodService());

    // spy 对象方法调用会用真实方法，所以这里返回 3
    assertEquals(3, thenCallRealMethodService.add(1, 2));

    // 设置让 add(1,2) 返回 100
    when(thenCallRealMethodService.add(1, 2)).thenReturn(100);
    when(thenCallRealMethodService.add(2, 2)).thenReturn(100);
    assertEquals(100, thenCallRealMethodService.add(1, 2));
    assertEquals(100, thenCallRealMethodService.add(2, 2));

    // 重置 spy 对象，让 add(1,2) 调用真实方法，返回 3
    when(thenCallRealMethodService.add(1, 2)).thenCallRealMethod();
    assertEquals(3, thenCallRealMethodService.add(1, 2));

    // add(2, 2) 还是返回 100
    assertEquals(100, thenCallRealMethodService.add(2, 2));
  }
}
