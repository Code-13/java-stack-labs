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

package io.github.code13.libs.jsonpath;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configuration.Defaults;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PredicatesExamples.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 13:21
 */
public class PredicatesExamples {

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

  /*
   * There are three different ways to create filter predicates in JsonPath.
   */

  @Test
  @DisplayName("inlinePredicate")
  void inline() {
    List<Book> books =
        JsonPath.parse(json)
            .read(
                "$..book[?(@.price < 10 && @.category == 'fiction')]",
                new TypeRef<List<Book>>() {});

    List<Book> booksInlinePredicate =
        root.getStore().getBook().stream()
            .filter(book -> book.getPrice() < 10 && "fiction".equals(book.getCategory()))
            .toList();

    assertEquals(books, booksInlinePredicate);
  }

  @Test
  @DisplayName("FilterPredicates")
  void filterPredicates() throws JsonProcessingException {

    Filter filter = filter(where("category").is("fiction").and("price").lte(10D));

    /*
     * Notice the placeholder ? for the filter in the path. When multiple filters are provided they
     * are applied in order where the number of placeholders must match the number of provided
     * filters. You can specify multiple predicate placeholders in one filter operation [?, ?], both
     * predicates must match.
     */
    List bookList = JsonPath.parse(json).read("$..book[?]", List.class, filter);

    List<Book> books =
        objectMapper.readValue(
            objectMapper.writeValueAsString(bookList), new TypeReference<List<Book>>() {});

    List<Book> booksInlinePredicate =
        root.getStore().getBook().stream()
            .filter(book -> book.getPrice() < 10 && "fiction".equals(book.getCategory()))
            .toList();

    assertEquals(books, booksInlinePredicate);
  }

  @Test
  @DisplayName("implement_your_own_predicates")
  void implement_your_own_predicates() throws JsonProcessingException {
    List isbnList =
        JsonPath.parse(json)
            .read(
                "$.store.book[?].isbn", List.class, ctx -> ctx.item(Map.class).containsKey("isbn"));

    List<String> isbns =
        objectMapper.readValue(
            objectMapper.writeValueAsString(isbnList), new TypeReference<List<String>>() {});

    List<String> isbnWithPredicates =
        root.getStore().getBook().stream().map(Book::getIsbn).filter(Objects::nonNull).toList();

    assertEquals(isbns, isbnWithPredicates);
  }
}
