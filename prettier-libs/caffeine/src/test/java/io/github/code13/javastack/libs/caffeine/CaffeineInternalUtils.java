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

package io.github.code13.javastack.libs.caffeine;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * CaffeineInternalUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/6 10:10
 */
class CaffeineInternalUtils {

  static Graph createExpensiveGraph(Key key) {
    return new Graph(key.getId());
  }

  CompletableFuture<? extends Graph> createExpensiveGraphAsync(Key key, Executor executor) {
    return CompletableFuture.supplyAsync(() -> new Graph(key.getId()), executor);
  }
}
