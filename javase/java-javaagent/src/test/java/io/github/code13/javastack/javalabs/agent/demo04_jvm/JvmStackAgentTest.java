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

package io.github.code13.javastack.javalabs.agent.demo04_jvm;

import java.util.LinkedList;
import java.util.List;

/**
 * JvmStackAgentTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 09:36
 */
class JvmStackAgentTest {

  public static void main(String[] args) {
    while (true) {
      List<Object> list = new LinkedList<>();
      list.add("嗨！JavaAgent");
      list.add("嗨！JavaAgent");
      list.add("嗨！JavaAgent");
    }
  }
}
