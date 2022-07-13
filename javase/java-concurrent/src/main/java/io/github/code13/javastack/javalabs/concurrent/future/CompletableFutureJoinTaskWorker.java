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

package io.github.code13.javastack.javalabs.concurrent.future;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * 使用 {@link CompletableFuture} 将任务先分批次请求再合并.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/27 20:27
 */
public class CompletableFutureJoinTaskWorker<T, R> {

  private final List<T> taskList;
  private final Function<T, CompletableFuture<R>> workFunction;

  public CompletableFutureJoinTaskWorker(
      List<T> taskList, Function<T, CompletableFuture<R>> workFunction) {
    this.taskList = Objects.requireNonNull(taskList);
    this.workFunction = Objects.requireNonNull(workFunction);
  }

  public List<R> getAllResult() {
    List<CompletableFuture<R>> completableFutures = taskList.stream().map(workFunction).toList();

    CompletableFuture<Void> all =
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

    return all.thenApply(
            unused -> completableFutures.stream().map(CompletableFuture::join).toList())
        .join();
  }
}
