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

package io.github.code13.tests.junit5.parallel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;
import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ResourceLock;

/**
 * ParallelTestsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 5:30 PM
 */
@Execution(CONCURRENT)
class ParallelTestsRunner {

  private Properties backup;

  @BeforeEach
  void backup() {
    backup = new Properties();
    backup.putAll(System.getProperties());
  }

  @AfterEach
  void restore() {
    System.setProperties(backup);
  }

  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ)
  void customPropertyIsNotSetByDefault() {
    assertNull(System.getProperty("my.prop"));
  }

  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
  void canSetCustomPropertyToApple() {
    System.setProperty("my.prop", "apple");
    assertEquals("apple", System.getProperty("my.prop"));
  }

  @Test
  @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
  void canSetCustomPropertyToBanana() {
    System.setProperty("my.prop", "banana");
    assertEquals("banana", System.getProperty("my.prop"));
  }
}
