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

package io.github.code13.libs.redisson.objects;

import static io.github.code13.libs.redisson.internal.InternalUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.github.code13.libs.redisson.RedissonClientBuilder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象. 除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 12:20
 */
@DisplayName("通用对象桶（Object Bucket）")
class ObjectBucketRunner {

  static RedissonClient redissonClient;
  static RBucket<Object> bucket;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    bucket = redissonClient.getBucket("Bucket");
  }

  @Test
  @DisplayName("set")
  void set() {
    bucket.set(1);
    var v = bucket.get();
    assertEquals(v, 1);
  }

  @Test
  @DisplayName("getAndDelete")
  void getAndDelete() {
    bucket.set(1);
    var v = bucket.getAndDelete();
    assertEquals(v, 1);
    assertNull(bucket.get());
  }

  @Test
  @DisplayName("trySet")
  void trySet() {
    bucket.delete();

    var b = bucket.trySet(1);
    assertTrue(b);

    var b1 = bucket.trySet(1);
    assertFalse(b1);

    bucket.delete();

    var b2 = bucket.trySet(1, 3, TimeUnit.SECONDS);
    assertTrue(b2);

    var b3 = bucket.trySet(1, 3, TimeUnit.SECONDS);
    assertFalse(b3);

    sleep(5);

    var b4 = bucket.trySet(1, 3, TimeUnit.SECONDS);
    assertTrue(b4);
  }

  @Test
  @DisplayName("setIfExists")
  void setIfExists() {
    bucket.delete();

    var b = bucket.setIfExists(1);
    assertFalse(b);

    bucket.set(1);
    var b1 = bucket.setIfExists(1, 5, TimeUnit.SECONDS);
    assertTrue(b1);
  }

  @Test
  @DisplayName("compareAndSet")
  void compareAndSet() {
    bucket.delete();

    bucket.set(1);
    var b = bucket.compareAndSet(1, 2);
    assertTrue(b);

    var b1 = bucket.compareAndSet(1, 3);
    assertFalse(b1);
  }

  @Test
  @DisplayName("getAndSet")
  void getAndSet() {
    bucket.delete();

    var v1 = bucket.getAndSet(1);
    assertNull(v1);

    var v2 = bucket.getAndSet(2);
    assertEquals(v2, 1);
  }

  /** 需要 redis 6.0 以上. */
  @Test
  @DisplayName("setAndKeepTTL")
  void setAndKeepTTL() {
    bucket.delete();

    bucket.set(1, 5, TimeUnit.SECONDS);

    sleep(3);

    bucket.setAndKeepTTL(2);

    sleep(1);

    var v1 = bucket.get();
    assertEquals(v1, 2);

    sleep(2);

    var v2 = bucket.get();
    assertNull(v2);
  }

  @Test
  @DisplayName("buckets")
  void buckets() {
    var buckets = redissonClient.getBuckets();

    var vMap = new HashMap<String, String>(4);
    vMap.put("Bucket1", "1");
    vMap.put("Bucket2", "2");
    vMap.put("Bucket3", "3");
    buckets.set(vMap);

    var map = buckets.<String>get("Bucket1", "Bucket2", "Bucket3");
    assertEquals(vMap, map);
  }
}
