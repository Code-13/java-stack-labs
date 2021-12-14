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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WebCrawler.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 10:59 AM
 */
public abstract class WebCrawler {

  private static final long TIMEOUT = 1000;
  private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
  private volatile TrackingExecutor executor;
  private final Set<URL> urlsToCrawl = new HashSet<>();

  public synchronized void start() {
    executor = new TrackingExecutor(Executors.newCachedThreadPool());
    for (URL url : urlsToCrawl) {
      submitCrawTask(url);
    }
    urlsToCrawl.clear();
  }

  public synchronized void stop() throws InterruptedException {
    try {
      saveUncrawled(executor.shutdownNow());
      if (executor.awaitTermination(TIMEOUT, UNIT)) {
        saveUncrawled(executor.getCancelledTasks());
      }
    } finally {
      executor = null;
    }
  }

  private void saveUncrawled(List<Runnable> uncrawled) {
    for (Runnable runnable : uncrawled) {
      urlsToCrawl.add(((CrawlTask) runnable).getPage());
    }
  }

  protected void submitCrawTask(URL url) {
    executor.execute(new CrawlTask(url));
  }

  protected abstract List<URL> processPage(URL url);

  private class CrawlTask implements Runnable {

    private final URL url;

    private CrawlTask(URL url) {
      this.url = url;
    }

    @Override
    public void run() {
      for (URL link : processPage(url)) {
        if (Thread.currentThread().isInterrupted()) {
          return;
        }
        submitCrawTask(link);
      }
    }

    public URL getPage() {
      return url;
    }
  }
}
