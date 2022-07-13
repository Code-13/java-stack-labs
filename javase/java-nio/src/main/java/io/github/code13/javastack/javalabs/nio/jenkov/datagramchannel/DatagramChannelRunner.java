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

package io.github.code13.javastack.javalabs.nio.jenkov.datagramchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.junit.jupiter.api.DisplayName;

/**
 * DatagramChannelRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/30 14:59
 */
@DisplayName("DatagramChannel")
class DatagramChannelRunner {

  public static void main(String[] args) throws IOException {
    DatagramChannel datagramChannel = DatagramChannel.open();
    datagramChannel.bind(new InetSocketAddress(9999));

    ByteBuffer buffer = ByteBuffer.allocate(48);
    buffer.clear();
    datagramChannel.receive(buffer);

    String newData = "New String to write to file..." + System.currentTimeMillis();

    ByteBuffer buf = ByteBuffer.allocate(48);
    buf.clear();
    buf.put(newData.getBytes());
    buf.flip();

    int bytesSent = datagramChannel.send(buf, new InetSocketAddress("jenkov.com", 80));

    datagramChannel.connect(new InetSocketAddress("jenkov.com", 80));
    int bytesRead = datagramChannel.read(buf);
    int bytesWritten = datagramChannel.write(buf);
  }
}
