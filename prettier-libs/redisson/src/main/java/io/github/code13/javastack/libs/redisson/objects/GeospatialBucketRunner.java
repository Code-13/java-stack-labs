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

package io.github.code13.javastack.libs.redisson.objects;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoPosition;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.redisson.api.geo.GeoSearchArgs;

/**
 * Redisson的分布式RGeo Java对象是一种专门用来储存与地理位置有关的对象桶。除了同步接口外，还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口..
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 12:25
 */
@DisplayName("地理空间对象桶（Geospatial Bucket）")
class GeospatialBucketRunner {

  static RedissonClient redissonClient;
  static RGeo<String> geo;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
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
