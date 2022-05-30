/*
 *
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

package io.github.code13.javastack.frameworks.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_05_StateFixtures.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/30 22:10
 */
@State(Scope.Thread)
public class JMHSample_05_StateFixtures {

  double x;

  /*
   * Since @State objects are kept around during the lifetime of the
   * benchmark, it helps to have the methods which do state housekeeping.
   * These are usual fixture methods, you are probably familiar with them from
   * JUnit and TestNG.
   *
   * Fixture methods make sense only on @State objects, and JMH will fail to
   * compile the test otherwise.
   *
   * As with the State, fixture methods are only called by those benchmark
   * threads which are using the state. That means you can operate in the
   * thread-local context, and (not) use synchronization as if you are
   * executing in the context of benchmark thread.
   *
   * Note: fixture methods can also work with static fields, although the
   * semantics of these operations fall back out of State scope, and obey
   * usual Java rules (i.e. one static field per class).
   */

  /*
   * Ok, let's prepare our benchmark:
   */

  @Setup
  public void prepare() {
    x = Math.PI;
  }

  /*
   * And, check the benchmark went fine afterwards:
   */

  @TearDown
  public void check() {
    assert x > Math.PI : "Nothing changed?";
  }

  /*
   * This method obviously does the right thing, incrementing the field x
   * in the benchmark state. check() will never fail this way, because
   * we are always guaranteed to have at least one benchmark call.
   */

  @Benchmark
  public void measureRight() {
    x++;
  }

  /*
   * This method, however, will fail the check(), because we deliberately
   * have the "typo", and increment only the local variable. This should
   * not pass the check, and JMH will fail the run.
   */

  @Benchmark
  public void measureWrong() {
    double x = 0;
    x++;
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You can see measureRight() yields the result, and measureWrong() fires
   * the assert at the end of the run.
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -ea -jar target/benchmarks.jar JMHSample_05 -f 1
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
    String simpleName = JMHSample_05_StateFixtures.class.getSimpleName();
    Options opt =
        new OptionsBuilder()
            .include(simpleName)
            .forks(1)
            .resultFormat(ResultFormatType.JSON)
            .result("prettier-frameworks/jmh/result/" + simpleName + ".json")
            .jvmArgs("-ea")
            .build();

    new Runner(opt).run();
  }
}
