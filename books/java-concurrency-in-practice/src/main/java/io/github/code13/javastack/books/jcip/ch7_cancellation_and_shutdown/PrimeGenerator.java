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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import static java.util.concurrent.TimeUnit.SECONDS;
import io.github.code13.javastack.books.jcip.GuardedBy;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * PrimeGenerator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 3:40 PM
 */
public class PrimeGenerator implements Runnable {

  @GuardedBy("this")
  private final List<BigInteger> primes = new ArrayList<>();

  private volatile boolean cancelled;

  @Override
  public void run() {
    BigInteger p = BigInteger.ONE;
    while (!cancelled) {
      p = p.nextProbablePrime();
      synchronized (this) {
        primes.add(p);
      }
    }
  }

  public void cancel() {
    cancelled = true;
  }

  public synchronized List<BigInteger> get() {
    return new ArrayList<>(primes);
  }

  static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
    PrimeGenerator generator = new PrimeGenerator();
    new Thread(generator).start();
    try {
      SECONDS.sleep(1);
    } finally {
      generator.cancel();
    }
    return generator.get();
  }
}
