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

package io.github.code13.javase.jnaf.jdk14;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RecordRunner.
 *
 * <p>Record .
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 23:31
 */
public class RecordRunner {

  @Test
  @DisplayName("简单测试")
  void test1() {
    User user = new User("1", "2");
    System.out.println(user);

    System.out.println(user.name());
    System.out.println(user.email());
  }
}
