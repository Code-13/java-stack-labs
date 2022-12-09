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

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.containsAnyIgnoreCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configuration.Defaults;
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
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JsonPathExamples.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 09:30
 */
public class JsonPathExamples {

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

  @AfterEach
  void tearDown() {}

  @Test
  @DisplayName("get_The_authors_of_all_books")
  void get_The_authors_of_all_books() {
    List<String> authors = JsonPath.read(json, "$.store.book[*].author");

    assertEquals(
        authors, List.of("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien"));
  }

  @Test
  @DisplayName("get_All_authors")
  void get_All_authors() {
    List<String> authors = JsonPath.read(json, "$..author");

    assertEquals(
        authors, List.of("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien"));
  }

  @Test
  @DisplayName("get_All_things_both_books_and_bicycles")
  void get_All_things_both_books_and_bicycles() {
    Object store = JsonPath.read(json, "$.store.*");
    assertEquals(
        "[[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],{\"color\":\"red\",\"price\":19.95}]",
        store.toString());
  }

  @Test
  @DisplayName("get_The_price_of_everything")
  void get_The_price_of_everything() {
    List<Double> prices =
        JsonPath.parse(json).read("$.store..price", new TypeRef<List<Double>>() {});

    assertEquals(List.of(8.95, 12.99, 8.99, 22.99, 19.95), prices);
  }

  @Test
  @DisplayName("get_the_third_book")
  void get_the_third_book() {
    Book book = JsonPath.parse(json).read("$.store.book[2]", Book.class);
    assertEquals(root.getStore().getBook().get(2), book);
  }

  @Test
  @DisplayName("get_The_second_to_last_book")
  void get_The_second_to_last_book() {
    Book book = JsonPath.parse(json).read("$.store.book[-2]", Book.class);
    assertEquals(root.getStore().getBook().get(2), book);
  }

  @Test
  @DisplayName("get_The_first_two_books")
  void get_The_first_two_books() {
    List<Book> books = JsonPath.parse(json).read("$.store.book[0,1]", new TypeRef<List<Book>>() {});

    List<Book> firstAndTwoBooks = root.getStore().getBook().stream().limit(2).toList();
    assertEquals(firstAndTwoBooks, books);
  }

  @Test
  @DisplayName("get_All_books_from_index0_inclusive_until_index2_exclusive")
  void get_All_books_from_index0_inclusive_until_index2_exclusive() {
    List<Book> books = JsonPath.parse(json).read("$.store.book[:2]", new TypeRef<List<Book>>() {});

    List<Book> firstAndTwoBooks = root.getStore().getBook().stream().limit(2).toList();
    assertEquals(firstAndTwoBooks, books);
  }

  @Test
  @DisplayName("get_All_books_from_index1_inclusive_until_index2_exclusive")
  void get_All_books_from_index1_inclusive_until_index2_exclusive() {
    List<Book> books = JsonPath.parse(json).read("$.store.book[1:2]", new TypeRef<List<Book>>() {});

    List<Book> twoBooks = singletonList(root.getStore().getBook().get(1));
    assertEquals(twoBooks, books);
  }

  @Test
  @DisplayName("get_All_books_with_an_ISBN_number")
  void get_All__books_with_an_ISBN_number() {
    List<Book> booksWithIsbnNumber =
        JsonPath.parse(json).read("$..book[?(@.isbn)]", new TypeRef<List<Book>>() {});

    List<Book> books =
        root.getStore().getBook().stream().filter(book -> book.getIsbn() != null).toList();

    assertEquals(books, booksWithIsbnNumber);
  }

  @Test
  @DisplayName("get_All_books_in_store_cheaper_than10")
  void get_All_books_in_store_cheaper_than10() {
    List<Book> books =
        JsonPath.parse(json).read("$..book[?(@.price < 10)]", new TypeRef<List<Book>>() {});

    List<Book> cheaperThan10 =
        root.getStore().getBook().stream().filter(book -> book.getPrice() < 10).toList();

    assertEquals(cheaperThan10, books);
  }

  @Test
  @DisplayName("get_All books in store that are not \"expensive\"")
  void get_All_books_in_store_that_are_not_expensive() {
    List<Book> books =
        JsonPath.parse(json)
            .read("$..book[?(@.price < $.expensive)]", new TypeRef<List<Book>>() {});

    int expensive = root.getExpensive();
    List<Book> booksThatAreNotExpensive =
        root.getStore().getBook().stream().filter(book -> book.getPrice() <= expensive).toList();

    assertEquals(booksThatAreNotExpensive, books);
  }

  @Test
  @DisplayName("get_All_books_matching_regex_ignore_case")
  void get_All_books_matching_regex_ignore_case() {
    List<Book> books =
        JsonPath.parse(json)
            .read("$..book[?(@.author =~ /.*REES/i)]", new TypeRef<List<Book>>() {});

    List<Book> booksMatchingRegexIgnoreCase =
        root.getStore().getBook().stream()
            .filter(book -> containsAnyIgnoreCase(book.getAuthor(), "REES"))
            .toList();

    assertEquals(booksMatchingRegexIgnoreCase, books);
  }

  @Test
  @DisplayName("get_the_number_of_book")
  void get_the_number_of_book() {
    Integer bookLength = JsonPath.parse(json).read("$..book.length()", int.class);

    int size = root.getStore().getBook().size();

    assertEquals(bookLength, size);
  }
}
