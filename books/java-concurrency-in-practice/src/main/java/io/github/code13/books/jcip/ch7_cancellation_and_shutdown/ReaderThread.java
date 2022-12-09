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

package io.github.code13.books.jcip.ch7_cancellation_and_shutdown;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * ReaderThread.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/13 21:36
 */
public class ReaderThread extends Thread {

  private static final int BUFSZ = 1024;

  private final Socket socket;
  private final InputStream is;

  public ReaderThread(Socket socket) throws IOException {
    this.socket = socket;
    is = socket.getInputStream();
  }

  @Override
  public void run() {
    try {
      byte[] bytes = new byte[BUFSZ];
      while (true) {
        int count = is.read(bytes);
        if (count < 0) {
          break;
        } else if (count > 0) {
          processBuffer(bytes, count);
        }
      }
    } catch (IOException ignored) {
      /*允许线程退出*/
    }
  }

  private void processBuffer(byte[] bytes, int count) {}

  @Override
  public void interrupt() {

    try {
      socket.close();
    } catch (IOException ignored) {
    } finally {
      super.interrupt();
    }
  }
}
