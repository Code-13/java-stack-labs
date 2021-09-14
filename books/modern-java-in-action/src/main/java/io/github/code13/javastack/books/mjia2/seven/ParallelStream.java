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

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * ParallelStream.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/14 17:55
 */
public class ParallelStream {

  public long parallelSum(long n) {
    return Stream.iterate(1L, aLong -> aLong + 1)
        .limit(n)
        .parallel() // 将顺序流转换为并行流
        .reduce(0L, Long::sum);
  }

  public long rangedSum(long n) {
    return LongStream.rangeClosed(1, n).reduce(0L, Long::sum);
  }
}
