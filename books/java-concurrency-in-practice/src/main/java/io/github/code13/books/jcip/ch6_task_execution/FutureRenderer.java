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

package io.github.code13.books.jcip.ch6_task_execution;

import io.github.code13.books.jcip.ch5_building_blocks.LaunderThrowable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * SingleThreadRenderer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 11:40 AM
 */
public abstract class FutureRenderer {

  private final ExecutorService executor = Executors.newFixedThreadPool(100);

  void renderPage(CharSequence source) {

    List<ImageInfo> imageInfos = scanForImageInfo(source);

    Future<List<ImageData>> future =
        executor.submit(
            () -> {
              List<ImageData> imageDataList = new ArrayList<>();
              for (ImageInfo imageInfo : imageInfos) {
                imageDataList.add(imageInfo.downloadImage());
              }
              return imageDataList;
            });

    rendText(source);

    try {
      List<ImageData> imageDataList = future.get();
      for (ImageData imageData : imageDataList) {
        renderImage(imageData);
      }
    } catch (InterruptedException e) {
      // 重新设置线程的中断状态
      Thread.currentThread().interrupt();
      // 由于不需要结果，因此取消任务
      future.cancel(true);
    } catch (ExecutionException e) {
      throw LaunderThrowable.launderThrowable(e);
    }
  }

  abstract void rendText(CharSequence source);

  abstract List<ImageInfo> scanForImageInfo(CharSequence source);

  abstract void renderImage(ImageData imageData);

  interface ImageData {}

  interface ImageInfo {
    ImageData downloadImage();
  }
}
