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

package io.github.code13.libs.jackson2.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ImmutableObjectDeserialization.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/22 14:50
 */
class ImmutableObjectDeserializationTest {

  @Test
  @DisplayName("whenPublicConstructorIsUsed_thenObjectIsDeserialized")
  void whenPublicConstructorIsUsed_thenObjectIsDeserialized() throws JsonProcessingException {
    String json = "{\"name\":\"Frank\",\"id\":5000}";

    Employee employee = new ObjectMapper().readValue(json, Employee.class);

    assertEquals("Frank", employee.getName());
    assertEquals(5000L, employee.getId());
  }

  @Test
  @DisplayName("whenBuilderIsUsedAndFieldIsNull_thenObjectIsDeserialized")
  void whenBuilderIsUsedAndFieldIsNull_thenObjectIsDeserialized() throws JsonProcessingException {
    String json = "{\"name\":\"Frank\"}";

    Person person = new ObjectMapper().readValue(json, Person.class);

    assertEquals("Frank", person.getName());
    assertNull(person.getAge());
  }

  @Test
  @DisplayName("whenBuilderIsUsedAndAllFieldsPresent_thenObjectIsDeserialized")
  void whenBuilderIsUsedAndAllFieldsPresent_thenObjectIsDeserialized()
      throws JsonProcessingException {
    String json = "{\"name\":\"Frank\",\"age\":50}";

    Person person = new ObjectMapper().readValue(json, Person.class);

    assertEquals("Frank", person.getName());
    assertNotNull(person.getAge());
    assertEquals(50, person.getAge());
  }
}
