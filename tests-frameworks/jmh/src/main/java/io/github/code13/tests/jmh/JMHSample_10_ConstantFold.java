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
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_10_ConstantFold.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_10_ConstantFold.java">JMHSample_10_ConstantFold</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/31 20:49
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHSample_10_ConstantFold {

  /*
   * The flip side of dead-code elimination is constant-folding.
   *
   * If JVM realizes the result of the computation is the same no matter what,
   * it can cleverly optimize it. In our case, that means we can move the
   * computation outside of the internal JMH loop.
   *
   * This can be prevented by always reading the inputs from non-final
   * instance fields of @State objects, computing the result based on those
   * values, and follow the rules to prevent DCE.
   */

  // IDEs will say "Oh, you can convert this field to local variable". Don't. Trust. Them.
  // (While this is normally fine advice, it does not work in the context of measuring correctly.)
  private double x = Math.PI;

  // IDEs will probably also say "Look, it could be final". Don't. Trust. Them. Either.
  // (While this is normally fine advice, it does not work in the context of measuring correctly.)
  private final double wrongX = Math.PI;

  @Benchmark
  public double baseline() {
    // simply return the value, this is a baseline
    return Math.PI;
  }

  @Benchmark
  public double measureWrong_1() {
    // This is wrong: the source is predictable, and computation is foldable.
    return Math.log(Math.PI);
  }

  @Benchmark
  public double measureWrong_2() {
    // This is wrong: the source is predictable, and computation is foldable.
    return Math.log(wrongX);
  }

  @Benchmark
  public double measureRight() {
    // This is correct: the source is not predictable.
    return Math.log(x);
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You can see the unrealistically fast calculation in with measureWrong_*(),
   * while realistic measurement with measureRight().
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_10 -i 5 -f 1
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
    String simpleName = JMHSample_10_ConstantFold.class.getSimpleName();
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
