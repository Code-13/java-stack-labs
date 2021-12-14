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

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

/**
 * IndexingService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 10:20 AM
 */
public class IndexingService {

  private static final File POISON = new File("");
  private final CrawlerThread crawlerThread = new CrawlerThread();
  private final IndexerThread indexerThread = new IndexerThread();
  private final BlockingQueue<File> queue;
  private final FileFilter fileFilter;
  private final File root;

  public IndexingService(BlockingQueue<File> queue, FileFilter fileFilter, File root) {
    this.queue = queue;
    this.fileFilter = fileFilter;
    this.root = root;
  }

  public void start() {
    crawlerThread.start();
    indexerThread.start();
  }

  class CrawlerThread extends Thread {

    @Override
    public void run() {
      try {
        crawl(root);
      } catch (InterruptedException ignored) {
        // retry
      } finally {
        while (true) {
          try {
            queue.put(POISON);
          } catch (InterruptedException ignored) {
            // retry
          }
        }
      }
    }

    private void crawl(File root) throws InterruptedException {
      //
    }
  }

  class IndexerThread extends Thread {

    @Override
    public void run() {
      try {
        while (true) {
          File file = queue.take();
          if (file == POISON) {
            break;
          }
          indexFile(file);
        }
      } catch (InterruptedException ignored) {
        // ignored
      }
    }

    private void indexFile(File file) {
      //
    }
  }
}
