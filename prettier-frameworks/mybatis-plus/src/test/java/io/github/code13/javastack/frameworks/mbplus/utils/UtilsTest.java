/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.frameworks.mbplus.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * UtilsTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 1:19 PM
 */
class UtilsTest {

  @Test
  void getYamlProperty() {
    String property = Utils.getYamlProperty("spring.datasource.url");
    assertNotNull(property);
  }

  @Test
  void loadClassPathResource() {
    String value = Utils.loadClassPathResource("schema.sql");
    System.out.println(value);
    assertNotNull(value);
  }
}