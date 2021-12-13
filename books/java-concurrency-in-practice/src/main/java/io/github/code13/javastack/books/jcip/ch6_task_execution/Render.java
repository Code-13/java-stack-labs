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

import io.github.code13.javastack.books.jcip.ch5_building_blocks.LaunderThrowable;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Render.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 1:38 PM
 */
public abstract class Render {

  private final ExecutorService executor;

  public Render(ExecutorService executor) {
    this.executor = executor;
  }

  void renderPage(CharSequence source) {
    List<ImageInfo> imageInfos = scanForImageInfo(source);
    CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);

    for (ImageInfo imageInfo : imageInfos) {
      completionService.submit(imageInfo::downloadImage);
    }

    renderText(source);

    int size = imageInfos.size();
    try {
      for (int i = 0; i < size; i++) {
        Future<ImageData> imageDataFuture = completionService.take();
        ImageData imageData = imageDataFuture.get();
        renderImage(imageData);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      throw LaunderThrowable.launderThrowable(e);
    }
  }

  protected abstract List<ImageInfo> scanForImageInfo(CharSequence source);

  protected abstract void renderText(CharSequence source);

  protected abstract void renderImage(ImageData imageData);

  interface ImageData {}

  interface ImageInfo {
    ImageData downloadImage();
  }
}
