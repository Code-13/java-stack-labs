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

package io.github.code13.javastack.javalabs.jnaf.jdk14;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TextBlockRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 23:51
 */
public class TextBlockRunner {

  @Test
  @DisplayName("jdk 14 新特性")
  void test1() {

    String sql = """
        SELECT id,name,email
        FROM user
        WHERE id > 30
        ORDER BY email desc
        """;

    System.out.println(sql);

    // \ 取消换行

    String sql1 = """
        SELECT id,name,email \
        FROM user \
        WHERE id > 30 \
        ORDER BY email desc \
        """;

    System.out.println(sql1);

  }
}
