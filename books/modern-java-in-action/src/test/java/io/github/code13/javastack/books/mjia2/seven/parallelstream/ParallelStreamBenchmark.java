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

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * ParallelStreamBenchmark.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/14 17:56
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(
    value = 2,
    jvmArgs = {"-Xms4G", "-Xmx4G"})
public class ParallelStreamBenchmark {

  private static final long N = 10000000L;

  @Benchmark
  public long sequentialSum() {
    return Stream.iterate(1L, aLong -> aLong + 1).limit(N).reduce(0L, Long::sum);
  }

  @Benchmark
  public long parallelSum() {
    return Stream.iterate(1L, aLong -> aLong + 1).limit(N).parallel().reduce(0L, Long::sum);
  }

  @Benchmark
  public long rangedSum() {
    return LongStream.rangeClosed(1, N).reduce(0L, Long::sum);
  }

  @Benchmark
  public long rangedParallelSum() {
    return LongStream.rangeClosed(1, N).parallel().reduce(0L, Long::sum);
  }

  @TearDown(Level.Invocation)
  public void tearDown() {
    System.gc();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(ParallelStreamBenchmark.class.getSimpleName())
            .result("result.json")
            .resultFormat(ResultFormatType.JSON)
            .build();
    new Runner(opt).run();

    // Benchmark                                  Mode  Cnt   Score    Error  Units
    // ParallelStreamBenchmark.parallelSum        avgt   10  92.903 ± 10.458  ms/op
    // ParallelStreamBenchmark.rangedParallelSum  avgt   10   2.712 ±  3.157  ms/op
    // ParallelStreamBenchmark.rangedSum          avgt   10   7.051 ±  0.041  ms/op
    // ParallelStreamBenchmark.sequentialSum      avgt   10  89.648 ±  0.583  ms/op
  }
}
