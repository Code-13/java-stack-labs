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

package io.github.code13.libs.datafaker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;
import net.datafaker.Faker;
import net.datafaker.sequence.FakeSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Sequences.
 *
 * @see https://www.datafaker.net/documentation/sequences/
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/5 13:18
 */
class Sequences {

  Faker faker = new Faker();

  /*
   * Supported fake sequences:
   *
   * - FakeCollection
   * - FakeStream
   */

  /**
   * For example, the following code will generate a list/stream of first and last names with number
   * of elements in it between 3 and 5:
   */
  @Test
  @DisplayName("test1")
  void test1() {

    List<String> names =
        faker
            .collection(() -> faker.name().firstName(), () -> faker.name().lastName())
            .len(3, 5)
            .generate();
    // or
    Stream<String> nameStream =
        faker.stream(() -> faker.name().firstName(), () -> faker.name().lastName())
            .len(3, 5)
            .generate();

    System.out.println(names);
    assertNotNull(names);

    names = nameStream.toList();
    System.out.println(names);
    assertNotNull(names);
  }

  /** A list/stream can also contain different types: */
  @Test
  @DisplayName("test2")
  void test2() {
    List<Object> objects =
        faker
            .<Object>collection(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .maxLen(5)
            .generate();
    // or
    Stream<Object> objectStream =
        faker.<Object>stream(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .maxLen(5)
            .generate();

    System.out.println(objects);
    assertNotNull(objects);

    objects = objectStream.toList();
    System.out.println(objects);
    assertNotNull(objects);
  }

  /**
   * With usage of nullRate it is possible to specify how often it should contain null values. By
   * default, it's value is 0, i.e. no null values will be generated.
   */
  @Test
  @DisplayName("test3")
  void test3() {
    List<Object> objects =
        faker
            .<Object>collection(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .nullRate(1)
            .maxLen(5)
            .generate();
    // or
    Stream<Object> objectStream =
        faker.<Object>stream(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .nullRate(1)
            .maxLen(5)
            .generate();

    System.out.println(objects);
    assertNotNull(objects);

    objects = objectStream.toList();
    System.out.println(objects);
    assertNotNull(objects);
  }

  /**
   * The above will generate a collection/stream where every value is null. To generate a
   * collection/stream with only about 30% values of null, nullRate(0.3) will do it.
   */
  @Test
  @DisplayName("test4")
  void test4() {
    List<Object> objects =
        faker
            .<Object>collection(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .nullRate(0.3)
            .maxLen(5)
            .generate();

    // or
    Stream<Object> objectStream =
        faker.<Object>stream(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .nullRate(0.3)
            .maxLen(5)
            .generate();

    System.out.println(objects);
    assertNotNull(objects);

    objects = objectStream.toList();
    System.out.println(objects);
    assertNotNull(objects);
  }

  /**
   * FakeSequence also supports generation of an infinite stream:
   *
   * <p>It is also possible to distinguish finite and infinite FakeStreams based on FakeSequence
   * API:
   */
  @Test
  @DisplayName("test5")
  void test5() {
    FakeSequence<Object> fakeSequence =
        faker.<Object>stream(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .build();

    System.out.println(fakeSequence.isInfinite()); // true
    assertTrue(fakeSequence.isInfinite());

    // For FakeCollection this function will always return false:
    fakeSequence =
        faker
            .<Object>collection(() -> faker.name().firstName(), () -> faker.random().nextInt(100))
            .build();

    System.out.println(fakeSequence.isInfinite()); // false
  }
}
