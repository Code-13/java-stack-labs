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

package io.github.code13.libs.jackson2.optional;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * UsingOptionalWithJackson.
 *
 * <p>add dep for gradle : {@code implementation
 * 'com.fasterxml.jackson.datatype:jackson-datatype-jdk8'}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/7 16:03
 */
public class UsingOptionalWithJackson {

  ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

  @NoArgsConstructor
  @Getter
  @Setter
  static class Book {
    String title;
    Optional<String> subTitle;
  }

  @Test
  @DisplayName("serializationWithOptional")
  void serializationWithOptional() throws JsonProcessingException {
    //  If we try and serialize our Book object, we'll see that there is now a subtitle, as
    // opposed to a nested JSON
    Book book = new Book();
    book.setTitle("Oliver Twist");
    book.setSubTitle(Optional.of("The Parish Boy's Progress"));

    String result = objectMapper.writeValueAsString(book);

    assertThat(parse(result).read("$.subTitle", String.class))
        .isEqualTo("The Parish Boy's Progress");

    // If we try serializing an empty book, it will be stored as null

    book = new Book();
    result = objectMapper.writeValueAsString(book);
    assertThat(parse(result).read("$.subTitle", String.class)).isNull();
  }

  @Test
  @DisplayName("deserializationWithOptional")
  void givenField_whenDeserializingIntoOptional_thenIsPresentWithValue()
      throws JsonProcessingException {

    String subTitle = "The Parish Boy's Progress";
    String book = "{ \"title\": \"Oliver Twist\", \"subTitle\": \"" + subTitle + "\" }";

    Book result = objectMapper.readValue(book, Book.class);

    assertThat(result.getSubTitle()).isEqualTo(Optional.of(subTitle));
  }

  @Test
  @DisplayName("givenNullField_whenDeserializingIntoOptional_thenIsEmpty")
  void givenNullField_whenDeserializingIntoOptional_thenIsEmpty() throws JsonProcessingException {
    String book = "{ \"title\": \"Oliver Twist\", \"subTitle\": null }";

    Book result = objectMapper.readValue(book, Book.class);

    assertThat(result.getSubTitle()).isEqualTo(Optional.empty());
  }
}
