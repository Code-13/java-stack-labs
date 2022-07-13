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

package io.github.code13.javastack.javalabs.nio.jenkov.channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ChannelRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/29 14:54
 */
@DisplayName("java.nio.channel")
class ChannelRunner {

  @Test
  @DisplayName("")
  void test() throws IOException {

    URL resource = ChannelRunner.class.getClassLoader().getResource("data\\nio-data.txt");
    assert resource != null;
    File f = new File(resource.getFile());

    var file = new RandomAccessFile(f, "rw");
    FileChannel channel = file.getChannel();

    ByteBuffer buffer = ByteBuffer.allocate(48);

    int bytesRead = channel.read(buffer);

    while (bytesRead != -1) {
      System.out.println("bytesRead = " + bytesRead);

      buffer.flip();

      while (buffer.hasRemaining()) {
        System.out.println(buffer.get());
      }

      buffer.clear();

      bytesRead = channel.read(buffer);
    }

    file.close();
  }
}
