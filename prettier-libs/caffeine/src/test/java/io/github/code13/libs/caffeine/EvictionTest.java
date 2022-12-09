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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.testing.FakeTicker;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 驱逐策略.
 *
 * <p>Caffeine 提供了三种驱逐策略，分别是基于容量，基于时间和基于引用三种类型。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/6 09:34
 */
@DisplayName("Caffeine 提供了三种驱逐策略，分别是基于容量，基于时间和基于引用三种类型")
class EvictionTest {

  @DisplayName("基于容量")
  static class BaseOnSizeTest {

    @Test
    @DisplayName("基于缓存内的元素个数进行驱逐")
    void testSizeElementInCache() {
      LoadingCache<Key, Graph> cache =
          Caffeine.newBuilder()
              .maximumSize(10_000)
              .build(CaffeineInternalUtils::createExpensiveGraph);

      for (int i = 0; i < 100000; i++) {
        Graph graph = cache.get(new Key((long) i));
        assertNotNull(graph);
        cache.cleanUp();
        assertTrue(cache.estimatedSize() <= 10_000);
      }
    }

    @Test
    @DisplayName("基于缓存内元素权重进行驱逐")
    void testWeigherElementInCache() {
      // 基于缓存内元素权重进行驱逐
      LoadingCache<Key, Graph> cache1 =
          Caffeine.newBuilder()
              .maximumWeight(10_000)
              .weigher((Key key, Graph graph) -> graph.vertices().size())
              .build(CaffeineInternalUtils::createExpensiveGraph);
    }
  }

  @DisplayName("基于时间")
  static class BaseOneTimeTest {

    @Test
    @DisplayName(
        "一个元素在上一次读写操作后一段时间之后，在指定的时间后没有被再次访问将会被认定为过期项。"
            + "在当被缓存的元素时被绑定在一个session上时，当session因为不活跃而使元素过期的情况下，这是理想的选择。")
    void testExpireAfterAccess() {
      // Guava's testlib
      FakeTicker ticker = new FakeTicker();

      LoadingCache<Key, Graph> graphs =
          Caffeine.newBuilder()
              .ticker(ticker::read)
              .expireAfterAccess(5, TimeUnit.MINUTES)
              .build(CaffeineInternalUtils::createExpensiveGraph);

      Key key = new Key();
      graphs.put(key, new Graph());

      graphs.getIfPresent(key);

      ticker.advance(30, TimeUnit.MINUTES);

      assertNull(graphs.getIfPresent(key));
    }

    @Test
    @DisplayName("一个元素将会在其创建或者最近一次被更新之后的一段时间后被认定为过期项。在对被缓存的元素的时效性存在要求的场景下，这是理想的选择。")
    void testExpireAfterWrite() {
      // Guava's testlib
      FakeTicker ticker = new FakeTicker();

      LoadingCache<Key, Graph> graphs =
          Caffeine.newBuilder()
              .ticker(ticker::read)
              .expireAfterWrite(10, TimeUnit.MINUTES)
              .build(CaffeineInternalUtils::createExpensiveGraph);

      Key key = new Key();
      graphs.put(key, new Graph());

      ticker.advance(30, TimeUnit.MINUTES);

      assertNull(graphs.getIfPresent(key));
    }

    @Test
    @DisplayName("一个元素将会在指定的时间后被认定为过期项。当被缓存的元素过期时间收到外部资源影响的时候，这是理想的选择。")
    void testExpiry() {

      // Guava's testlib
      FakeTicker ticker = new FakeTicker();

      LoadingCache<Key, Graph> graphs =
          Caffeine.newBuilder()
              .ticker(ticker::read)
              .expireAfter(
                  new Expiry<Key, Graph>() {
                    @Override
                    public long expireAfterCreate(Key key, Graph graph, long currentTime) {
                      long second =
                          graph
                              .creationDate()
                              .plusHours(5)
                              .minus(System.currentTimeMillis(), ChronoUnit.MILLIS)
                              .toEpochSecond(ZoneOffset.ofHours(8));
                      return TimeUnit.SECONDS.toNanos(second);
                    }

                    @Override
                    public long expireAfterUpdate(
                        Key key, Graph graph, long currentTime, @NonNegative long currentDuration) {
                      return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(
                        Key key, Graph graph, long currentTime, @NonNegative long currentDuration) {
                      return currentDuration;
                    }
                  })
              .build(CaffeineInternalUtils::createExpensiveGraph);

      Key key = new Key();
      graphs.put(key, new Graph());

      ticker.advance(6, TimeUnit.HOURS);

      graphs.cleanUp();
      assertNull(graphs.getIfPresent(key));
    }
  }

  @DisplayName("基于引用")
  static class BaseOnReference {
    /*
     * Caffeine allows you to set up your cache to allow the garbage collection of entries, by using
     * weak references for keys or values, and by using soft references for values. Note that weak
     * and soft value references are not supported by AsyncCache.
     *
     * <p>Caffeine.weakKeys() stores keys using weak references. This allows entries to be
     * garbage-collected if there are no other strong references to the keys. Since garbage
     * collection depends only on identity equality, this causes the whole cache to use identity
     * (==) equality to compare keys, instead of equals().
     *
     * <p>Caffeine.weakValues() stores values using weak references. This allows entries to be
     * garbage-collected if there are no other strong references to the values. Since garbage
     * collection depends only on identity equality, this causes the whole cache to use identity
     * (==) equality to compare values, instead of equals().
     *
     * <p>Caffeine.softValues() stores values using soft references. Softly referenced objects are
     * garbage-collected in a globally least-recently-used manner, in response to memory demand.
     * Because of the performance implications of using soft references, we generally recommend
     * using the more predictable maximum cache size instead. Use of softValues() will cause values
     * to be compared using identity (==) equality instead of equals().
     */

    @Test
    @DisplayName("weakKeys and weakValues")
    void testWeakKeysAndWeaValues() {
      LoadingCache<Key, Graph> graphs =
          Caffeine.newBuilder()
              .weakKeys()
              .weakValues()
              .build(CaffeineInternalUtils::createExpensiveGraph);

      // Evict when the garbage collector needs to free memory
      LoadingCache<Key, Graph> cache =
          Caffeine.newBuilder().softValues().build(CaffeineInternalUtils::createExpensiveGraph);
    }
  }
}
