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

package io.github.code13.books.jcip.ch5_building_blocks;

import java.io.Serial;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * PreLoader.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/8/2021 3:37 PM
 */
public class PreLoader {

  private final FutureTask<ProductInfo> future = new FutureTask<>(PreLoader.this::loadProductInfo);

  private ProductInfo loadProductInfo() {
    return null;
  }

  private final Thread thread = new Thread(future);

  public void start() {
    thread.start();
  }

  public ProductInfo get() throws DataLoadException, InterruptedException {
    try {
      return future.get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof DataLoadException) {
        throw (DataLoadException) cause;
      } else {
        throw LaunderThrowable.launderThrowable(cause);
      }
    }
  }

  static class DataLoadException extends Exception {

    @Serial private static final long serialVersionUID = 3992718468942845928L;
  }

  interface ProductInfo {}
}
