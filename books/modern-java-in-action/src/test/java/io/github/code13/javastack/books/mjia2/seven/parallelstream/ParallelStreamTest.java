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

package io.github.code13.javastack.books.mjia2.seven.parallelstream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ParallelStreamTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 14:09
 */
class ParallelStreamTest {

  private static final org.slf4j.Logger LOGGER =
      org.slf4j.LoggerFactory.getLogger(ParallelStreamTest.class);

  private ParallelStream parallelStream;

  @BeforeEach
  public void before() {
    parallelStream = new ParallelStream();
  }

  @Test
  public void parallelSum() {
    int n = 10;
    long start = System.currentTimeMillis();
    long l = parallelStream.parallelSum(n);
    long end = System.currentTimeMillis();
    LOGGER.info("耗时为: {}", end - start);
    LOGGER.info("结果为:{}", l);
    Assertions.assertTrue(true);
  }
}
