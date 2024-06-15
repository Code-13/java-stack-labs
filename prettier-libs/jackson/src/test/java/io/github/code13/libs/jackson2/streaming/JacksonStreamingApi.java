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

package io.github.code13.libs.jackson2.streaming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * JacksonStreamingApi.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/15 15:44
 */
public class JacksonStreamingApi {

  /** We can write JSON content directly to the OutputStream by using a JsonGenerator class. */
  @Nested
  class WritingToJSONTest {

    @Test
    @DisplayName("givenJsonGenerator_whenAppendJsonToIt_thenGenerateJson")
    void givenJsonGenerator_whenAppendJsonToIt_thenGenerateJson() throws IOException {

      /* { "name":"Tom", "age":25, "address":[ "Poland", "5th avenue" ] } */

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      JsonFactory jsonFactory = new JsonFactory();

      try (JsonGenerator generator =
          jsonFactory.createGenerator(outputStream, JsonEncoding.UTF8); ) {
        generator.writeStartObject();
        generator.writeStringField("name", "Tom");
        generator.writeNumberField("age", 25);
        generator.writeFieldName("address");
        generator.writeStartArray();
        generator.writeString("Poland");
        generator.writeString("5th avenue");
        generator.writeEndArray();
        generator.writeEndObject();
      }

      String json = outputStream.toString(StandardCharsets.UTF_8);

      assertEquals("{\"name\":\"Tom\",\"age\":25,\"address\":[\"Poland\",\"5th avenue\"]}", json);
    }
  }

  /**
   * When we get a JSON String as an input, and we want to extract specific fields from it, a
   * JsonParser class can be used:
   */
  @Nested
  class ParsingJSONTest {

    @Test
    @DisplayName("givenJson_whenReadItUsingStreamAPI_thenShouldCreateProperJsonObject")
    void givenJson_whenReadItUsingStreamAPI_thenShouldCreateProperJsonObject() throws IOException {
      String json =
          """
          {"name":"Tom","age":25,"address":["Poland","5th avenue"]}""";
      JsonFactory jsonFactory = new JsonFactory();

      String parsedName = null;
      Integer parsedAge = null;

      List<String> address = new LinkedList<>();

      try (JsonParser parser = jsonFactory.createParser(json)) {
        while (parser.nextToken() != JsonToken.END_OBJECT) {
          String fieldName = parser.getCurrentName();

          if ("name".equals(fieldName)) {
            parser.nextToken();
            parsedName = parser.getText();
          }

          if ("age".equals(fieldName)) {
            parser.nextToken();
            parsedAge = parser.getIntValue();
          }

          if ("address".equals(fieldName)) {
            parser.nextToken();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
              address.add(parser.getText());
            }
          }
        }
      }

      assertEquals("Tom", parsedName);
      assertEquals(25, parsedAge);

      assertEquals(address, List.of("Poland", "5th avenue"));
    }
  }
}
