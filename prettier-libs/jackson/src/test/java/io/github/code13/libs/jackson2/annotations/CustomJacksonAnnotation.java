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

package io.github.code13.libs.jackson2.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CustomJacksonAnnotation.
 *
 * <p>Custom Jackson Annotation.
 *
 * @see com.fasterxml.jackson.annotation.JacksonAnnotationsInside
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/2 10:25
 */
public class CustomJacksonAnnotation {

  /*
   * We can make use of the @JacksonAnnotationsInside annotation to create a custom Jackson annotation
   */

  @Retention(RetentionPolicy.RUNTIME)
  @JacksonAnnotationsInside
  @JsonInclude(Include.NON_NULL)
  @JsonPropertyOrder({"name", "id", "dateCreated"})
  @interface CustomAnnotation {}

  /*
   * use the new annotation on an entity
   */

  @CustomAnnotation
  static class BeanWithCustomAnnotation {
    public int id;
    public String name;
    public LocalDateTime dateCreated;

    public BeanWithCustomAnnotation(int id, String name, LocalDateTime dateCreated) {
      this.id = id;
      this.name = name;
      this.dateCreated = dateCreated;
    }
  }

  @Test
  @DisplayName("whenSerializingUsingCustomAnnotation_thenCorrect")
  void whenSerializingUsingCustomAnnotation_thenCorrect() throws JsonProcessingException {
    BeanWithCustomAnnotation bean = new BeanWithCustomAnnotation(1, "My bean", null);

    String result = new ObjectMapper().writeValueAsString(bean);

    /* { "name":"My bean", "id":1 } */

    assertThat(result, containsString("My bean"));
    assertThat(result, containsString("1"));
    assertThat(result, not(containsString("dateCreated")));
  }
}
