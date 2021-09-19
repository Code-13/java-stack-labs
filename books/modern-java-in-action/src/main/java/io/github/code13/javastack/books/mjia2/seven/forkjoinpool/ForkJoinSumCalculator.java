/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.mjia2.seven.forkjoinpool;

import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinSumCalculator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/14 17:50
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

  private static final long serialVersionUID = -8997796050773034749L;

  private static final org.slf4j.Logger LOGGER =
      org.slf4j.LoggerFactory.getLogger(ForkJoinSumCalculator.class);

  private static final long THRESHOLD = 10000L;

  private final long[] numbers;
  private final int start;
  private final int end;

  public ForkJoinSumCalculator(long[] numbers) {
    this(numbers, 0, numbers.length);
  }

  private ForkJoinSumCalculator(long[] numbers, int start, int end) {
    this.numbers = numbers;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Long compute() {

    int length = end - start;

    // 如果小于或等于阈值，就计算结果
    if (length <= THRESHOLD) {
      LOGGER.info("阈值已过开始计算");
      return computeSequentially();
    }

    LOGGER.info("尚未超过阈值");

    // 创建子任务来为数组的前一半来求值
    ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);

    // 利用 ForkJoinPool 的另一个线程异步地执行新建的子任务
    leftTask.fork();

    // 创建一个子任务来为数组的后一半求和
    ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);

    // 当前线程同步执行第二个子任务，有可能进行进一步的递归划分
    Long rightResult = rightTask.compute();

    // 读取第一个子任务的结果，如果还未完成就等待
    Long leftResult = leftTask.join();

    // 整合结果
    return rightResult + leftResult;
  }

  /**
   * 任务足够小时采用的简单算法.
   *
   * @return {@link long} 计算结果
   */
  private long computeSequentially() {
    long sum = 0;
    for (int i = start; i < end; i++) {
      sum += numbers[i];
    }
    return sum;
  }
}
