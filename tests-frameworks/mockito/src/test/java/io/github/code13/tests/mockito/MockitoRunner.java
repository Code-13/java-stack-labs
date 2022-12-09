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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

/**
 * MockitoRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/8 14:53
 */
public class MockitoRunner {

  @Test
  @DisplayName("verify")
  void verifyTest() {
    List mockedList = mock(List.class);

    mockedList.add("one");
    mockedList.clear();

    verify(mockedList).add("one");
    verify(mockedList).clear();
  }

  @Test
  @DisplayName("stubbingTest")
  void stubbingTest() {
    // You can mock concrete classes, not just interfaces
    LinkedList mockedList = mock(LinkedList.class);

    // stubbing
    when(mockedList.get(0)).thenReturn("first");
    when(mockedList.get(1)).thenThrow(new RuntimeException());

    assertEquals("first", mockedList.get(0));
    assertThrows(RuntimeException.class, () -> mockedList.get(1));
    assertNull(mockedList.get(999));

    verify(mockedList).get(0);
  }

  @Test
  @DisplayName("argumentMatchers")
  void argumentMatchers() {
    LinkedList mockedList = mock(LinkedList.class);

    // stubbing using built-in anyInt() argument matcher
    when(mockedList.get(anyInt())).thenReturn("element");

    // stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
    when(mockedList.contains(argThat(isValid()))).thenReturn(true);

    assertEquals("element", mockedList.get(999));

    // you can also verify using an argument matcher
    verify(mockedList).get(anyInt());

    // argument matchers can also be written as Java 8 Lambdas
    //    verify(mockedList).add(argThat((String someString) -> someString.length() > 5));
  }

  @Test
  @DisplayName("VerifyingExactNumberOfInvocations")
  void verifyingExactNumberOfInvocations() {
    LinkedList mockedList = mock(LinkedList.class);

    // using mock
    mockedList.add("once");

    mockedList.add("twice");
    mockedList.add("twice");

    mockedList.add("three times");
    mockedList.add("three times");
    mockedList.add("three times");

    // following two verifications work exactly the same - times(1) is used by default
    verify(mockedList).add("once");
    verify(mockedList, times(1)).add("once");

    // exact number of invocations verification
    verify(mockedList, times(2)).add("twice");
    verify(mockedList, times(3)).add("three times");

    // verification using never(). never() is an alias to times(0)
    verify(mockedList, never()).add("never happened");

    // verification using atLeast()/atMost()
    verify(mockedList, atMostOnce()).add("once");
    verify(mockedList, atLeastOnce()).add("three times");
    verify(mockedList, atLeast(2)).add("three times");
    verify(mockedList, atMost(5)).add("three times");
  }

  @Test
  @DisplayName("stubbingVoidMethodsWithExceptions")
  void stubbingVoidMethodsWithExceptions() {
    LinkedList mockedList = mock(LinkedList.class);

    doThrow(new RuntimeException()).when(mockedList).clear();

    assertThrows(RuntimeException.class, mockedList::clear);
  }

  static ArgumentMatcher<String> isValid() {
    return argument -> argument.length() > 5;
  }
}
