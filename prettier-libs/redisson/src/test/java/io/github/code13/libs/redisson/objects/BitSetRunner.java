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

import io.github.code13.libs.redisson.RedissonClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

/**
 * Java implementation of Redis based RBitSet object provides API similar to java.util.BitSet. It
 * represents vector of bits that grows as needed. Size limited by Redis to 4 294 967 295 bits.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 12:27
 */
@DisplayName("BitSet")
class BitSetRunner {
  static RedissonClient redissonClient;
  static RBitSet set;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    set = redissonClient.getBitSet("BitSet");
  }

  @Test
  @DisplayName("example from redisson wiki")
  void test1() {
    set.delete();

    set.set(0, true);
    set.set(1812, false);

    set.clear(0);

    set.and("anotherBitset");
    set.xor("anotherBitset");
  }
}
