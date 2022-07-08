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

package io.github.code13.javastack.tests.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_02_BenchmarkModes.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_02_BenchmarkModes.java">JMHSample_02_BenchmarkModes</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/26 13:47
 */
public class JMHSample_02_BenchmarkModes {

  /*
   * JMH generates lots of synthetic code for the benchmarks for you during
   * the benchmark compilation. JMH can measure the benchmark methods in lots
   * of modes. Users may select the default benchmark mode with a special
   * annotation, or select/override the mode via the runtime options.
   *
   * With this scenario, we start to measure something useful. Note that our
   * payload code potentially throws exceptions, and we can just declare them
   * to be thrown. If the code throws the actual exception, the benchmark
   * execution will stop with an error.
   *
   * When you are puzzled with some particular behavior, it usually helps to
   * look into the generated code. You might see the code is doing not
   * something you intend it to do. Good experiments always follow up on the
   * experimental setup, and cross-checking the generated code is an important
   * part of that follow up.
   *
   * The generated code for this particular sample is somewhere at
   * target/generated-sources/annotations/.../JMHSample_02_BenchmarkModes.java
   */

  /*
   * Mode.Throughput, as stated in its Javadoc, measures the raw throughput by
   * continuously calling the benchmark method in a time-bound iteration, and
   * counting how many times we executed the method.
   *
   * We are using the special annotation to select the units to measure in,
   * although you can use the default.
   */

  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void measureThroughput() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * Mode.AverageTime measures the average execution time, and it does it
   * in the way similar to Mode.Throughput.
   *
   * Some might say it is the reciprocal throughput, and it really is.
   * There are workloads where measuring times is more convenient though.
   */

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void measureAvgTime() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * Mode.SampleTime samples the execution time. With this mode, we are
   * still running the method in a time-bound iteration, but instead of
   * measuring the total time, we measure the time spent in *some* of
   * the benchmark method calls.
   *
   * This allows us to infer the distributions, percentiles, etc.
   *
   * JMH also tries to auto-adjust sampling frequency: if the method
   * is long enough, you will end up capturing all the samples.
   */

  @Benchmark
  @BenchmarkMode(Mode.SampleTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void measureSampleTime() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * Mode.SingleShotTime measures the single method invocation time. As the Javadoc
   * suggests, we do only the single benchmark method invocation. The iteration
   * time is meaningless in this mode: as soon as benchmark method stops, the
   * iteration is over.
   *
   * This mode is useful to do cold startup tests, when you specifically
   * do not want to call the benchmark method continuously.
   */

  @Benchmark
  @BenchmarkMode(Mode.SingleShotTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void measureSingleShotTime() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * We can also ask for multiple benchmark modes at once. All the tests
   * above can be replaced with just a single test like this:
   */

  @Benchmark
  @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void measureMultiple() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * Or even...
   */

  @Benchmark
  @BenchmarkMode(Mode.All)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void measureAll() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(100);
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You are expected to see the run with large number of iterations, and
   * very large throughput numbers. You can see that as the estimate of the
   * harness overheads per method call. In most of our measurements, it is
   * down to several cycles per call.
   *
   * a) Via command-line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_01
   *
   * JMH generates self-contained JARs, bundling JMH together with it.
   * The runtime options for the JMH are available with "-h":
   *    $ java -jar target/benchmarks.jar -h
   *
   * b) Via the Java API:
   *    (see the JMH homepage for possible caveats when running from IDE:
   *      http://openjdk.java.net/projects/code-tools/jmh/)
   */

  /*
   * Idea 下载 JMH 插件：
   *        https://plugins.jetbrains.com/plugin/7529-jmh-java-microbenchmark-harness
   * 就可以直接下面的运行下面的 main 方法。注意请直接使用 run 模式，不要使用 debug 模式！！！
   *
   * 所有的报告均在 prettier-frameworks/jmh/result 目录下
   */

  public static void main(String[] args) throws RunnerException {
    String simpleName = JMHSample_02_BenchmarkModes.class.getSimpleName();

    Options options =
        new OptionsBuilder()
            .include(simpleName)
            .forks(1)
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .build();

    new Runner(options).run();
  }
}
