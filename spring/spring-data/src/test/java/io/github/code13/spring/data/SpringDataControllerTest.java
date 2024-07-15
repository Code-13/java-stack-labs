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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * SpringDataControllerTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/15 13:10
 */
@ExtendWith(MockitoExtension.class)
public class SpringDataControllerTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private SpringDataController controller;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void testFindAll() throws Exception {
    // Mocking the behavior of userRepository
    List<User> all = Collections.singletonList(new User());
    when(userRepository.findAll()).thenReturn(all);

    // Performing a GET request and asserting the response
    mockMvc.perform(MockMvcRequestBuilders.get("/spring-data/users")).andExpect(status().isOk());
    // serialization of the mocked User object.

    // Verify that the method was called exactly once
    verify(userRepository, times(1)).findAll();
  }
}
