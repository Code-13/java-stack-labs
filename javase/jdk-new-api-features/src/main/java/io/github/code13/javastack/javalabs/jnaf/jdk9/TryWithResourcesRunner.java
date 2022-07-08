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

package io.github.code13.javastack.javalabs.jnaf.jdk9;

import java.io.BufferedInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TryWithResourcesRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 16:34
 */
@DisplayName("try with resources")
class TryWithResourcesRunner {

  @Test
  @DisplayName("try-with-resources 优化")
  void test1() {

    // write in java7

    try (BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in);
        BufferedInputStream bufferedInputStream1 = new BufferedInputStream(System.in)) {
      // do something
    } catch (IOException e) {
      e.printStackTrace();
    }

    // now in java9

    BufferedInputStream bufferedInputStream = new BufferedInputStream(System.in);
    BufferedInputStream bufferedInputStream1 = new BufferedInputStream(System.in);

    try (bufferedInputStream;
        bufferedInputStream1) {
      // do something
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}