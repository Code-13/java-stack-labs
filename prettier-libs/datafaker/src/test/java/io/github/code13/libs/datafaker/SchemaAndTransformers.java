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

package io.github.code13.libs.datafaker;

import static net.datafaker.transformations.Field.compositeField;
import static net.datafaker.transformations.Field.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.providers.base.Name;
import net.datafaker.sequence.FakeStream;
import net.datafaker.transformations.CsvTransformer;
import net.datafaker.transformations.Field;
import net.datafaker.transformations.JsonTransformer;
import net.datafaker.transformations.Schema;
import net.datafaker.transformations.SimpleField;
import net.datafaker.transformations.XmlTransformer;
import net.datafaker.transformations.YamlTransformer;
import net.datafaker.transformations.sql.SqlDialect;
import net.datafaker.transformations.sql.SqlTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SchemaAndTransformers.
 *
 * @see https://www.datafaker.net/documentation/schemas/
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/5 10:00
 */
class SchemaAndTransformers {

  @Test
  @DisplayName("schemaExamples")
  void schemaExamples() {
    Faker faker = new Faker();

    Schema<String, String> schema =
        Schema.of(
            field("first_name", () -> faker.name().firstName()),
            field("last_name", () -> faker.name().lastName()),
            field("address", () -> faker.address().streetAddress()));

    // or

    schema = Schema.of(compositeField("key", new Field[] {field("key", () -> "value")}));

    assertNotNull(schema);
  }

  @Test
  @DisplayName("csv")
  void csv() {
    Faker faker = new Faker();

    Schema<Name, String> schema =
        Schema.of(field("firstName", Name::firstName), field("lastname", Name::lastName));

    CsvTransformer<Name> transformer =
        new CsvTransformer.CsvTransformerBuilder<Name>().header(false).separator(" : ").build();

    String csv =
        transformer.generate(
            faker.<Name>collection().suppliers(faker::name).maxLen(100).build(), schema);

    assertNotNull(csv);
    System.out.println(csv);
  }

  @Test
  @DisplayName("json")
  void json() {
    Faker faker = new Faker();

    Schema<Object, ?> schema =
        Schema.of(
            field("Text", () -> faker.name().firstName()),
            field("Bool", () -> faker.bool().bool()));

    JsonTransformer<Object> transformer = new JsonTransformer<>();
    String json = transformer.generate(schema, 2);

    assertNotNull(json);
    System.out.println(json);
  }

  @Test
  @DisplayName("sql")
  void sql() {
    Faker faker = new Faker();
    Schema<String, String> schema =
        Schema.of(
            field("firstName", () -> faker.name().firstName()),
            field("lastName", () -> faker.name().lastName()));
    SqlTransformer<String> transformer =
        new SqlTransformer.SqlTransformerBuilder<String>()
            .batch(5)
            .tableName("MY_TABLE")
            .dialect(SqlDialect.POSTGRES)
            .build();
    String output = transformer.generate(schema, 10);
    assertNotNull(output);
    System.out.println(output);
  }

  @Test
  @DisplayName("yaml")
  void yaml() {
    BaseFaker faker = new BaseFaker();

    YamlTransformer<Object> transformer = new YamlTransformer<>();
    Schema<Object, ?> schema =
        Schema.of(
            field("name", () -> faker.name().firstName()),
            field("lastname", () -> faker.name().lastName()),
            field(
                "phones",
                () ->
                    Schema.of(
                        field(
                            "worknumbers",
                            () ->
                                ((Stream<?>)
                                        faker.<String>stream()
                                            .suppliers(() -> faker.phoneNumber().phoneNumber())
                                            .maxLen(2)
                                            .build()
                                            .get())
                                    .collect(Collectors.toList())),
                        field(
                            "cellphones",
                            () ->
                                ((Stream<?>)
                                        faker.<String>stream()
                                            .suppliers(() -> faker.phoneNumber().cellPhone())
                                            .maxLen(3)
                                            .build()
                                            .get())
                                    .collect(Collectors.toList())))),
            field(
                "address",
                () ->
                    Schema.of(
                        field("city", () -> faker.address().city()),
                        field("country", () -> faker.address().country()),
                        field("streetAddress", () -> faker.address().streetAddress()))));

    String yaml = transformer.generate(schema, 1);
    assertNotNull(yaml);
    System.out.println(yaml);
  }

  @Test
  @DisplayName("xml")
  void xml() {
    BaseFaker faker = new BaseFaker();
    FakeStream<?> address =
        (FakeStream<SimpleField<String, List<Object>>>)
            faker.<SimpleField<String, List<Object>>>stream()
                .suppliers(
                    () ->
                        field(
                            "address",
                            () ->
                                Arrays.asList(
                                    field("country", () -> faker.address().country()),
                                    field("city", () -> faker.address().city()),
                                    field("streetAddress", () -> faker.address().streetAddress()))))
                .maxLen(3)
                .build();

    FakeStream<?> persons =
        (FakeStream<SimpleField<Object, List<Object>>>)
            faker.<SimpleField<Object, List<Object>>>stream()
                .suppliers(
                    () ->
                        field(
                            "person",
                            () ->
                                Arrays.asList(
                                    field("firstname", () -> faker.name().firstName()),
                                    field("lastname", () -> faker.name().lastName()),
                                    field(
                                        "addresses",
                                        () -> address.get().collect(Collectors.toList())))))
                .maxLen(3)
                .build();

    XmlTransformer<Object> xmlTransformer =
        new XmlTransformer.XmlTransformerBuilder<>().pretty(true).build();

    CharSequence generate =
        xmlTransformer.generate(
            Schema.of(field("persons", () -> persons.get().collect(Collectors.toList()))), 1);

    assertNotNull(generate);
    System.out.println(generate);
  }
}
