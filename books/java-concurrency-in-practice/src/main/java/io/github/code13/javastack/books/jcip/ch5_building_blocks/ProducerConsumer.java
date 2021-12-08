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

package io.github.code13.javastack.books.jcip.ch5_building_blocks;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FileCrawler.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/8/2021 2:32 PM
 */
public class ProducerConsumer {

  static class FileCrawler implements Runnable {

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
      this.fileQueue = fileQueue;
      this.fileFilter = file -> file.isDirectory() || fileFilter.accept(file);
      this.root = root;
    }

    @Override
    public void run() {}

    private boolean alreadyIndexed(File f) {
      return false;
    }

    private void crawl(File root) throws InterruptedException {
      File[] files = root.listFiles(fileFilter);
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            crawl(file);
          } else if (!alreadyIndexed(file)) {
            fileQueue.put(file);
          }
        }
      }
    }
  }

  static class Indexer implements Runnable {

    private final BlockingQueue<File> queue;

    Indexer(BlockingQueue<File> queue) {
      this.queue = queue;
    }

    @Override
    public void run() {
      try {
        while (true) {
          indexFile(queue.take());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    public void indexFile(File file) {
      // Index the file...
    }
  }

  private static final int BOUND = 10;
  private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

  public static void startIndexing(File[] roots) {
    BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
    FileFilter filter = file -> true;

    for (File root : roots) {
      new Thread(new FileCrawler(queue, filter, root)).start();
    }

    for (int i = 0; i < N_CONSUMERS; i++) {
      new Thread(new Indexer(queue)).start();
    }
  }
}
