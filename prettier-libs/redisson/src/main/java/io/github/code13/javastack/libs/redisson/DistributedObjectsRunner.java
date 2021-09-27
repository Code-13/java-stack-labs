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

package io.github.code13.javastack.libs.redisson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoPosition;
import org.redisson.api.GeoUnit;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RBucket;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.redisson.api.geo.GeoSearchArgs;

/**
 * Redisson 分布式对象.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/26 22:22
 */
@DisplayName("Redisson 分布式对象")
class DistributedObjectsRunner {

  static RedissonClient redissonClient;

  static {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  /**
   * Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象.
   * 除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口.
   */
  @DisplayName("通用对象桶（Object Bucket）")
  static class ObjectBucketRunner {

    static RBucket<Object> bucket;

    @BeforeAll
    static void setup() {
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

  /**
   * Redisson的分布式RBinaryStream Java对象同时提供了InputStream接口和OutputStream接口的实现. 流的最大容量受Redis主节点的内存大小限制.
   */
  @DisplayName("二进制流（Binary Stream）")
  static class BinaryStreamRunner {

    static RBinaryStream stream;

    @BeforeAll
    static void setup() {
      stream = redissonClient.getBinaryStream("anyStream");
    }

    @Test
    @DisplayName("Binary Stream")
    void stream() throws IOException {
      byte[] bytes = {1, 1, 1, 1};
      stream.set(bytes);

      var in = stream.getInputStream();
      byte[] readBuffer = new byte[512];
      in.read(readBuffer);

      var out = stream.getOutputStream();
      byte[] contentToWrite = {2, 2, 2, 2};
      out.write(contentToWrite);
    }
  }

  /**
   * Redisson的分布式RGeo Java对象是一种专门用来储存与地理位置有关的对象桶。除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口.
   */
  @DisplayName("地理空间对象桶（Geospatial Bucket）")
  static class GeospatialBucketRunner {

    static RGeo<String> geo;

    @BeforeAll
    static void setup() {
      geo = redissonClient.getGeo("GeoTest");
    }

    @Test
    @DisplayName("Geospatial")
    void geo() {
      geo.delete();

      geo.add(
          new GeoEntry(13.361389, 38.115556, "Palermo"),
          new GeoEntry(15.087269, 37.502669, "Catania"));

      geo.add(37.618423, 55.751244, "Moscow");

      Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS);

      System.out.println(distance);

      Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Catania", "test1");

      System.out.println(positions);

      var search = geo.search(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
      System.out.println(search);

      var search1 = geo.search(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS).count(1));
      System.out.println(search1);

      var searchWithDistance =
          geo.searchWithDistance(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
      System.out.println(searchWithDistance);

      var searchWithPosition =
          geo.searchWithPosition(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
      System.out.println(searchWithPosition);
    }
  }

  static void sleep(int timeout) {
    try {
      TimeUnit.SECONDS.sleep(timeout);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
