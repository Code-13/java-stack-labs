/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.javalabs.jnaf.jdk9;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * StreamRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 14:58
 */
@DisplayName("Stream jdk9 新 api")
class StreamRunner {

  @Test
  @DisplayName("""
      Stream<T> ofNullable(T t)  返回包含单个元素的顺序Stream ，如果非空，否则返回空Stream
      """)
  void ofNullable() {
    Stream.ofNullable(null).forEach(System.out::println);
  }
  
  @Test
  @DisplayName("""
      用来生成有限流的新迭代实现
      seed 初始种子值
      hasNext 用来判断何时结束流，这个与seed有关。如何该函数不迭代保留seed计算，返回的流可能为空。
      next函数用来计算下一个元素值。
      """)
  void iterate() {
    Stream.iterate(0, i -> i < 5, i -> i + 1)
        .forEach(System.out::println);

    // equals

    for (int i = 0; i < 5; ++i) {
      System.out.println(i);
    }

  }

  @Test
  @DisplayName("""
      Stream.takeWhile(Predicate)
      Stream中元素会被断言Predicate，一旦元素断言为false就中断操作，忽略掉没有断言的元素（及时未断言中的元素有满足条件的），仅仅把之前满足元素返回。
      """)
  void takeWhile() {
    Stream.of(1, 2, 3, 4, 2, 5)
        .takeWhile(integer -> integer < 4)
        .forEach(System.out::println);
  }

  @Test
  @DisplayName("""
      这个API和takeWhile机制类似，也用来筛选Stream中的元素。
      不过符合断言的元素会被从Stream中移除。
      一旦元素断言为false，就会把断言为false的元素以及后面的元素统统返回。
      """)
  void dropWhile() {
    Stream.of(1, 2, 3, 4, 2, 5)
        .dropWhile(integer -> integer < 4)
        .forEach(System.out::println);
  }
  
}
