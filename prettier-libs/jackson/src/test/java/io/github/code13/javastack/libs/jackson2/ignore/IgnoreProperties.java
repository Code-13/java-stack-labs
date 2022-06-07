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

package io.github.code13.javastack.libs.jackson2.ignore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * How to ignore certain fields when serializing an object to JSON.
 *
 * @see com.fasterxml.jackson.annotation.JsonIgnoreProperties
 * @see com.fasterxml.jackson.annotation.JsonIgnore
 * @see com.fasterxml.jackson.annotation.JsonIgnoreType
 * @see com.fasterxml.jackson.annotation.JsonFilter
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/7 11:20
 */
public class IgnoreProperties {

  /**
   * We can ignore specific fields at the class level, using the @JsonIgnoreProperties annotation
   * and specifying the fields by name
   */
  static class IgnoreFieldsAtTheClassLevelTest {

    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(value = {"intValue"})
    static class MyDto {

      private String stringValue;
      private int intValue;
      private boolean booleanValue;
    }

    @Test
    @DisplayName("givenFieldIsIgnoredByName_whenDtoIsSerialized_thenCorrect")
    void givenFieldIsIgnoredByName_whenDtoIsSerialized_thenCorrect()
        throws JsonProcessingException {
      String result = new ObjectMapper().writeValueAsString(new MyDto());

      assertThat(result, not(containsString("intValue")));
    }
  }

  /** We can also ignore a field directly via the @JsonIgnore annotation directly on the field */
  static class IgnoreFieldAtTheFieldLevelTest {

    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDto {
      private String stringValue;
      @JsonIgnore private int intValue;
      private boolean booleanValue;
    }

    @Test
    @DisplayName("givenFieldIsIgnoredDirectly_whenDtoIsSerialized_thenCorrect")
    void givenFieldIsIgnoredDirectly_whenDtoIsSerialized_thenCorrect()
        throws JsonProcessingException {
      String result = new ObjectMapper().writeValueAsString(new MyDto());

      assertThat(result, not(containsString("intValue")));
    }
  }

  /**
   * Finally, we can ignore all fields of a specified type, using the @JsonIgnoreType annotation. If
   * we control the type, then we can annotate the class directly.
   */
  static class IgnoreAllFieldsByTypeTest {

    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDtoWithSpecialField {
      private String[] stringValue;
      private int intValue;
      private boolean booleanValue;
    }

    @JsonIgnoreType
    static class MyMixInForIgnoreType {}

    @Test
    @DisplayName("givenFieldTypeIsIgnored_whenDtoIsSerialized_thenCorrect")
    void givenFieldTypeIsIgnored_whenDtoIsSerialized_thenCorrect() throws JsonProcessingException {
      // At this point, all String arrays will be ignored instead of marshalled to JSON:

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.addMixIn(String[].class, MyMixInForIgnoreType.class);

      MyDtoWithSpecialField dto = new MyDtoWithSpecialField();
      dto.setBooleanValue(true);

      String result = objectMapper.writeValueAsString(dto);

      assertThat(result, containsString("intValue"));
      assertThat(result, containsString("booleanValue"));
      assertThat(result, not(containsString("stringValue")));
    }
  }

  /** we can also use filters to ignore specific fields in Jackson. */
  static class IgnoreFieldsUsingFiltersTest {

    /** First, we need to define the filter on the Java object */
    @JsonFilter("myFilter")
    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDtoWithFilter {
      private String[] stringValue;
      private int intValue;
      private boolean booleanValue;
    }

    @Test
    @DisplayName("givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect")
    void givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect()
        throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();

      SimpleBeanPropertyFilter beanPropertyFilter =
          SimpleBeanPropertyFilter.serializeAllExcept("intValue");
      SimpleFilterProvider filter =
          new SimpleFilterProvider().addFilter("myFilter", beanPropertyFilter);

      MyDtoWithFilter dto = new MyDtoWithFilter();
      String result = objectMapper.writer(filter).writeValueAsString(dto);

      assertThat(result, not(containsString("intValue")));
      assertThat(result, containsString("stringValue"));
      assertThat(result, containsString("booleanValue"));
    }
  }
}
