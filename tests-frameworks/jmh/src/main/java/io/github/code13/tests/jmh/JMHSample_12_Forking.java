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

package io.github.code13.tests.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_12_Forking.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/6 17:10
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_12_Forking {

  /*
   * JVMs are notoriously good at profile-guided optimizations. This is bad
   * for benchmarks, because different tests can mix their profiles together,
   * and then render the "uniformly bad" code for every test. Forking (running
   * in a separate process) each test can help to evade this issue.
   *
   * JMH will fork the tests by default.
   */

  /*
   * Suppose we have this simple counter interface, and two implementations.
   * Even though those are semantically the same, from the JVM standpoint,
   * those are distinct classes.
   */

  public interface Counter {
    int inc();
  }

  public static class Counter1 implements Counter {
    private int x;

    @Override
    public int inc() {
      return x++;
    }
  }

  public static class Counter2 implements Counter {
    private int x;

    @Override
    public int inc() {
      return x++;
    }
  }

  /*
   * And this is how we measure it.
   * Note this is susceptible for same issue with loops we mention in previous examples.
   */

  public int measure(Counter c) {
    int s = 0;
    for (int i = 0; i < 10; i++) {
      s += c.inc();
    }
    return s;
  }

  /*
   * These are two counters.
   */
  Counter c1 = new Counter1();
  Counter c2 = new Counter2();

  /*
   * We first measure the Counter1 alone...
   * Fork(0) helps to run in the same JVM.
   */

  @Benchmark
  @Fork(0)
  public int measure_1_c1() {
    return measure(c1);
  }

  /*
   * Then Counter2...
   */

  @Benchmark
  @Fork(0)
  public int measure_2_c2() {
    return measure(c2);
  }

  /*
   * Then Counter1 again...
   */

  @Benchmark
  @Fork(0)
  public int measure_3_c1_again() {
    return measure(c1);
  }

  /*
   * These two tests have explicit @Fork annotation.
   * JMH takes this annotation as the request to run the test in the forked JVM.
   * It's even simpler to force this behavior for all the tests via the command
   * line option "-f". The forking is default, but we still use the annotation
   * for the consistency.
   *
   * This is the test for Counter1.
   */

  @Benchmark
  @Fork(1)
  public int measure_4_forked_c1() {
    return measure(c1);
  }

  /*
   * ...and this is the test for Counter2.
   */

  @Benchmark
  @Fork(1)
  public int measure_5_forked_c2() {
    return measure(c2);
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * Note that C1 is faster, C2 is slower, but the C1 is slow again! This is because
   * the profiles for C1 and C2 had merged together. Notice how flawless the measurement
   * is for forked runs.
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_12
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
    String simpleName = JMHSample_12_Forking.class.getSimpleName();
    Options opt =
        new OptionsBuilder()
            .include(simpleName)
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .build();

    new Runner(opt).run();
  }
}
