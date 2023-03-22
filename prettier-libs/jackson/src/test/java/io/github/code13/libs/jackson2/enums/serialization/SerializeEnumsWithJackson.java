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

package io.github.code13.libs.jackson2.enums.serialization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SerializeAndDeserializeEnumsWithJackson.
 *
 * <p>How To Serialize and Deserialize Enums with Jackson
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/20 09:46
 */
public class SerializeEnumsWithJackson {

  @Test
  @DisplayName("givenEnum_whenSerializingJson_thenCorrectRepresentation")
  void givenEnum_whenSerializingJson_thenCorrectRepresentation() throws JsonProcessingException {
    String result = new ObjectMapper().writeValueAsString(Distance.MILE);

    // {"name":"MILE","unit":"miles","meters":1609.34}

    assertThat(result, containsString("1609.34"));
  }

  @Test
  @DisplayName("whenSerializingASimpleEnum_thenCorrect")
  void whenSerializingASimpleEnum_thenCorrect() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(DistanceEnumSimple.MILE);

    // "MILE"

    assertEquals("\"MILE\"", result);
  }

  @Test
  @DisplayName("whenSerializingAEnumWithValue_thenCorrect")
  void whenSerializingAEnumWithValue_thenCorrect() throws JsonProcessingException {
    String result = new ObjectMapper().writeValueAsString(DistanceEnumWithValue.MILE);

    // 1609.34

    assertThat(result, is("1609.34"));
  }

  @Test
  @DisplayName("whenSerializingAnEnum_thenCorrect")
  void whenSerializingAnEnum_thenCorrect() throws JsonProcessingException {
    String result = new ObjectMapper().writeValueAsString(DistanceEnumWithJsonFormat.MILE);

    // {"unit":"miles","meters":1609.34}

    assertThat(result, containsString("\"meters\":1609.34"));
  }

  @Test
  @DisplayName("whenSerializingEntityWithEnum_thenCorrect")
  void whenSerializingEntityWithEnum_thenCorrect() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String result =
        objectMapper.writeValueAsString(
            new MyDtoWithEnumJsonFormat("a", 1, true, DistanceEnumWithJsonFormat.MILE));

    // {"stringValue":"a","intValue":1,"booleanValue":true,"distanceType":{"unit":"miles","meters":1609.34}}

    assertThat(result, containsString("\"meters\":1609.34"));
  }

  @Test
  @DisplayName("whenSerializingArrayOfEnums_thenCorrect")
  void whenSerializingArrayOfEnums_thenCorrect() throws JsonProcessingException {
    String result =
        new ObjectMapper()
            .writeValueAsString(
                new DistanceEnumWithJsonFormat[] {
                  DistanceEnumWithJsonFormat.MILE, DistanceEnumWithJsonFormat.KILOMETER
                });

    //  [{"unit":"miles","meters":1609.34},{"unit":"km","meters":1000.0}]

    assertThat(result, containsString("\"meters\":1609.34"));
  }

  @Test
  @DisplayName("givenCustomSerializer_whenSerializingEntityWithEnum_thenCorrect")
  void givenCustomSerializer_whenSerializingEntityWithEnum_thenCorrect()
      throws JsonProcessingException {
    String result =
        new ObjectMapper().writeValueAsString(new MyDtoWithEnumCustom("a", 1, true, Distance.MILE));

    // {"stringValue":"a","intValue":1,"booleanValue":true,"type":{"name":"MILE","unit":"miles","meters":1609.34}}

    assertThat(result, containsString("\"meters\":1609.34"));
  }
}
