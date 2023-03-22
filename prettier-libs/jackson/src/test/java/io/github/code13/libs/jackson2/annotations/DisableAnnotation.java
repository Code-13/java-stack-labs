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
import static org.hamcrest.core.StringContains.containsString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DisableAnnotation.
 *
 * <p>Disable Jackson Annotation.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/2 10:43
 */
public class DisableAnnotation {

  /*
   * Finally, let's see how we can disable all Jackson annotations.
   * We can do this by disabling the MapperFeature.USE_ANNOTATIONS as in the following example:
   */

  @JsonInclude(Include.NON_NULL)
  @JsonPropertyOrder({"name", "id"})
  static class MyBean {
    public int id;
    public String name;

    public MyBean(int id, String name) {
      this.id = id;
      this.name = name;
    }
  }

  @Test
  @DisplayName("whenDisablingAllAnnotations_thenAllDisabled")
  void whenDisablingAllAnnotations_thenAllDisabled() throws JsonProcessingException {
    MyBean bean = new MyBean(1, null);

    String result =
        new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS).writeValueAsString(bean);

    assertThat(result, containsString("1"));
    assertThat(result, containsString("name"));
  }
}
