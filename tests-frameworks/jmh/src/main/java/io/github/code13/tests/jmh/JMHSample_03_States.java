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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_03_States.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_03_States.java">JMHSample_03_States</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/26 15:26
 */
public class JMHSample_03_States {

  /*
   * Most of the time, you need to maintain some state while the benchmark is
   * running. Since JMH is heavily used to build concurrent benchmarks, we
   * opted for an explicit notion of state-bearing objects.
   *
   * Below are two state objects. Their class names are not essential, it
   * matters they are marked with @State. These objects will be instantiated
   * on demand, and reused during the entire benchmark trial.
   *
   * The important property is that state is always instantiated by one of
   * those benchmark threads which will then have the access to that state.
   * That means you can initialize the fields as if you do that in worker
   * threads (ThreadLocals are yours, etc).
   */

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    volatile double x = Math.PI;
  }

  @State(Scope.Thread)
  public static class ThreadState {
    volatile double x = Math.PI;
  }

  /*
   * Benchmark methods can reference the states, and JMH will inject the
   * appropriate states while calling these methods. You can have no states at
   * all, or have only one state, or have multiple states referenced. This
   * makes building multi-threaded benchmark a breeze.
   *
   * For this exercise, we have two methods.
   */

  @Benchmark
  public void measureUnshared(ThreadState state) {
    // All benchmark threads will call in this method.
    //
    // However, since ThreadState is the Scope.Thread, each thread
    // will have it's own copy of the state, and this benchmark
    // will measure unshared case.
    state.x++;
  }

  @Benchmark
  public void measureShared(BenchmarkState state) {
    // All benchmark threads will call in this method.
    //
    // Since BenchmarkState is the Scope.Benchmark, all threads
    // will share the state instance, and we will end up measuring
    // shared case.
    state.x++;
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You are expected to see the drastic difference in shared and unshared cases,
   * because you either contend for single memory location, or not. This effect
   * is more articulated on large machines.
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_03 -t 4 -f 1
   *    (we requested 4 threads, single fork; there are also other options, see -h)
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
    String simpleName = JMHSample_03_States.class.getSimpleName();

    Options opt =
        new OptionsBuilder()
            .include(simpleName)
            .threads(4)
            .forks(1)
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .build();

    new Runner(opt).run();
  }
}
