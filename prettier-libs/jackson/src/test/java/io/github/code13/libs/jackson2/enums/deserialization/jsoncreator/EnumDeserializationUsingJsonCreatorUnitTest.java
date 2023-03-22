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

package io.github.code13.libs.jackson2.enums.deserialization.jsoncreator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * EnumDeserializationUsingJsonCreatorUnitTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/22 10:05
 */
class EnumDeserializationUsingJsonCreatorUnitTest {

  @Test
  @DisplayName("givenEnumWithJsonCreator_whenDeserializingJson_thenCorrectRepresentation")
  void givenEnumWithJsonCreator_whenDeserializingJson_thenCorrectRepresentation()
      throws JsonProcessingException {
    String json = "{\"distance\": {\"unit\":\"miles\",\"meters\":1609.34}}";

    City city = new ObjectMapper().readValue(json, City.class);
    assertEquals(Distance.MILE, city.getDistance());
  }
}
