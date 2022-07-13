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

package io.github.code13.javastack.libs.redisson.additionalfeatures;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.BatchOptions.ExecutionMode;
import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;

/**
 * ExecutionBatchesOfCommandsRunner.
 *
 * <p>Multiple commands can be sent in a batch using RBatch object in a single network call. Command
 * batches allows to reduce the overall execution time of a group of commands. In Redis this
 * approach called Pipelining.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:22
 */
@DisplayName("Execution batches of commands")
class ExecutionBatchesOfCommandsRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("BatchOptions")
  void batchOptions() {
    BatchOptions options =
        BatchOptions.defaults()
            // Sets execution mode
            //
            // ExecutionMode.REDIS_READ_ATOMIC - Store batched invocations in Redis and execute them
            // atomically as a single command
            //
            // ExecutionMode.REDIS_WRITE_ATOMIC - Store batched invocations in Redis and execute
            // them atomically as a single command
            //
            // ExecutionMode.IN_MEMORY - Store batched invocations in memory on Redisson side and
            // execute them on Redis. Default mode
            //
            // ExecutionMode.IN_MEMORY_ATOMIC - Store batched invocations on Redisson side and
            // executes them atomically on Redis as a single command
            .executionMode(ExecutionMode.IN_MEMORY)

            // Inform Redis not to send reply back to client. This allows to save network traffic
            // for commands with batch with big
            .skipResult()

            // Synchronize write operations execution across defined amount of Redis slave nodes
            //
            // sync with 2 slaves with 1 second for timeout
            .syncSlaves(2, 1, TimeUnit.SECONDS)

            // Response timeout
            .responseTimeout(2, TimeUnit.SECONDS)

            // Retry interval for each attempt to send Redis commands batch
            .retryInterval(2, TimeUnit.SECONDS)

            // Attempts amount to re-send Redis commands batch if it wasn't sent due to network
            // delays or other issues
            .retryAttempts(4);
  }

  @Test
  @DisplayName("Execution batches of commands")
  void example() {
    RBatch batch = redissonClient.createBatch(BatchOptions.defaults());
    batch.getMap("test1").fastPutAsync("1", "2");
    batch.getMap("test2").fastPutAsync("2", "3");
    batch.getMap("test3").putAsync("2", "5");
    RFuture<Double> future = batch.getAtomicDouble("counter").incrementAndGetAsync();
    batch.getAtomicLong("counter").incrementAndGetAsync();

    future.whenComplete(
        (aDouble, throwable) -> {
          //
        });

    BatchResult<?> batchResult = batch.execute();
    List<?> responses = batchResult.getResponses();
  }
}
