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

package io.github.code13.javastack.javalabs.nio.jenkov.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;

/**
 * SocketChannelNonBlockingRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 14:24
 */
@DisplayName("SocketChannel non-blocking mode")
class SocketChannelNonBlockingRunner {

  public static void main(String[] args) throws IOException {
    SocketChannel sc = SocketChannel.open();
    sc.configureBlocking(false);

    sc.connect(new InetSocketAddress(8080));

    while (!sc.finishConnect()) {
      String newData = "New String to write to file..." + System.currentTimeMillis();

      ByteBuffer byteBuffer = ByteBuffer.allocate(48);
      byteBuffer.clear();
      byteBuffer.put(newData.getBytes(StandardCharsets.UTF_8));
      byteBuffer.flip();

      while (byteBuffer.hasRemaining()) {
        sc.write(byteBuffer);
      }

      byteBuffer.clear();
    }

    //    sc.close();
  }
}
