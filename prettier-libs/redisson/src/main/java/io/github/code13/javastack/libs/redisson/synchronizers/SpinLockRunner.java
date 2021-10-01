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

package io.github.code13.javastack.libs.redisson.synchronizers;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * SpinLockRunner.
 *
 * <p>Redis based distributed reentrant SpinLock object for Java and implements Lock interface.
 *
 * <p>Thousands or more locks acquired/released per short time interval may cause reaching of
 * network throughput limit and Redis CPU overload because of pubsub usage in Lock object. This
 * occurs due to nature of Redis pubsub - messages are distributed to all nodes in Redis cluster.
 * Spin Lock uses Exponential Backoff strategy by default for lock acquisition instead of pubsub
 * channel.
 *
 * <p>If Redisson instance which acquired lock crashes then such lock could hang forever in acquired
 * state. To avoid this Redisson maintains lock watchdog, it prolongs lock expiration while lock
 * holder Redisson instance is alive. By default lock watchdog timeout is 30 seconds and can be
 * changed through Config.lockWatchdogTimeout setting.
 *
 * <p>Also Redisson allow to specify leaseTime parameter during lock acquisition. After specified
 * time interval locked lock will be released automatically.
 *
 * <p>RLock object behaves according to the Java Lock specification. It means only lock owner thread
 * can unlock it otherwise IllegalMonitorStateException would be thrown. Otherwise consider to use
 * RSemaphore object.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:18
 */
@DisplayName("Spin Lock")
class SpinLockRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("Spin Lock")
  void execute() throws InterruptedException {
    RLock lock = redissonClient.getSpinLock("SpinLock");

    // traditional lock method
    lock.lock();

    // or acquire lock and automatically unlock it after 10 seconds
    lock.lock(10, TimeUnit.SECONDS);

    // or wait for lock aquisition up to 100 seconds
    // and automatically unlock it after 10 seconds
    boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
    if (res) {
      try {
        System.out.println("res = " + res);
      } finally {
        lock.unlock();
      }
    }
  }
}
