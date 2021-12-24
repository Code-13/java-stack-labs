/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch16_jmm;

import org.junit.jupiter.api.Test;

/**
 * PossibleReordering.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/24/2021 11:19 AM
 */
public class PossibleReordering {

  int a;
  int b = 0;
  int x;
  int y = 0;

  @Test
  void test() throws InterruptedException {
    Thread one =
        new Thread(
            () -> {
              a = 1;
              x = b;
            });

    Thread two =
        new Thread(
            () -> {
              b = 1;
              y = a;
            });

    one.start();
    two.start();

    one.join();
    two.join();

    System.out.println("(" + x + "，" + y + ")");
  }
}
