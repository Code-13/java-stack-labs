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

package io.github.code13.libs.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * vavr Tuples
 *
 * <p>https://docs.vavr.io/#_tuples
 *
 * <p>Java is missing a general notion of tuples. A Tuple combines a fixed number of elements
 * together so that they can be passed around as a whole. Unlike an array or list, a tuple can hold
 * objects with different types, but they are also immutable.
 *
 * <p>Tuples are of type Tuple1, Tuple2, Tuple3 and so on. There currently is an upper limit of 8
 * elements. To access elements of a tuple t, you can use method t._1 to access the first element,
 * t._2 to access the second, and so on.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 14:51
 */
@DisplayName("Tuples")
class TuplesTest {

  /** Here is an example of how to create a tuple holding a String and an Integer:. */
  @Test
  @DisplayName("Create a tuple")
  void test1() {
    Tuple2<String, Integer> java11 = Tuple.of("Java", 11);

    // Java
    String s = java11._1;

    // 11
    Integer i = java11._2;
  }

  /**
   * The component-wise map evaluates a function per element in the tuple, returning another tuple.
   */
  @Test
  @DisplayName("Map a tuple component-wise")
  void test2() {
    Tuple2<String, Integer> java11 = Tuple.of("Java", 11);

    Tuple2<String, Integer> that = java11.map(s -> s.substring(2) + "vr", i -> i / 11);

    // (vavr, 1)
    System.out.println(that);
  }

  /** It is also possible to map a tuple using one mapping function. */
  @Test
  @DisplayName("Map a tuple using one mapper")
  void test3() {
    Tuple2<String, Integer> java11 = Tuple.of("Java", 11);

    Tuple2<String, Integer> that = java11.map((s, i) -> Tuple.of(s.substring(2) + "vr", i / 11));

    // (vavr, 1)
    System.out.println(that);
  }

  /** Transform creates a new type based on the tuple’s contents. */
  @Test
  @DisplayName("Transform a tuple")
  void test4() {
    Tuple2<String, Integer> java11 = Tuple.of("Java", 11);

    String that = java11.apply((s, i) -> s.substring(2) + "vr " + i / 11);

    // vavr 1
    System.out.println(that);
  }
}
