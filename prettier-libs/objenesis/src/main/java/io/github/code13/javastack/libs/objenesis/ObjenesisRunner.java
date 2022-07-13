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

package io.github.code13.javastack.libs.objenesis;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 * ObjenesisRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 15:20
 */
@DisplayName("Objenesis")
class ObjenesisRunner {

  Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer

  @Test
  @DisplayName("objenesis.newInstance")
  void test1() {
    User user = objenesis.newInstance(User.class);
    System.out.println(user);
  }

  @Test
  @DisplayName("objenesis.getInstantiatorOf")
  void test2() {
    ObjectInstantiator<User> instantiator = objenesis.getInstantiatorOf(User.class);
    User user = instantiator.newInstance();
    System.out.println(user);
  }

  @Test
  @DisplayName("testNotNeedCons")
  void testNotNeedCons() {
    HelloObjenesis instance = objenesis.newInstance(HelloObjenesis.class);
    System.out.println(instance);
    System.out.println(instance.hello);
    // io.github.code13.javastack.libs.objenesis.ObjenesisRunner$HelloObjenesis@33cb5951
    // null
  }

  @Data
  static class User {
    private String userName;
    private Integer age;
  }

  static class HelloObjenesis {
    private String hello = "Objenesis!";

    public HelloObjenesis(String hello) {
      this.hello = hello;
    }

    public void hello() {
      System.out.println("你好！ " + hello);
    }
  }
}
