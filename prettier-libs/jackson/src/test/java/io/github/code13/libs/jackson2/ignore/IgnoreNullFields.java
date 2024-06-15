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

package io.github.code13.libs.jackson2.ignore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * IgnoreNullFields.
 *
 * <p>This quick tutorial is going to cover how to set up Jackson to ignore null fields when
 * serializing a java class.
 *
 * @see com.fasterxml.jackson.annotation.JsonInclude
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/7 13:32
 */
public class IgnoreNullFields {

  @Nested
  class IgnoreNullFieldsOnTheClassOrFieldTest {

    /*
     * Jackson allow us to control this behavior at either the class level:
     */
    @JsonInclude(Include.NON_NULL)
    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDtoOnClassLevel {
      private String stringValue;
      private int intValue;
    }

    /*
     * Or with more granularity at the field level:
     */
    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDtoOnFieldLevel {

      @JsonInclude(Include.NON_NULL)
      private String stringValue;

      private int intValue;
    }

    @Test
    @DisplayName("givenNullsIgnoredOnClass_whenWritingObjectWithNullField_thenIgnored")
    void givenNullsIgnoredOnClass_whenWritingObjectWithNullField_thenIgnored()
        throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      MyDtoOnClassLevel dto = new MyDtoOnClassLevel();

      String result = objectMapper.writeValueAsString(dto);

      assertThat(result, containsString("intValue"));
      assertThat(result, not(containsString("stringValue")));
    }
  }

  /** Jackson also allows us to configure this behavior globally on the ObjectMapper. */
  @Nested
  class IgnoreNullFieldsGloballyTest {

    @NoArgsConstructor
    @Getter
    @Setter
    static class MyDto {
      private String stringValue;
      private int intValue;
      private boolean booleanValue;
    }

    @Test
    @DisplayName("givenNullsIgnoredGlobally_whenWritingObjectWithNullField_thenIgnored")
    void givenNullsIgnoredGlobally_whenWritingObjectWithNullField_thenIgnored()
        throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();

      // Jackson also allows us to configure this behavior globally on the ObjectMapper:
      objectMapper.setSerializationInclusion(Include.NON_NULL);

      MyDto dto = new MyDto();
      String result = objectMapper.writeValueAsString(dto);

      assertThat(result, containsString("intValue"));
      assertThat(result, containsString("booleanValue"));
      assertThat(result, not(containsString("stringValue")));
    }
  }
}
