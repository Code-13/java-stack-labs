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
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RedissonClient;

/**
 * Redis based distributed RHyperLogLog object for Java. Probabilistic data structure that lets you
 * maintain counts of millions of items with extreme space efficiency.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 14:47
 */
@DisplayName("HyperLogLog")
class HyperLogLogRunner {

  static RedissonClient redissonClient;
  static RHyperLogLog<Integer> hyperLogLog;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    hyperLogLog = redissonClient.getHyperLogLog("HyperLogLog");
  }

  @Test
  @DisplayName("HyperLogLog")
  void hyperLogLog() {

    hyperLogLog.add(1);
    hyperLogLog.add(2);
    hyperLogLog.add(3);
    hyperLogLog.add(4);

    var count = hyperLogLog.count();
    System.out.println(count);
  }
}
