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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_06_FixtureLevel.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_06_FixtureLevel.java">JMHSample_06_FixtureLevel</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/31 10:33
 */
@State(Scope.Thread)
public class JMHSample_06_FixtureLevel {

  double x;

  /*
   * Fixture methods have different levels to control when they should be run.
   * There are at least three Levels available to the user. These are, from
   * top to bottom:
   *
   * Level.Trial: before or after the entire benchmark run (the sequence of iterations)
   * Level.Iteration: before or after the benchmark iteration (the sequence of invocations)
   * Level.Invocation; before or after the benchmark method invocation (WARNING: read the Javadoc before using)
   *
   * Time spent in fixture methods does not count into the performance
   * metrics, so you can use this to do some heavy-lifting.
   */

  @TearDown(Level.Iteration)
  public void check() {
    assert x > Math.PI : "Nothing changed?";
  }

  @Benchmark
  public void measureRight() {
    x++;
  }

  @Benchmark
  public void measureWrong() {
    double x = 0;
    x++;
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You can see measureRight() yields the result, and measureWrong() fires
   * the assert at the end of first iteration! This will not generate the results
   * for measureWrong(). You can also prevent JMH for proceeding further by
   * requiring "fail on error".
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -ea -jar target/benchmarks.jar JMHSample_06 -f 1
   *    (we requested single fork; there are also other options, see -h)
   *
   *    You can optionally supply -foe to fail the complete run.
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
    String simpleName = JMHSample_06_FixtureLevel.class.getSimpleName();
    Options opt =
        new OptionsBuilder()
            .include(simpleName)
            .forks(1)
            .jvmArgs("-ea")
            .shouldFailOnError(false) // switch to "true" to fail the complete run
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .build();

    new Runner(opt).run();
  }
}
