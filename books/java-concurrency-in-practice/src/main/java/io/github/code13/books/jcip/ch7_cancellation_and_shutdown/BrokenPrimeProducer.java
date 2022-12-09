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

package io.github.code13.books.jcip.ch7_cancellation_and_shutdown;

import io.github.code13.books.jcip.Terrible;
import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BrokenPrimeProducer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 4:18 PM
 */
@Terrible
public class BrokenPrimeProducer extends Thread {
  private final BlockingQueue<BigInteger> queue;
  private volatile boolean cancelled = false;

  public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    BigInteger p = BigInteger.ONE;
    try {
      while (!cancelled) {
        queue.put(p.nextProbablePrime());
      }
    } catch (InterruptedException consumed) {
    }
  }

  public void cancel() {
    cancelled = true;
  }

  void consumePrimes() throws InterruptedException {
    BlockingQueue<BigInteger> primes = new ArrayBlockingQueue<>(100);
    BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
    producer.start();
    try {
      while (needMorePrimes()) {
        consume(primes.take());
      }
    } finally {
      producer.cancel();
    }
  }

  private boolean needMorePrimes() {
    return true;
  }

  private void consume(BigInteger take) {}
}
