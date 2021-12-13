/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch6_task_execution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * RenderWithTimeBudget.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 2:32 PM
 */
public class RenderWithTimeBudget {

  private static final Ad DEFAULT_AD = new Ad();
  private static final long TIME_BUDGET = 1000;
  private static final ExecutorService exec = Executors.newCachedThreadPool();

  Page renderPageWithAd() throws InterruptedException {
    long end = System.nanoTime() + TIME_BUDGET;
    Future<Ad> f = exec.submit(Ad::new);
    Page page = renderPageBody();

    Ad ad;
    try {
      long timeLeft = end - System.nanoTime();
      ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
    } catch (ExecutionException e) {
      ad = DEFAULT_AD;
    } catch (TimeoutException e) {
      ad = DEFAULT_AD;
      f.cancel(true);
    }

    page.setAd(ad);
    return page;
  }

  private Page renderPageBody() {
    return new Page();
  }

  static class Ad {}

  static class Page {
    public void setAd(Ad ad) {}
  }
}
