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

package io.github.code13.spring.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * UserRepositoryTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/12 12:55
 */
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
class UserRepositoryTest {

  @Autowired TestEntityManager testEntityManager;

  @Autowired UserRepository userRepository;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  @DisplayName("testSave")
  void testSave() {
    User entity = new User();
    entity.setUsername("1");
    entity.setEncodePassword("111");

    User save = userRepository.save(entity);

    System.out.println(save);
  }
}
