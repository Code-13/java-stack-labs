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

package io.github.code13.libs.caffeine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PopulationTest.
 *
 * <p>添加策略
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/4 13:28
 */
class PopulationTest {

  @Test
  @DisplayName("Manual")
  void manual() {
    Cache<Key, Graph> cache =
        Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(10_000).build();

    Key key = new Key();

    // Lookup an entry, or null if not found
    Graph graph = cache.getIfPresent(key);
    assertNull(graph);

    // Lookup and compute an entry if absent, or null if not computable
    graph = cache.get(key, k -> createExpensiveGraph(key));
    assertNotNull(graph);

    // Insert or update an entry
    Graph g = new Graph(100_000L);
    cache.put(key, g);
    graph = cache.getIfPresent(key);
    assertEquals(graph, g);

    // Remove an entry
    cache.invalidate(key);
    graph = cache.getIfPresent(key);
    assertNull(graph);
  }

  @Test
  @DisplayName("Loading")
  void loading() {
    LoadingCache<Key, Graph> cache =
        Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(this::createExpensiveGraph);

    Key key = new Key();

    // Lookup and compute an entry if absent, or null if not computable
    Graph graph = cache.get(key);
    assertNotNull(graph);

    Map<Key, Graph> graphs = cache.getAll(List.of(new Key(), new Key()));
    assertNotNull(graphs);
  }

  @Test
  @DisplayName("Asynchronous")
  void asynchronous() {
    AsyncCache<Key, Graph> cache =
        Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .buildAsync();

    Key key = new Key();

    CompletableFuture<Graph> graphFuture = cache.getIfPresent(key);
    assertNull(graphFuture);

    graphFuture = cache.get(key, this::createExpensiveGraph);
    assertNotNull(graphFuture);

    cache.put(key, CompletableFuture.supplyAsync(Graph::new));

    cache.synchronous().invalidate(key);
  }

  @Test
  @DisplayName("Asynchronously")
  void asynchronously() {
    AsyncLoadingCache<Key, Graph> cache =
        Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .buildAsync(this::createExpensiveGraph);

    Key key = new Key();
    CompletableFuture<Graph> graph = cache.get(key);

    cache =
        Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .buildAsync(this::createExpensiveGraphAsync);

    key = new Key();
    graph = cache.get(key);

    assertNotNull(graph);
  }

  private CompletableFuture<? extends Graph> createExpensiveGraphAsync(Key key, Executor executor) {
    return CompletableFuture.supplyAsync(() -> new Graph(key.getId()), executor);
  }

  private Graph createExpensiveGraph(Key key) {
    return new Graph(key.getId());
  }
}
