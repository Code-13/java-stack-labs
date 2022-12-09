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

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.datafaker.Faker;
import net.datafaker.formats.Csv;
import net.datafaker.formats.Format;
import net.datafaker.formats.Xml;
import net.datafaker.formats.Yaml;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * FileFormats.
 *
 * @deprecated use {@link SchemaAndTransformers} instead
 * @see https://www.datafaker.net/documentation/formats/
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/8 10:00
 */
@Deprecated
class FileFormats {

  @Test
  @DisplayName("formatWithCsv")
  void formatWithCsv() {
    Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
    System.out.println(
        Format.toCsv(
                Csv.Column.of("first_name", () -> faker.name().firstName()),
                Csv.Column.of("last_name", () -> faker.name().lastName()),
                Csv.Column.of("address", () -> faker.address().streetAddress()))
            .header(true)
            .separator(" ; ")
            .limit(5)
            .build()
            .get());
  }

  @Test
  @DisplayName("formatWithJson")
  void formatWithJson() {
    Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
    String json =
        Format.toJson(faker.<Name>collection().suppliers(faker::name).maxLen(2).minLen(2).build())
            .set("firstName", Name::firstName)
            .set("lastName", Name::lastName)
            .set(
                "address",
                Format.toJson(
                        faker
                            .<Address>collection()
                            .suppliers(faker::address)
                            .maxLen(1)
                            .minLen(1)
                            .build())
                    .set("country", Address::country)
                    .set("city", Address::city)
                    .set("zipcode", Address::zipCode)
                    .set("streetAddress", Address::streetAddress)
                    .build())
            .set(
                "phones",
                name ->
                    faker
                        .<String>collection()
                        .suppliers(() -> faker.phoneNumber().phoneNumber())
                        .maxLen(3)
                        .build()
                        .get())
            .build()
            .generate();

    System.out.println(json);
  }

  @Test
  @DisplayName("formatWithYml")
  void formatWithYml() {
    Faker faker = new Faker();
    Map<Supplier<String>, Supplier<Object>> map = new LinkedHashMap<>();
    Map<Supplier<String>, Supplier<Object>> address = new LinkedHashMap<>();
    Map<Supplier<String>, Supplier<Object>> phones = new LinkedHashMap<>();
    phones.put(
        () -> "worknumbers",
        () ->
            faker
                .<String>collection()
                .suppliers(() -> faker.phoneNumber().phoneNumber())
                .maxLen(2)
                .build()
                .get());
    phones.put(
        () -> "cellphones",
        () ->
            faker
                .<String>collection()
                .suppliers(() -> faker.phoneNumber().cellPhone())
                .maxLen(3)
                .build()
                .get());
    address.put(() -> "city", () -> faker.address().city());
    address.put(() -> "country", () -> faker.address().country());
    address.put(() -> "streetAddress", () -> faker.address().streetAddress());
    map.put(() -> "name", () -> faker.name().firstName());
    map.put(() -> "lastname", () -> faker.name().lastName());
    map.put(() -> "address", () -> address);
    map.put(() -> "phones", () -> phones);
    Yaml yaml = new Yaml(map);
    System.out.println(yaml.generate());
  }

  @Test
  @DisplayName("formatWithXmlWithEleAndAttr")
  void formatWithXmlWithEleAndAttr() {
    Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);

    Collection<Xml.XmlNode> address =
        faker
            .<Xml.XmlNode>collection()
            .suppliers(
                () ->
                    new Xml.XmlNode(
                        "address",
                        map(
                            entry("country", faker.address().country()),
                            entry("city", faker.address().city()),
                            entry("streetAddress", faker.address().streetAddress())),
                        Collections.emptyList()))
            .maxLen(3)
            .build()
            .get();

    Collection<Xml.XmlNode> persons =
        faker
            .<Xml.XmlNode>collection()
            .suppliers(
                () ->
                    new Xml.XmlNode(
                        "person",
                        map(
                            entry("firstname", faker.name().firstName()),
                            entry("lastname", faker.name().lastName())),
                        of(new Xml.XmlNode("addresses", address))))
            .maxLen(3)
            .build()
            .get();

    String str = new Xml(new Xml.XmlNode("persons", persons)).generate(true);
    System.out.println(str);
  }

  private static <T> Collection<T> of(T... elems) {
    return Arrays.asList(elems);
  }

  private static Map.Entry<String, String> entry(String key, String value) {
    return new AbstractMap.SimpleEntry<>(key, value);
  }

  private static Map<String, String> map(Map.Entry<String, String>... entries) {
    Map<String, String> map = new LinkedHashMap<>();
    for (Map.Entry<String, String> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }

  @Test
  @DisplayName("formatWithXmlElementOnly")
  void formatWithXmlElementOnly() {
    Faker faker = new Faker();
    Collection<Xml.XmlNode> address =
        faker
            .<Xml.XmlNode>collection()
            .suppliers(
                () ->
                    new Xml.XmlNode(
                        "address",
                        of(
                            new Xml.XmlNode("country", faker.address().country()),
                            new Xml.XmlNode("city", faker.address().city()),
                            new Xml.XmlNode("streetAddress", faker.address().streetAddress()))))
            .maxLen(4)
            .build()
            .get();
    Collection<Xml.XmlNode> persons =
        faker
            .<Xml.XmlNode>collection()
            .suppliers(
                () ->
                    new Xml.XmlNode(
                        "person",
                        of(
                            new Xml.XmlNode("firstname", faker.name().firstName()),
                            new Xml.XmlNode("lastname", faker.name().lastName()),
                            new Xml.XmlNode("addresses", address))))
            .maxLen(2)
            .build()
            .get();

    String str = new Xml(new Xml.XmlNode("persons", persons)).generate(true);
    System.out.println(str);
  }
}
