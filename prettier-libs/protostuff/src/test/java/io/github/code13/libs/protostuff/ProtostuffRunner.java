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

package io.github.code13.libs.protostuff;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ProtostuffRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 14:13
 */
@DisplayName("Protostuff 测试")
class ProtostuffRunner {

  @Test
  @DisplayName("Protostuff")
  void test() {
    User user = User.builder().id("1").name("夏天").age(12).desc("蒹葭").build();
    byte[] data = ProtostuffUtils.serialize(user);
    System.out.println("序列化后：" + Arrays.toString(data));

    User deserialized = ProtostuffUtils.deserialize(data, User.class);
    System.out.println("反序列化之后：" + deserialized.toString());

    Assertions.assertEquals(user, deserialized);
  }
}
