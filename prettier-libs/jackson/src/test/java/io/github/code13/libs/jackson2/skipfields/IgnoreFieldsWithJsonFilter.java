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

package io.github.code13.libs.jackson2.skipfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * IgnoreFieldsWithJsonFilter.
 *
 * <p>Use Jackson Filter to Control the Serialization Process
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/8 15:47
 */
public class IgnoreFieldsWithJsonFilter {

  @JsonFilter("myFilter")
  @Getter
  @Setter
  static class MyDtoWithFilter {
    private String stringValue;
    private int intValue;
    private boolean booleanValue;

    public MyDtoWithFilter() {
      super();
    }

    public MyDtoWithFilter(String stringValue, int intValue, boolean booleanValue) {
      super();

      this.stringValue = stringValue;
      this.intValue = intValue;
      this.booleanValue = booleanValue;
    }
  }

  @Test
  @DisplayName("givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect")
  void givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect()
      throws JsonProcessingException {

    ObjectMapper mapper = new ObjectMapper();
    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("intValue");
    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);

    MyDtoWithFilter dtoObject = new MyDtoWithFilter();
    dtoObject.setIntValue(12);

    String dtoAsString = mapper.writer(filters).writeValueAsString(dtoObject);

    assertThat(dtoAsString, not(containsString("intValue")));
    assertThat(dtoAsString, containsString("booleanValue"));
    assertThat(dtoAsString, containsString("stringValue"));
  }

  @Test
  @DisplayName("givenTypeHasFilterThatIgnoresNegativeInt_whenDtoIsSerialized_thenCorrect")
  void givenTypeHasFilterThatIgnoresNegativeInt_whenDtoIsSerialized_thenCorrect()
      throws JsonProcessingException {
    PropertyFilter filter =
        new SimpleBeanPropertyFilter() {
          @Override
          public void serializeAsField(
              Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
              throws Exception {
            if (include(writer)) {
              if (!writer.getName().equals("intValue")) {
                writer.serializeAsField(pojo, jgen, provider);
              } else {
                int intValue = ((MyDtoWithFilter) pojo).getIntValue();
                if (intValue >= 0) {
                  writer.serializeAsField(pojo, jgen, provider);
                }
              }
            } else {
              writer.serializeAsOmittedField(pojo, jgen, provider);
            }
          }

          @Override
          protected boolean include(BeanPropertyWriter writer) {
            return true;
          }

          @Override
          protected boolean include(PropertyWriter writer) {
            return true;
          }
        };

    SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("myFilter", filter);

    MyDtoWithFilter dtoObject = new MyDtoWithFilter();
    dtoObject.setIntValue(-1);

    String result = new ObjectMapper().writer(filterProvider).writeValueAsString(dtoObject);

    assertThat(result, not(containsString("intValue")));
    assertThat(result, containsString("booleanValue"));
    assertThat(result, containsString("stringValue"));
  }
}
