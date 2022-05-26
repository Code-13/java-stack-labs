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
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMHSample_01_HelloWorld.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_01_HelloWorld.java">JMHSample_01_HelloWorld</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/26 09:08
 */
public class JMHSample_01_HelloWorld {

  /*
   * This is our first benchmark method.
   *
   * JMH works as follows: users annotate the methods with @Benchmark, and
   * then JMH produces the generated code to run this particular benchmark as
   * reliably as possible. In general one might think about @Benchmark methods
   * as the benchmark "payload", the things we want to measure. The
   * surrounding infrastructure is provided by the harness itself.
   *
   * Read the Javadoc for @Benchmark annotation for complete semantics and
   * restrictions. At this point we only note that the methods names are
   * non-essential, and it only matters that the methods are marked with
   * @Benchmark. You can have multiple benchmark methods within the same
   * class.
   *
   * Note: if the benchmark method never finishes, then JMH run never finishes
   * as well. If you throw an exception from the method body the JMH run ends
   * abruptly for this benchmark and JMH will run the next benchmark down the
   * list.
   *
   * Although this benchmark measures "nothing" it is a good showcase for the
   * overheads the infrastructure bear on the code you measure in the method.
   * There are no magical infrastructures which incur no overhead, and it is
   * important to know what are the infra overheads you are dealing with. You
   * might find this thought unfolded in future examples by having the
   * "baseline" measurements to compare against.
   */

  @Benchmark
  public void wellHelloThere() {
    // this method was intentionally left blank.
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

    String simpleName = JMHSample_01_HelloWorld.class.getSimpleName();

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
