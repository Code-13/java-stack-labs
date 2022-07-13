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

package io.github.code13.javastack.ck.bronze;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-青铜7：顺藤摸瓜-如何从synchronized中的锁认识Monitor .
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/12 10:00
 */
@DisplayName("并发王者课-青铜7：顺藤摸瓜-如何从synchronized中的锁认识Monitor")
class Bronze7 {

  @Test
  @DisplayName("理解Monitor")
  void test1() {

    /*
     * Monitor作为一种同步机制，它并非Java所特有，但Java实现了这一机制。
     */

    /*
     * Monitor在计算机科学中的作用：
     *
     * 互斥（mutual exclusion ）：每次只允许一个线程进入临界区；
     * 协作（cooperation）：当临界区的线程执行结束后满足特定条件时，可以通知其他的等待线程进入。
     */

    /*
     *https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/dc10890b3bdb4ceb97c7a5e66efe24bd~tplv-k3u1fbpfcp-zoom-1.image
     */

  }
}
