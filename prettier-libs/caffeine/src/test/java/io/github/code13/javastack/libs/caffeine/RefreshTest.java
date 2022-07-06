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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.testing.FakeTicker;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RefreshTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/6 15:03
 */
class RefreshTest {

  @Test
  @DisplayName("testRefresh")
  void testRefresh() {

    FakeTicker ticker = new FakeTicker();

    LoadingCache<Key, Graph> cache =
        Caffeine.newBuilder()
            .ticker(ticker::read)
            .refreshAfterWrite(1, TimeUnit.MINUTES)
            .build(CaffeineInternalUtils::createExpensiveGraphI);

    Key key = new Key();
    Graph graph = new Graph();
    cache.put(key, graph);

    ticker.advance(5, TimeUnit.MINUTES);

    Graph newGraph = cache.getIfPresent(key);
    assertNotNull(newGraph);
    assertNotEquals(graph, newGraph);
  }
}
