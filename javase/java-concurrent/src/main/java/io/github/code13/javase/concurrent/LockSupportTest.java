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

package io.github.code13.javase.concurrent;

import java.util.concurrent.locks.LockSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * LockSupportTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/8/24 10:56
 */
@DisplayName("LockSupport")
class LockSupportTest {

  int time = 5000;

  /** 这个测试方法现在不对. */
  @Test
  @DisplayName("LockSupport")
  void test1() {
    System.out.println("LockSupport 测试开始");

    LockSupport.park(Thread.currentThread());

    long currentTimeMillis = System.currentTimeMillis();
    while (true) {
      System.out.println("加锁");
      long millis = System.currentTimeMillis();

      if (millis - currentTimeMillis > time) {
        break;
      }
    }

    LockSupport.unpark(Thread.currentThread());

    System.out.println("LockSupport 测试结束");
  }
}
