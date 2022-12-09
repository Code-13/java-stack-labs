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

package io.github.code13.libs.redisson.synchronizers;

import io.github.code13.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * MultiLockRunner.
 *
 * <p>Redis based distributed MultiLock object allows to group Lock objects and handle them as a
 * single lock. Each RLock object may belong to different Redisson instances.
 *
 * <p>If Redisson instance which acquired MultiLock crashes then such MultiLock could hang forever
 * in acquired state. To avoid this Redisson maintains lock watchdog, it prolongs lock expiration
 * while lock holder Redisson instance is alive. By default lock watchdog timeout is 30 seconds and
 * can be changed through Config.lockWatchdogTimeout setting.
 *
 * <p>Also Redisson allow to specify leaseTime parameter during lock acquisition. After specified
 * time interval locked lock will be released automatically.
 *
 * <p>MultiLock object behaves according to the Java Lock specification. It means only lock owner
 * thread can unlock it otherwise IllegalMonitorStateException would be thrown. Otherwise consider
 * to use RSemaphore object.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 17:04
 */
@DisplayName("MultiLock")
class MultiLockRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("traditional lock method")
  void lock1() {
    RLock lock1 = redissonClient.getLock("Lock1");
    RLock lock2 = redissonClient.getLock("Lock2");
    RLock lock3 = redissonClient.getLock("Lock3");

    RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3);

    multiLock.lock();
    try {
      // do something
    } finally {
      multiLock.unlock();
    }
  }

  @Test
  @DisplayName("acquire lock and automatically unlock it after 10 second")
  void lock2() {
    RLock lock1 = redissonClient.getLock("Lock1");
    RLock lock2 = redissonClient.getLock("Lock2");
    RLock lock3 = redissonClient.getLock("Lock3");

    RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3);

    multiLock.lock(10, TimeUnit.SECONDS);
    try {
      // do something
    } finally {
      multiLock.unlock();
    }
  }

  @Test
  @DisplayName(
      """
      wait for lock aquisition up to 100 seconds
      and automatically unlock it after 10 seconds
      """)
  void lock3() throws InterruptedException {
    RLock lock1 = redissonClient.getLock("Lock1");
    RLock lock2 = redissonClient.getLock("Lock2");
    RLock lock3 = redissonClient.getLock("Lock3");

    RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3);
    boolean tryLock = multiLock.tryLock(100, 10, TimeUnit.SECONDS);
    if (tryLock) {
      try {
        System.out.println("1");
      } finally {
        multiLock.unlock();
      }
    }
  }
}
