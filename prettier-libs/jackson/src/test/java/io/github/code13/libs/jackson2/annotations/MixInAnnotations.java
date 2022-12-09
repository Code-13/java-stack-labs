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
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * MixInAnnotations.
 *
 * <p>Jackson MixIn Annotations.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/2 10:35
 */
public class MixInAnnotations {

  static class Item {
    public int id;
    public String itemName;
    public User owner;

    public Item(int id, String itemName, User owner) {
      this.id = id;
      this.itemName = itemName;
      this.owner = owner;
    }
  }

  static class User {
    public int id;

    public User(int id) {
      this.id = id;
    }
  }

  @JsonIgnoreType
  static class MyMixInForIgnoreType {}

  @Test
  @DisplayName("whenSerializingUsingMixInAnnotation_thenCorrect")
  void whenSerializingUsingMixInAnnotation_thenCorrect() throws JsonProcessingException {
    Item item = new Item(1, "My bean", null);

    String result = new ObjectMapper().writeValueAsString(item);
    assertThat(result, containsString("owner"));

    result =
        new ObjectMapper()
            .addMixIn(User.class, MyMixInForIgnoreType.class)
            .writeValueAsString(item);

    assertThat(result, not(containsString("owner")));
  }
}
