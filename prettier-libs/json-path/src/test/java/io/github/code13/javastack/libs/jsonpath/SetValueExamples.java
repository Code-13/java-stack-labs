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

package io.github.code13.javastack.libs.jsonpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configuration.Defaults;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SetValueExamples.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 14:04
 */
public class SetValueExamples {

  String json;

  Root root;

  ObjectMapper objectMapper;

  @BeforeAll
  static void setupJsonPath() {
    // 为 JsonPath 默认配置 jackson 序列化
    Configuration.setDefaults(
        new Defaults() {

          private final JsonProvider jsonProvider = new JacksonJsonProvider();
          private final MappingProvider mappingProvider = new JacksonMappingProvider();

          @Override
          public JsonProvider jsonProvider() {
            return jsonProvider;
          }

          @Override
          public Set<Option> options() {
            return EnumSet.noneOf(Option.class);
          }

          @Override
          public MappingProvider mappingProvider() {
            return mappingProvider;
          }
        });
  }

  @BeforeEach
  void setup() throws IOException {
    try (InputStream inputStream = getClass().getResourceAsStream("/root.json");
        ByteArrayOutputStream out = new ByteArrayOutputStream(); ) {
      inputStream.transferTo(out);
      json = out.toString(StandardCharsets.UTF_8);
    }

    objectMapper = new ObjectMapper();
    root = objectMapper.readValue(json, Root.class);
  }

  @Test
  @DisplayName("setValue")
  void setValue() {
    String newAuthorJson = JsonPath.parse(json).set("$.store.book[0].author", "Paul").jsonString();

    String author = JsonPath.parse(newAuthorJson).read("$.store.book[0].author", String.class);

    assertEquals("Paul", author);
  }
}
