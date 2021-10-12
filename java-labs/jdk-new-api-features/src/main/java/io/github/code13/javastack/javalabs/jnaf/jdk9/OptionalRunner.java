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

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * OptionalRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 15:21
 */
@DisplayName("Optional jdk9 新 api")
class OptionalRunner {

  @Test
  @DisplayName("Optional现在可以转Stream")
  void stream() {
    Optional.of("1").stream().forEach(System.out::println);
  }

  @Test
  @DisplayName(
      "ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)    如果有值了怎么消费，没有值了怎么消费")
  void ifPresentOrElse() {
    Optional.of("1").ifPresentOrElse(System.out::println, () -> System.out.println("没有值"));

    Optional.empty().ifPresentOrElse(System.out::println, () -> System.out.println("没有值"));
  }

  @Test
  @DisplayName("""
      or(Supplier<? extends Optional<? extends T>> supplier)
      如果有值就返回有值的Optional，否则就提供能获取一个有值的Optional的渠道（Supplier）
      """)
  void or() {
    Optional.empty().or(() -> Optional.of("1")).ifPresent(System.out::println);
  }
}
