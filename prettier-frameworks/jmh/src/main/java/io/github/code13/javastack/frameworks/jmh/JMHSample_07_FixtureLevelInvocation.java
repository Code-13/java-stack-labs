/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.code13.javastack.frameworks.jmh;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
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
 * JMHSample_07_FixtureLevelInvocation.
 *
 * <p>copy from <a
 * href="http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_07_FixtureLevelInvocation.java">JMHSample_07_FixtureLevelInvocation</a>
 *
 * <p>just for study
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/31 10:57
 */
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JMHSample_07_FixtureLevelInvocation {

  /*
   * Fixtures have different Levels to control when they are about to run.
   * Level.Invocation is useful sometimes to do some per-invocation work,
   * which should not count as payload. PLEASE NOTE the timestamping and
   * synchronization for Level.Invocation helpers might significantly offset
   * the measurement, use with care. See Level.Invocation javadoc for further
   * discussion.
   *
   * Consider this sample:
   */

  /*
   * This state handles the executor.
   * Note we create and shutdown executor with Level.Trial, so
   * it is kept around the same across all iterations.
   */

  @State(Scope.Benchmark)
  public static class NormalState {
    ExecutorService service;

    @Setup(Level.Trial)
    public void up() {
      service = Executors.newCachedThreadPool();
    }

    @TearDown(Level.Trial)
    public void down() {
      service.shutdown();
    }
  }

  /*
   * This is the *extension* of the basic state, which also
   * has the Level.Invocation fixture method, sleeping for some time.
   */

  public static class LaggingState extends NormalState {
    public static final int SLEEP_TIME = Integer.getInteger("sleepTime", 10);

    @Setup(Level.Invocation)
    public void lag() throws InterruptedException {
      TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
    }
  }

  /*
   * This allows us to formulate the task: measure the task turnaround in
   * "hot" mode when we are not sleeping between the submits, and "cold" mode,
   * when we are sleeping.
   */

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double measureHot(NormalState e, Scratch s)
      throws ExecutionException, InterruptedException {
    return e.service.submit(new Task(s)).get();
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public double measureCold(LaggingState e, Scratch s)
      throws ExecutionException, InterruptedException {
    return e.service.submit(new Task(s)).get();
  }

  /*
   * This is our scratch state which will handle the work.
   */

  @State(Scope.Thread)
  public static class Scratch {
    private double p;

    public double doWork() {
      p = Math.log(p);
      return p;
    }
  }

  public static class Task implements Callable<Double> {
    private Scratch s;

    public Task(Scratch s) {
      this.s = s;
    }

    @Override
    public Double call() {
      return s.doWork();
    }
  }

  /*
   * ============================== HOW TO RUN THIS TEST: ====================================
   *
   * You can see the cold scenario is running longer, because we pay for
   * thread wakeups.
   *
   * You can run this test:
   *
   * a) Via the command line:
   *    $ mvn clean install
   *    $ java -jar target/benchmarks.jar JMHSample_07 -f 1
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
    String simpleName = JMHSample_07_FixtureLevelInvocation.class.getSimpleName();
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
