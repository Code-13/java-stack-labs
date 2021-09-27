/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.libs.redisson.objects;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;

/**
 * Redisson的分布式RBinaryStream Java对象同时提供了InputStream接口和OutputStream接口的实现. 流的最大容量受Redis主节点的内存大小限制..
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/27 12:24
 */
@DisplayName("二进制流（Binary Stream）")
class BinaryStreamRunner {

  static RedissonClient redissonClient;
  static RBinaryStream stream;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
    stream = redissonClient.getBinaryStream("anyStream");
  }

  @Test
  @DisplayName("Binary Stream")
  void stream() throws IOException {
    byte[] bytes = {1, 1, 1, 1};
    stream.set(bytes);

    var in = stream.getInputStream();
    byte[] readBuffer = new byte[512];
    in.read(readBuffer);

    var out = stream.getOutputStream();
    byte[] contentToWrite = {2, 2, 2, 2};
    out.write(contentToWrite);
  }
}
