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

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;

/**
 * SetRunner.
 *
 * <p>Redis based Set object for Java implements java.util.Set interface. Keeps elements uniqueness
 * via element state comparison. Set size limited by Redis to 4 294 967 295 elements.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/29 11:37
 */
@DisplayName("Set")
class SetRunner {

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
  @DisplayName("Set")
  void test1() {
    var set = redissonClient.<String>getSet("Set");
    set.add("1");
    set.remove("1");

    var lock = set.getLock("1");
    lock.lock();
    try {
      //
    } finally {
      lock.unlock();
    }
  }
}
