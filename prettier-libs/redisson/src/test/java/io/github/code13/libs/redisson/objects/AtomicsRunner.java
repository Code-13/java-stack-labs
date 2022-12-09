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

import static org.junit.jupiter.api.Assertions.assertEquals;
import io.github.code13.libs.redisson.RedissonClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;

/**
 * AtomicLong and AtomicDouble.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 14:28
 */
@DisplayName("AtomicLong and AtomicDouble")
class AtomicsRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("AtomicLong")
  void atomicLong() {
    var atomicLong = redissonClient.getAtomicLong("AtomicLong");
    atomicLong.set(3);
    var v = atomicLong.incrementAndGet();

    assertEquals(v, 4);
  }

  @Test
  @DisplayName("AtomicDouble")
  void atomicDouble() {
    var atomicDouble = redissonClient.getAtomicDouble("AtomicDouble");
    atomicDouble.delete();

    var v = atomicDouble.incrementAndGet();

    assertEquals(v, 1.0d);
  }
}
