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

package io.github.code13.javastack.javalabs.jnaf.jdk9;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CollectionsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 14:50
 */
@DisplayName("新的创建新的集合的方法")
class CollectionsRunner {

  @Test
  @DisplayName("静态工厂方法,创建的集合均为不可变集合")
  void staticMethodForCreateCollection() {

    List<Integer> integers = List.of(1, 2, 3, 4);

    Set<String> strings = Set.of("1", "2", "3");

    Map<String, String> stringMap = Map.of("k1", "v1", "k2", "v2");
  }
}
