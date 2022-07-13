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

package io.github.code13.javastack.libs.redisson.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.EvictionMode;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.EntryRemovedListener;
import org.redisson.api.map.event.EntryUpdatedListener;

/**
 * Map.
 *
 * <p>基于Redis的Redisson的分布式映射结构的RMap Java对象实现了java.util.concurrent.ConcurrentMap接口和java.util.Map接口。
 * 与HashMap不同的是，RMap保持了元素的插入顺序。该对象的最大容量受Redis限制，最大元素数量是4 294 967 295个.
 *
 * <p>在特定的场景下，映射缓存（Map）上的高度频繁的读取操作，使网络通信都被视为瓶颈时，可以使用Redisson提供的带有本地缓存功能的映射。
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/29 09:08
 */
@DisplayName("映射（Map）")
class MapRunner {

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
  @DisplayName("简单使用")
  void test1() {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var prev1 = map.put("k1", "v1");
    assertNull(prev1);

    var prev2 = map.putIfAbsent("k1", "v2");
    assertEquals(prev2, "v1");

    var v = map.remove("k1");
    assertEquals(v, "v1");
  }

  @Test
  @DisplayName("fast*")
  void testFast() {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var b = map.fastPut("k2", "v2");
    assertTrue(b);

    var b1 = map.fastPutIfAbsent("k2", "v2");
    assertFalse(b1);

    var count = map.fastRemove("k2");
    assertEquals(count, 1L);
  }

  @Test
  @DisplayName("Lock")
  void lock() {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var keyLock = map.getLock("k1");
    keyLock.lock();
    try {
      var v1 = map.get("k1");
      System.out.println(v1);
      assertNull(v1);
    } finally {
      keyLock.unlock();
    }
  }

  @Test
  @DisplayName("readWriteLock")
  void readWriteLock() {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var rwLock = map.getReadWriteLock("k2");
    rwLock.readLock().lock();
    try {
      var v1 = map.get("k2");
      System.out.println(v1);
      assertNull(v1);
    } finally {
      rwLock.readLock().unlock();
    }
  }

  @Test
  @DisplayName("semaphore")
  void semaphore() {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var semaphore = map.getSemaphore("k3");
    var b = semaphore.trySetPermits(10);
    assertTrue(b);
    var i = semaphore.availablePermits();
    assertEquals(i, 10);
    semaphore.delete();
  }

  @Test
  @DisplayName("countDownLatch")
  void countDownLatch() throws InterruptedException {
    var map = redissonClient.<String, String>getMap("Map");
    map.delete();

    var countDownLatch = map.getCountDownLatch("k4");
    countDownLatch.trySetCount(10L);
    ExecutorService service = Executors.newCachedThreadPool();
    for (int j = 0; j < 10; j++) {
      int finalJ = j;
      service.execute(
          () -> {
            System.out.println(finalJ + "v");
            countDownLatch.countDown();
          });
    }
    service.shutdown();
    countDownLatch.await();
    countDownLatch.delete();
  }

  /**
   * Map object with eviction support implements org.redisson.api.RMapCache interface and extends
   * java.util.concurrent.ConcurrentMap interface. It also has Async, Reactive and RxJava3
   * interfaces.
   *
   * <p>Current Redis implementation doesn't have map entry eviction functionality. Therefore
   * expired entries are cleaned time to time by org.redisson.eviction.EvictionScheduler. It removes
   * 100 expired entries at once. Task launch time tuned automatically and depends on expired
   * entries amount deleted in previous time and varies between 5 second to half an hour. Thus if
   * clean task deletes 100 entries each time it will be executed every 5 seconds (minimum execution
   * delay). But if current expired entries amount is lower than previous one then execution delay
   * will be increased by 1.5 times and decreased otherwise.
   *
   * <p>It's recommended to use single instance of RMapCache object with the same name for each
   * Redisson client instance.
   */
  @Test
  @DisplayName("Eviction")
  void eviction() {
    var mapCache = redissonClient.<String, String>getMapCache("MapCache");
    mapCache.delete();

    mapCache.put("k1", "v1", 10L, TimeUnit.SECONDS, 15L, TimeUnit.SECONDS);

    var v = mapCache.get("k1");
    System.out.println("v = " + v);

    mapCache.destroy();

    var v1 = mapCache.get("k1");
    System.out.println("v1 = " + v1);
  }

  /**
   * Map object with local cache support implements org.redisson.api.RLocalCachedMap which extends
   * java.util.concurrent.ConcurrentMap interface.
   *
   * <p>It's recommended to use single instance of LocalCachedMap instance per name for each
   * Redisson client instance. Same LocalCachedMapOptions object should be used across all instances
   * with the same name.
   */
  @Test
  @DisplayName("localCache")
  void localCache() {
    var localCachedMap =
        redissonClient.<String, String>getLocalCachedMap(
            "LocalCachedMap", LocalCachedMapOptions.defaults());
    localCachedMap.delete();

    var prev = localCachedMap.put("k1", "v1");
    assertNull(prev);

    var prev1 = localCachedMap.putIfAbsent("k2", "v2");
    assertNull(prev1);

    var remove = localCachedMap.remove("k1");
    assertEquals(remove, "v1");
  }

  @Test
  @DisplayName("Data Partitioning")
  void dataPartitioning() {
    // todo
  }

  @Test
  @DisplayName("Map persistence")
  void mapPersistence() {
    // todo
  }

  @Test
  @DisplayName("Map listeners")
  void mapListeners() {
    var map = redissonClient.<String, String>getMapCache("MapListeners");
    map.delete();

    var updatedListener =
        map.addListener(
            (EntryUpdatedListener<String, String>)
                event -> {
                  System.out.println("Updated:event.getKey() = " + event.getKey());
                  System.out.println("Updated:event.getValue() = " + event.getValue());
                  System.out.println("Updated:event.getOldValue() = " + event.getOldValue());
                });

    var createdListener =
        map.addListener(
            (EntryCreatedListener<Integer, Integer>)
                event -> {
                  System.out.println("Created:event.getKey() = " + event.getKey());
                  System.out.println("Created:event.getValue() = " + event.getValue());
                });

    var expireListener =
        map.addListener(
            (EntryExpiredListener<Integer, Integer>)
                event -> {
                  System.out.println("Expired:event.getKey() = " + event.getKey());
                  System.out.println("Expired:event.getValue() = " + event.getValue());
                });

    var removedListener =
        map.addListener(
            (EntryRemovedListener<Integer, Integer>)
                event -> {
                  System.out.println("Removed:event.getKey() = " + event.getKey());
                  System.out.println("Removed:event.getValue() = " + event.getValue());
                });

    map.put("k1", "v1");
    map.put("k1", "v2");

    map.remove("k1");
  }

  @Test
  @DisplayName("LRU/LFU bounded Map")
  void LRULFUBoundedMap() {
    var map = redissonClient.<String, String>getMapCache("anyMap");
    map.delete();

    // tries to set limit map to 10 entries using LRU eviction algorithm
    map.trySetMaxSize(10);
    // ... using LFU eviction algorithm
    map.trySetMaxSize(10, EvictionMode.LFU);

    // set or change limit map to 10 entries using LRU eviction algorithm
    map.setMaxSize(10);
    // ... using LFU eviction algorithm
    map.setMaxSize(10, EvictionMode.LFU);

    map.put("1", "2");
    map.put("3", "3", 1, TimeUnit.SECONDS);
  }
}
