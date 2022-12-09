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

package io.github.code13.libs.jackson2.objectmapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ReadingAndWritingUsingObjectMapper.
 *
 * <p>Reading and Writing Using ObjectMapper
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/6 10:31
 */
public class ReadingAndWritingUsingObjectMapper {

  TemporaryFolder temporaryFolder;

  @Test
  @DisplayName("testJavaObjectToJson")
  void testJavaObjectToJson() throws IOException {
    Car car = new Car("yellow", "renault");
    ObjectMapper objectMapper = new ObjectMapper();

    String result = objectMapper.writeValueAsString(car);

    // {"color":"yellow","type":"renault"}

    assertThat(result, containsString("color"));
    assertThat(result, containsString("renault"));
  }

  @Test
  @DisplayName("testJsonToJavaObject")
  void testJsonToJavaObject() throws JsonProcessingException {
    String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

    Car car = new ObjectMapper().readValue(json, Car.class);

    assertNotNull(car);
    assertEquals("BMW", car.getType());
  }

  @Test
  @DisplayName("whenReadJsonToJsonNode_thanCorrect")
  void whenReadJsonToJsonNode_thanCorrect() throws JsonProcessingException {
    String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";

    JsonNode jsonNode = new ObjectMapper().readTree(json);

    assertNotNull(jsonNode);
    assertThat(jsonNode.get("color").asText(), containsString("Black"));
  }

  @Test
  @DisplayName("whenReadJsonToList_thanCorrect")
  void whenReadJsonToList_thanCorrect() throws JsonProcessingException {
    final String json =
        "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"BMW\" }]";

    List<Car> cars = new ObjectMapper().readValue(json, new TypeReference<List<Car>>() {});

    for (Car car : cars) {
      assertNotNull(car);
      assertEquals(car.getType(), "BMW");
    }
  }

  @Test
  @DisplayName("whenReadJsonToMap_thanCorrect")
  void whenReadJsonToMap_thanCorrect() throws JsonProcessingException {
    String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

    Map<String, String> map =
        new ObjectMapper().readValue(json, new TypeReference<Map<String, String>>() {});

    assertNotNull(map);

    for (String key : map.keySet()) {
      assertNotNull(key);
    }
  }
}
