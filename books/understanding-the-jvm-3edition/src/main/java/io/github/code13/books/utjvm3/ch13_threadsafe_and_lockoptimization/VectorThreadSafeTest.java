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

package io.github.code13.books.utjvm3.ch13_threadsafe_and_lockoptimization;

import java.util.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 对 Vector 线程安全的测试.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/16 21:35
 */
public class VectorThreadSafeTest {

  private static final Vector<Integer> vector = new Vector<>();

  @Test
  @DisplayName("notThreadSafe")
  void notThreadSafe() {

    while (Thread.activeCount() > 20) {

      for (int i = 0; i < 10; i++) {
        vector.add(i);
      }

      Thread removeThread =
          new Thread(
              () -> {
                for (int i = 0; i < vector.size(); i++) {
                  vector.remove(i);
                }
              });

      Thread printThread =
          new Thread(
              () -> {
                for (int i = 0; i < vector.size(); i++) {
                  System.out.println(vector.get(i));
                }
              });

      removeThread.start();
      printThread.start();
    }
  }

  @Test
  @DisplayName("threadSafe")
  void threadSafe() {

    while (Thread.activeCount() > 20) {

      for (int i = 0; i < 10; i++) {
        vector.add(i);
      }

      Thread removeThread =
          new Thread(
              () -> {
                synchronized (vector) {
                  for (int i = 0; i < vector.size(); i++) {
                    vector.remove(i);
                  }
                }
              });

      Thread printThread =
          new Thread(
              () -> {
                synchronized (vector) {
                  for (int i = 0; i < vector.size(); i++) {
                    System.out.println(vector.get(i));
                  }
                }
              });

      removeThread.start();
      printThread.start();
    }
  }
}
