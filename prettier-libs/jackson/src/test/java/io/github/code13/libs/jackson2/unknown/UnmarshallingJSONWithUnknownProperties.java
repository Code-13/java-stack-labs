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

package io.github.code13.libs.jackson2.unknown;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unmarshalling JSON with Unknown Properties.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/7 21:01
 */
public class UnmarshallingJSONWithUnknownProperties {

  @NoArgsConstructor
  @Getter
  @Setter
  static class MyDto {

    private String stringValue;
    private int intValue;
    private boolean booleanValue;
  }

  @Test
  @DisplayName("givenNotAllFieldsHaveValuesInJson_whenDeserializingAJsonToAClass_thenCorrect")
  void givenNotAllFieldsHaveValuesInJson_whenDeserializingAJsonToAClass_thenCorrect()
      throws JsonProcessingException {
    String jsonAsString =
        """
        {"stringValue":"a","booleanValue":true}""";
    ObjectMapper mapper = new ObjectMapper();

    MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
  }

  @Test
  @DisplayName("givenJsonHasUnknownValues_whenDeserializingAJsonToAClass_thenExceptionIsThrown")
  void givenJsonHasUnknownValues_whenDeserializingAJsonToAClass_thenExceptionIsThrown() {
    assertThrows(
        UnrecognizedPropertyException.class,
        () -> {
          String jsonAsString =
              """
                  {"stringValue":"a","intValue":1,"booleanValue":true,"stringValue2":"something"}""";
          ObjectMapper mapper = new ObjectMapper();

          MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

          assertNotNull(readValue);
          assertThat(readValue.getStringValue(), equalTo("a"));
          assertThat(readValue.isBooleanValue(), equalTo(true));
          assertThat(readValue.getIntValue(), equalTo(1));
        });
  }

  @Test
  @DisplayName(
      "givenJsonHasUnknownValuesButJacksonIsIgnoringUnknownFields_whenDeserializing_thenCorrect")
  void givenJsonHasUnknownValuesButJacksonIsIgnoringUnknownFields_whenDeserializing_thenCorrect()
      throws JsonProcessingException {
    String jsonAsString =
        """
            {"stringValue":"a",
            "intValue":1,
            "booleanValue":true,
            "stringValue2":"something"}""";

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    MyDto readValue = objectMapper.readValue(jsonAsString, MyDto.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
    assertThat(readValue.getIntValue(), equalTo(1));
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @NoArgsConstructor
  @Getter
  @Setter
  static class MyDtoIgnoreUnknown {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;
  }

  @Test
  @DisplayName("givenJsonHasUnknownValuesButIgnoredOnClass_whenDeserializing_thenCorrect")
  void givenJsonHasUnknownValuesButIgnoredOnClass_whenDeserializing_thenCorrect()
      throws JsonProcessingException {
    String jsonAsString =
        """
            {"stringValue":"a",
            "intValue":1,
            "booleanValue":true,
            "stringValue2":"something"}""";

    ObjectMapper mapper = new ObjectMapper();

    MyDtoIgnoreUnknown readValue = mapper.readValue(jsonAsString, MyDtoIgnoreUnknown.class);

    assertNotNull(readValue);
    assertThat(readValue.getStringValue(), equalTo("a"));
    assertThat(readValue.isBooleanValue(), equalTo(true));
    assertThat(readValue.getIntValue(), equalTo(1));
  }
}
