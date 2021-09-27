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

package io.github.code13.javastack.libs.redisson.objects;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;

/**
 * Redis based Java Id generator RReliableTopic generates unique numbers but not monotonically
 * increased. At first request batch of id numbers is allocated and cached on Java side till it's
 * exhausted. This approach allows to generate ids faster than RAtomicLong.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 20:35
 */
@DisplayName("IdGenerator")
class IdGeneratorRunner {

  static RedissonClient redissonClient;
  static RIdGenerator idGenerator;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    idGenerator = redissonClient.getIdGenerator("IdGenerator");
  }

  @Test
  @DisplayName("IdGenerator")
  void idGenerator() {
    // Initialize with start value = 12 and allocation size = 20000
    idGenerator.tryInit(12, 20000);

    for (int i = 0; i < 10000; i++) {
      var id = idGenerator.nextId();
      System.out.println(id);
    }
  }
}
