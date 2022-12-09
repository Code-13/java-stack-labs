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

package io.github.code13.books.jcip.ch5_building_blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * HashMap 实现的 Memoization.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/9/2021 1:56 PM
 */
public class Memoizer<A, V> implements Computable<A, V> {

  private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

  private final Computable<A, V> c;

  public Memoizer(Computable<A, V> c) {
    this.c = c;
  }

  // 可使用 computedIfAbsent
  @Override
  public synchronized V compute(A arg) throws InterruptedException {

    Future<V> f = cache.get(arg);

    if (f == null) {
      FutureTask<V> ft = new FutureTask<>(() -> c.compute(arg));
      f = cache.putIfAbsent(arg, ft);
      if (f == null) {
        f = ft;
        ft.run();
      }
    }

    try {
      return f.get();
    } catch (ExecutionException e) {
      cache.remove(arg, f);
      throw LaunderThrowable.launderThrowable(e);
    }
  }
}
