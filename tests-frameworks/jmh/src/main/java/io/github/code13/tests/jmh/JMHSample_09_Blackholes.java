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
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_09_Blackholes.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_09_Blackholes.java">JMHSample_09_Blackholes</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/31 13:22
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMHSample_09_Blackholes {

  /*
   * Should your benchmark require returning multiple results, you have to
   * consider two options (detailed below).
   *
   * NOTE: If you are only producing a single result, it is more readable to
   * use the implicit return, as in JMHSample_08_DeadCode. Do not make your benchmark
   * code less readable with explicit Blackholes!
   */

  double x1 = Math.PI;
  double x2 = Math.PI * 2;

  /*
   * Baseline measurement: how much single Math.log costs.
   */

  @Benchmark
  public double baseline() {
    return Math.log(x1);
  }

  /*
   * While the Math.log(x2) computation is intact, Math.log(x1)
   * is redundant and optimized out.
   */

  @Benchmark
  public double measureWrong() {
    Math.log(x1);
    return Math.log(x2);
  }

  /*
   * This demonstrates Option A:
   *
   * Merge multiple results into one and return it.
   * This is OK when is computation is relatively heavyweight, and merging
   * the results does not offset the results much.
   */

  @Benchmark
  public double measureRight_1() {
    return Math.log(x1) + Math.log(x2);
  }

  /*
   * This demonstrates Option B:
   *
   * Use explicit Blackhole objects, and sink the values there.
   * (Background: Blackhole is just another @State object, bundled with JMH).
   */

  @Benchmark
  public void measureRight_2(Blackhole bh) {
    bh.consume(Math.log(x1));
    bh.consume(Math.log(x2));
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You will see measureWrong() running on-par with baseline().
   * Both measureRight() are measuring twice the baseline, so the logs are intact.
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_09 -f 1
   *    (we requested single fork; there are also other options, see -h)
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
    String simpleName = JMHSample_09_Blackholes.class.getSimpleName();
    Options opt =
        new OptionsBuilder()
            .include(simpleName)
            .forks(1)
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .build();

    new Runner(opt).run();
  }
}
