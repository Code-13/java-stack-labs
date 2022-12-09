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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.github.code13.libs.redisson.RedissonClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

/**
 * Redis based distributed RBloomFilter bloom filter for Java. Number of contained bits is limited
 * to 2^32.
 *
 * <p>Must be initialized with capacity size by tryInit(expectedInsertions, falseProbability) method
 * before usage.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 14:37
 */
@DisplayName("BloomFilter")
class BloomFilterRunner {

  static RedissonClient redissonClient;
  static RBloomFilter<String> bloomFilter;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    bloomFilter = redissonClient.getBloomFilter("BloomFilter");
    bloomFilter.tryInit(550000000, 0.03);
  }

  @Test
  @DisplayName("BloomFilter")
  void bloomFilter() {
    bloomFilter.delete();

    bloomFilter.add("1");
    bloomFilter.add("2");
    bloomFilter.add("3");

    var c1 = bloomFilter.contains("1");
    assertTrue(c1);

    var c2 = bloomFilter.contains("5");
    assertFalse(c2);
  }
}
