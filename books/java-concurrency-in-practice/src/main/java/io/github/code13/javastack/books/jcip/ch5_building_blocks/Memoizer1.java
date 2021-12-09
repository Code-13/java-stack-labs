/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch5_building_blocks;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap 实现的 Memoization.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/9/2021 1:56 PM
 */
public class Memoizer1<A, V> implements Computable<A, V> {

  private final Map<A, V> cache = new HashMap<>();

  private final Computable<A, V> c;

  public Memoizer1(Computable<A, V> c) {
    this.c = c;
  }

  // 可使用 computedIfAbsent
  @Override
  public synchronized V compute(A arg) throws InterruptedException {

    V v = cache.get(arg);

    if (v == null) {
      v = c.compute(arg);
      cache.put(arg, v);
    }

    return v;
  }
}
