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

package io.github.code13.javase.nio.jenkov.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;

/**
 * SelectorRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 09:23
 */
@DisplayName("Selector")
class SelectorRunner {

  private static ThreadPoolExecutor threadPoolExecutor =
      new ThreadPoolExecutor(
          1,
          10,
          1000,
          TimeUnit.MILLISECONDS,
          new LinkedTransferQueue<>(),
          new ThreadPoolExecutor.AbortPolicy());

  public static void main(String[] args) throws IOException {
    try (ServerSocketChannel channel = ServerSocketChannel.open()) {
      channel.configureBlocking(false);

      channel.bind(new InetSocketAddress(8080));

      Selector selector = Selector.open();

      SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_ACCEPT);

      while (true) {
        int readyChannels = selector.select();
        if (readyChannels == 0) {
          continue;
        }

        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next();
          if (key.isAcceptable()) {
            // a connection was accepted by a ServerSocketChannel.
            System.out.println("a connection was accepted by a ServerSocketChannel.");
            accept(key);
          } else if (key.isConnectable()) {
            // a connection was established with a remote server.
            System.out.println("a connection was established with a remote server.");
          } else if (key.isReadable()) {
            // a channel is ready for reading
            System.out.println("a channel is ready for reading");
            threadPoolExecutor.submit(new NioServerHandler(key));
          } else if (key.isWritable()) {
            // a channel is ready for writing
            System.out.println("a channel is ready for writing");
          }
          keyIterator.remove();
        }
      }
    }
  }

  public static void accept(SelectionKey key) throws IOException {
    Selector selector = key.selector();
    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
    SocketChannel sc = channel.accept();
    sc.configureBlocking(false);

    sc.register(selector, SelectionKey.OP_READ);
    System.out.println("accept a client : " + sc.socket().getInetAddress().getHostName());
  }

  public record NioServerHandler(SelectionKey selectionKey) implements Runnable {

    @Override
    public void run() {
      try {
        if (selectionKey.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

          // 从通道读取数据到缓冲区
          ByteBuffer buffer = ByteBuffer.allocate(1024);

          // 输出客户端发送过来的消息
          socketChannel.read(buffer);
          buffer.flip();
          System.out.println(
              "收到客户端"
                  + socketChannel.socket().getInetAddress().getHostName()
                  + "的数据："
                  + new String(buffer.array()));

          // 将数据添加到key中
          ByteBuffer outBuffer = ByteBuffer.wrap(buffer.array());

          // 将消息回送给客户端
          socketChannel.write(outBuffer);
          selectionKey.cancel();
          socketChannel.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
