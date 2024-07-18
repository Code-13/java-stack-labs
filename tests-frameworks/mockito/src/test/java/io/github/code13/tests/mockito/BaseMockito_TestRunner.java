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

package io.github.code13.tests.mockito;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

/**
 * BaseMockito_TestRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/17 23:19
 */
public class BaseMockito_TestRunner {

  // 等于 @ExtendWith(MockitoExtension.class)

  protected AutoCloseable closeable;

  @BeforeEach
  public void openMocks() {
    // MockitoAnnotations.initMocks 的一个替代方案是使用 MockitoJUnitRunner 。
    // MockitoAnnotations.openMocks 的一个替代方案是使用 MockitoExtension 。

    closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void releaseMocks() throws Exception {
    closeable.close();
  }
}
