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

package io.github.code13.javase.nio.jenkov.serversocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.DisplayName;

/**
 * ServerSocketChannelRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 14:37
 */
@DisplayName("ServerSocketChannel")
class ServerSocketChannelRunner {

  public static void main(String[] args) throws IOException {

    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(8080));

    while (true) {
      SocketChannel socketChannel = serverSocketChannel.accept();

      if (socketChannel != null) {}
    }
  }

  public static void main1(String[] args) throws IOException {
    ServerSocketChannel noBlockingServerSocketChannel = ServerSocketChannel.open();
    noBlockingServerSocketChannel.socket().bind(new InetSocketAddress(8080));
    noBlockingServerSocketChannel.configureBlocking(false);

    while (true) {
      SocketChannel socketChannel = noBlockingServerSocketChannel.accept();

      if (socketChannel != null) {
        // do something with socketChannel...
      }
    }
  }
}
