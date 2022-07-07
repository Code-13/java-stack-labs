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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configuration.Defaults;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ConfigurationExample.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 14:15
 */
public class ConfigurationExample {

  String json =
      """
          [
             {
                "name" : "john",
                "gender" : "male"
             },
             {
                "name" : "ben"
             }
          ]""";

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

    //    Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
  }

  @AfterEach
  void tearDown() {}

  @Test
  @DisplayName("test_DEFAULT_PATH_LEAF_TO_NULL")
  void test_DEFAULT_PATH_LEAF_TO_NULL() {
    Configuration conf = Configuration.defaultConfiguration();

    assertAll(
        () -> assertNotNull(JsonPath.using(conf).parse(json).read("$[0].gender")),
        () ->
            assertThrows(
                PathNotFoundException.class,
                () -> JsonPath.using(conf).parse(json).read("$[1].gender")));

    conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    assertAll(
        () -> assertNotNull(JsonPath.using(conf).parse(json).read("$[0].gender")),
        () -> assertNotNull(JsonPath.using(conf).parse(json).read("$[0].gender")));
  }
}
