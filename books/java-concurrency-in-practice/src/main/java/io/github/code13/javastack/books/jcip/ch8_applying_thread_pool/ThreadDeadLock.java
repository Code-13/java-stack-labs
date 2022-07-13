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

package io.github.code13.javastack.books.jcip.ch8_applying_thread_pool;

import io.github.code13.javastack.books.jcip.Terrible;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ThreadDeadlock.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/15/2021 10:41 AM
 */
@Terrible
public class ThreadDeadLock {

  static ExecutorService executor = Executors.newSingleThreadExecutor();

  static class LoadFileTask implements Callable<String> {

    private final String fileName;

    public LoadFileTask(String fileName) {
      this.fileName = fileName;
    }

    @Override
    public String call() throws Exception {
      return "";
    }
  }

  static class RenderPageTask implements Callable<String> {

    @Override
    public String call() throws Exception {

      Future<String> header = executor.submit(new LoadFileTask("header.html"));
      Future<String> footer = executor.submit(new LoadFileTask("footer.html"));

      String page = renderBody();

      // 将发生死锁，因为任务在等待自任务的结果
      return header.get() + page + footer.get();
    }

    private String renderBody() {
      return "";
    }
  }

  @Test
  @DisplayName("test_dead_lock")
  void test_dead_lock() throws Exception {
    RenderPageTask renderPageTask = new RenderPageTask();
    renderPageTask.call();
  }
}
