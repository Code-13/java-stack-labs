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

package io.github.code13.javastack.libs.vavr.values;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * vavr Try.
 *
 * <p>Try is a monadic container type which represents a computation that may either result in an
 * exception, or return a successfully computed value. It’s similar to, but semantically different
 * from Either. Instances of Try, are either an instance of Success or Failure.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 16:05
 */
@DisplayName("Try")
class TryTest {

  @Test
  @DisplayName("Example for Try")
  void test1() {

    Integer result =
        Try.of(this::bunchOfWork)
            .recover(
                x ->
                    Match(x)
                        .of(
                            Case($(instanceOf(IllegalArgumentException.class)), t -> 2),
                            Case($(instanceOf(NullPointerException.class)), t -> 3)))
            .getOrElse(10);

    System.out.println(result);
  }

  private int bunchOfWork() {
    return 1;
    // throw new IllegalArgumentException();
    // throw new NullPointerException();
  }
}
