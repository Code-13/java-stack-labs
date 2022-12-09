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
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SerializationDeserializationFeatureUnitTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/6 14:56
 */
public class SerializationDeserializationFeature {

  final String EXAMPLE_JSON = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
  final String JSON_CAR = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";
  final String JSON_ARRAY =
      "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"BMW\" }]";

  @Test
  @DisplayName("whenFailOnUnkownPropertiesFalse_thanJsonReadCorrectly")
  void whenFailOnUnkownPropertiesFalse_thanJsonReadCorrectly() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Car car = objectMapper.readValue(JSON_CAR, Car.class);

    JsonNode jsonNode = objectMapper.readTree(JSON_CAR);
    JsonNode jsonNodeYear = jsonNode.get("year");
    String year = jsonNodeYear.asText();

    assertNotNull(car);
    assertEquals("Black", car.getColor());
    assertThat(year, containsString("1970"));
  }

  @Test
  @DisplayName("whenCustomSerializerDeserializer_thanReadWriteCorrect")
  void whenCustomSerializerDeserializer_thanReadWriteCorrect() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule serializerModule =
        new SimpleModule("CustomSerializer", new Version(1, 0, 0, null, null, null));
    serializerModule.addSerializer(Car.class, new CustomCarSerializer());

    mapper.registerModule(serializerModule);

    Car car = new Car("yellow", "renault");
    String carJson = mapper.writeValueAsString(car);

    assertThat(carJson, containsString("renault"));
    assertThat(carJson, containsString("model"));

    SimpleModule deserializerModule =
        new SimpleModule("CustomCarDeserializer", new Version(1, 0, 0, null, null, null));
    deserializerModule.addDeserializer(Car.class, new CustomDeserializer());
    mapper.registerModule(deserializerModule);

    Car carResult = mapper.readValue(EXAMPLE_JSON, Car.class);
    assertNotNull(carResult);
    assertEquals("Black", carResult.getColor());
  }

  @Test
  @DisplayName("whenUseJavaArrayForJsonArrayTrue_thanJsonReadAsArray")
  void whenUseJavaArrayForJsonArrayTrue_thanJsonReadAsArray() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

    Car[] cars = objectMapper.readValue(JSON_ARRAY, Car[].class);

    for (Car car : cars) {
      assertNotNull(car);
      assertEquals("BMW", car.getType());
    }
  }

  static class CustomCarSerializer extends StdSerializer<Car> {

    public CustomCarSerializer() {
      super(Car.class);
    }

    @Override
    public void serialize(Car value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      gen.writeStartObject();
      gen.writeStringField("model:", value.getType());
      gen.writeEndObject();
    }
  }

  static class CustomDeserializer extends StdDeserializer<Car> {

    public CustomDeserializer() {
      super(Car.class);
    }

    @Override
    public Car deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JacksonException {
      Car car = new Car();

      ObjectCodec codec = p.getCodec();
      JsonNode jsonNode = codec.readTree(p);

      String color = jsonNode.get("color").asText();
      car.setColor(color);

      return car;
    }
  }
}
