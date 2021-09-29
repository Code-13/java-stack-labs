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

package io.github.code13.javastack.libs.redisson.collections;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSetMultimapCache;
import org.redisson.api.RedissonClient;

/**
 * MultimapRunner.
 *
 * <p>Redis based Multimap for Java allows to bind multiple values per key. Keys amount limited by
 * Redis to 4 294 967 295 elements.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/29 11:16
 */
@SuppressWarnings("DuplicatedCode")
@DisplayName("Multimap")
class MultimapRunner {
  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @AfterAll
  static void setDown() {
    redissonClient.shutdown();
  }

  @Test
  @DisplayName("Set based Multimap")
  void setBasedMultimap() {
    var setMultimap = redissonClient.<String, String>getSetMultimap("SetMultimap");
    setMultimap.delete();

    setMultimap.put("k1", "v1");
    setMultimap.put("k1", "v2");
    setMultimap.put("k1", "v3");

    System.out.println(setMultimap.get("k1"));

    var vList = List.of("v4", "v5", "v6");
    var oldValues = setMultimap.replaceValues("k1", vList);

    System.out.println(oldValues);
    System.out.println(setMultimap.get("k1"));

    setMultimap.removeAll("k1");
    System.out.println(setMultimap.get("k1"));
  }

  @Test
  @DisplayName("List based Multimap")
  void listBasedMultimap() {
    var listMultimap = redissonClient.<String, String>getListMultimap("ListMultimap");
    listMultimap.delete();

    listMultimap.put("k1", "v1");
    listMultimap.put("k1", "v2");
    listMultimap.put("k1", "v3");

    System.out.println(listMultimap.get("k1"));

    var vList = List.of("v4", "v5", "v6");
    var oldValues = listMultimap.replaceValues("k1", vList);

    System.out.println(oldValues);
    System.out.println(listMultimap.get("k1"));

    listMultimap.removeAll("k1");
    System.out.println(listMultimap.get("k1"));
  }

  @Test
  @DisplayName("Multimap eviction")
  void multimapEviction() {
    RSetMultimapCache<String, String> multimap =
        redissonClient.getSetMultimapCache("MultimapEviction");
    multimap.delete();

    multimap.put("1", "a");
    multimap.put("1", "b");
    multimap.put("1", "c");

    multimap.put("2", "e");
    multimap.put("2", "f");

    multimap.expireKey("2", 10, TimeUnit.SECONDS);

    multimap.delete();
  }
}
