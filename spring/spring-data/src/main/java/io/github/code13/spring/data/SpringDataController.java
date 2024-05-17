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

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringDataController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/3/21 13:18
 */
@RestController
@RequestMapping("/spring-data")
public class SpringDataController {

  private final UserRepository userRepository;

  public SpringDataController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/users")
  public List<User> findAll() {
    List<User> all = userRepository.findAll();
    return all;
  }

  @GetMapping("/user/{id}")
  public User findById(@PathVariable String id) {
    return userRepository.getUserById(id);
  }

  @GetMapping("/user/page")
  public Page<User> findPage(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @GetMapping("/user/querydsl/{id}")
  public User findByIdQuerydsl(@PathVariable String id) {
    return userRepository.findOne(QUser.user.userId.eq(id)).orElse(null);
  }
}
