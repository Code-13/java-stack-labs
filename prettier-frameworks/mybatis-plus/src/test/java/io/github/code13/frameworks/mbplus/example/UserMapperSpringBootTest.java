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

package io.github.code13.frameworks.mbplus.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * UserMapperSpringBootTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 12:32 PM
 */
@SpringBootTest
class UserMapperSpringBootTest {

  @Autowired UserMapper userMapper;

  @Test
  void testSelect() {
    System.out.println(("----- selectAll method test ------"));
    List<User> userList = userMapper.selectList(null);
    assertEquals(5, userList.size());
    userList.forEach(System.out::println);
  }

  @Test
  void testGetByEmail() {
    Optional<User> userOptional = userMapper.getByEmail("11");
    assertFalse(userOptional.isPresent());
  }
}
