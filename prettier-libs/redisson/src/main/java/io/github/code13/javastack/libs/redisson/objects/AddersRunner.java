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

import static io.github.code13.javastack.libs.redisson.internal.InternalUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;

/**
 * AddersRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 15:00
 */
@DisplayName("LongAdder and DoubleAdder")
class AddersRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("LongAdder")
  void longAdder() {
    var longAdder = redissonClient.getLongAdder("LongAdder");
    longAdder.increment();
    var sum = longAdder.sum();
    System.out.println(sum);
  }

  @Test
  @DisplayName("LongAdder")
  void longAdderConcurrent() {
    var executorService = Executors.newFixedThreadPool(10);

    var longAdder = redissonClient.getLongAdder("LongAdderConcurrent");

    for (int i = 0; i < 10; i++) {
      executorService.submit(longAdder::increment);
    }

    sleep(1);

    var sum = longAdder.sum();
    assertEquals(sum, 10);
  }

  @Test
  @DisplayName("DoubleAdder")
  void doubleAdder() {
    var doubleAdder = redissonClient.getDoubleAdder("DoubleAdder");
    doubleAdder.increment();
    var sum = doubleAdder.sum();
    System.out.println(sum);
  }
}
