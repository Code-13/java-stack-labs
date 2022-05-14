/*
 *
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

package io.github.code13.javastack.javalabs.agent.demo05_threadlocal_track;

/**
 * TrackAgentTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 10:08
 */
public class TrackAgentTest {

  public static void main(String[] args) {
    // 线程一
    new Thread(() -> new TrackAgentTest().http_lt1()).start();

    // 线程二
    new Thread(
            () -> {
              new TrackAgentTest().http_lt1();
            })
        .start();
  }

  public void http_lt1() {
    System.out.println("测试结果：hi1");
    http_lt2();
  }

  public void http_lt2() {
    System.out.println("测试结果：hi2");
    http_lt3();
  }

  public void http_lt3() {
    System.out.println("测试结果：hi3");
  }
}
