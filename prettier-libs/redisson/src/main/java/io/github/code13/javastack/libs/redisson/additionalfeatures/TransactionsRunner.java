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

package io.github.code13.javastack.libs.redisson.additionalfeatures;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.redisson.transaction.TransactionException;

/**
 * TransactionsRunner.
 *
 * <p>Redisson objects like RMap, RMapCache, RLocalCachedMap, RSet, RSetCache and RBucket could
 * participant in Transaction with ACID properties. It uses locks for write operations and maintains
 * data modification operations list till the commit() operation. On rollback() operation all
 * aquired locks are released. Implementation throws org.redisson.transaction.TransactionException
 * in case of any error during commit/rollback execution.
 *
 * <p>Supported Redis modes: single, master/slave, sentinel, replicated, proxy.
 *
 * <p>Transaction isolation level is: READ_COMMITTED.
 *
 * <p>Execution modes: Sync, Async, Reactive, RxJava3.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:29
 */
@DisplayName("Transactions")
class TransactionsRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("TransactionOptions")
  void transactionOptions() {
    TransactionOptions options =
        TransactionOptions.defaults()
            // Synchronization data timeout between Redis master participating in transaction and
            // its slaves.
            // Default is 5000 milliseconds.
            .syncSlavesTimeout(5, TimeUnit.SECONDS)

            // Response timeout
            // Default is 3000 milliseconds.
            .responseTimeout(3, TimeUnit.SECONDS)

            // Defines time interval for each attempt to send transaction if it hasn't been sent
            // already.
            // Default is 1500 milliseconds.
            .retryInterval(2, TimeUnit.SECONDS)

            // Defines attempts amount to send transaction if it hasn't been sent already.
            // Default is 3 attempts.
            .retryAttempts(3)

            // If transaction hasn't committed within <code>timeout</code> it will rollback
            // automatically.
            // Default is 5000 milliseconds.
            .timeout(5, TimeUnit.SECONDS);
  }

  @Test
  @DisplayName("Code Example")
  void execute() {
    RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());

    RMap<String, String> map = transaction.getMap("myMap");
    map.put("1", "2");
    String value = map.get("3");
    RSet<String> set = transaction.getSet("mySet");
    set.add(value);

    try {
      transaction.commit();
    } catch (TransactionException e) {
      transaction.rollback();
    }
  }
}
