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

import io.vavr.Lazy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * vavr lazy
 *
 * <p>Lazy is a monadic container type which represents a lazy evaluated value. Compared to a
 * Supplier, Lazy is memoizing, i.e. it evaluates only once and therefore is referentially
 * transparent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 16:18
 */
@DisplayName("Lazy")
class LazyTest {

  @Test
  @DisplayName("Example for Lazy")
  void test() {
    Lazy<Double> lazy = Lazy.of(Math::random);

    System.out.println(lazy.isEvaluated());

    System.out.println(lazy.get());

    System.out.println(lazy.isEvaluated());
  }

  @Test
  @DisplayName("a real lazy value (works only with interfaces)")
  void test2() {
    CharSequence chars = Lazy.val(() -> "Yay!", CharSequence.class);

    System.out.println(chars);
  }
}
