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
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Removal.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/6 14:19
 */
class RemovalTest {

  /*
   * 驱逐 缓存元素因为策略被移除
   * 失效 缓存元素被手动移除
   * 移除 由于驱逐或者失效而最终导致的结果
   */

  @Test
  @DisplayName("ExplicitRemoval")
  void explicitRemoval() {
    Cache<Key, Graph> cache = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

    Key key = new Key();
    Graph value = new Graph();

    cache.put(key, value);

    assertNotNull(cache.getIfPresent(key));

    cache.invalidate(key);
    assertNull(cache.getIfPresent(key));

    for (int i = 0; i < 10_000; i++) {
      cache.put(new Key((long) i), new Graph((long) i));
    }

    cache.invalidateAll();
    cache.cleanUp();
    assertEquals(0, cache.estimatedSize());
  }

  @Test
  @DisplayName("RemovalListeners")
  void removalListeners() {
    Cache<Key, Graph> cache =
        Caffeine.newBuilder()
            .evictionListener(
                (key, value, cause) -> System.out.printf("Key %s was evicted (%s)%n", key, cause))
            .removalListener(
                (key, value, cause) -> System.out.printf("Key %s was removed (%s)%n", key, cause))
            .build();

    cache.put(new Key(), new Graph());
    cache.invalidateAll();
  }
}
