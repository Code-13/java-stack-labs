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

package io.github.code13.javastack.books.mjia2.seven;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ForkJoinSumCalculatorTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/14 17:52
 */
class ForkJoinSumCalculatorTest {

  private static final long THRESHOLD = 10000000L;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void compute() {
    long[] longs = LongStream.rangeClosed(1L, THRESHOLD).toArray();
    ForkJoinTask<Long> forkJoinSumCalculator = new ForkJoinSumCalculator(longs);
    Long invoke = ForkJoinPool.commonPool().invoke(forkJoinSumCalculator);
    System.out.println(invoke);
  }
}
