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

package io.github.code13.javastack.libs.jackson2.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JacksonSerializationAnnotationsRunner.
 *
 * <p>每一个静态内部类都只测试了一个注解，顺序如下：
 *
 * @see com.fasterxml.jackson.annotation.JsonAnyGetter
 * @see com.fasterxml.jackson.annotation.JsonGetter
 * @see com.fasterxml.jackson.annotation.JsonPropertyOrder
 * @see com.fasterxml.jackson.annotation.JsonRawValue
 * @see com.fasterxml.jackson.annotation.JsonValue
 * @see com.fasterxml.jackson.annotation.JsonRootName
 * @see com.fasterxml.jackson.databind.annotation.JsonSerialize
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/31 22:10
 */
@DisplayName("Jackson Serialization Annotations")
public class SerializationAnnotations {

  /** {@link JsonAnyGetter} 注解允许灵活地使用 Map 字段作为标准属性。 */
  @DisplayName("Test for JsonAnyGetter")
  static class JsonAnyGetterTest {

    @Test
    @DisplayName("whenSerializingUsingJsonAnyGetter_thenCorrect")
    void whenSerializingUsingJsonAnyGetter_thenCorrect() throws JsonProcessingException {
      ExtendableBean bean = new ExtendableBean("My bean");
      bean.add("attr1", "val1");
      bean.add("attr2", "val2");

      String result = new ObjectMapper().writeValueAsString(bean);

      /* { "name":"My bean", "attr2":"val2", "attr1":"val1" } */

      assertThat(result, containsString("attr1"));
      assertThat(result, containsString("val1"));
    }

    /** ExtendableBean 实体具有 name 属性和一组键值对形式的可扩展属性： */
    static class ExtendableBean {
      public final String name;
      private final Map<String, String> properties;

      public ExtendableBean(String name) {
        this.name = name;
        properties = new HashMap<>();
      }

      @JsonAnyGetter
      public Map<String, String> getProperties() {
        return properties;
      }

      public void add(String k, String v) {
        properties.put(k, v);
      }
    }
  }

  /**
   * {@link JsonGetter} 注解是 {@link com.fasterxml.jackson.annotation.JsonProperty}
   * 注解的替代品，它将方法标记为getter 方法。
   */
  static class JsonGetterTest {

    @Test
    @DisplayName("whenSerializingUsingJsonGetter_thenCorrect")
    void whenSerializingUsingJsonGetter_thenCorrect() throws JsonProcessingException {
      MyBean bean = new MyBean(1, "My bean");

      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("My bean"));
      assertThat(result, containsString("1"));
    }

    static class MyBean {
      public int id;
      private String name;

      public MyBean(int id, String name) {
        this.id = id;
        this.name = name;
      }

      @JsonGetter("name")
      public String getTheName() {
        return name;
      }
    }
  }

  /** 我们可以使用 {@link JsonPropertyOrder} 注解来指定序列化属性的顺序。 */
  static class JsonPropertyOrderTest {

    @Test
    void whenSerializingUsingJsonPropertyOrder_thenCorrect() throws JsonProcessingException {

      MyBean bean = new MyBean(1, "My bean");

      String result = new ObjectMapper().writeValueAsString(bean);

      /* { "name":"My bean", "id":1 } */

      assertThat(result, containsString("My bean"));
      assertThat(result, containsString("1"));
    }

    @JsonPropertyOrder({"name", "id"})
    static class MyBean {
      public int id;
      public String name;

      public MyBean(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    /** 我们还可以使用 @JsonPropertyOrder(alphabetic=true) 按字母顺序对属性进行排序。 */
    @JsonPropertyOrder(alphabetic = true)
    static class MyBean2 {
      public int id;
      public String name;

      public MyBean2(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }
  }

  /** {@link JsonRawValue} 注释可以指示 Jackson 完全按原样序列化属性。 */
  static class JsonRawValueTest {

    @Test
    @DisplayName("whenSerializingUsingJsonRawValue_thenCorrect")
    void whenSerializingUsingJsonRawValue_thenCorrect() throws JsonProcessingException {
      RawBean bean = new RawBean("My bean", "\"attr\": false");

      String result = new ObjectMapper().writeValueAsString(bean);

      /* { "name":"My bean", "json":{ "attr":false } } */

      assertThat(result, containsString("My bean"));
      assertThat(result, containsString("\"attr\": false"));
    }

    /** we use @JsonRawValue to embed some custom JSON as a value of an entity: */
    static class RawBean {
      public String name;
      @JsonRawValue public String json;

      public RawBean(String name, String json) {
        this.name = name;
        this.json = json;
      }
    }
  }

  /** {@link JsonValue} 表示库将用于序列化整个实例的单个方法。 */
  static class JsonValueTest {

    @Test
    @DisplayName("whenSerializingUsingJsonValue_thenCorrect")
    void whenSerializingUsingJsonValue_thenCorrect() throws JsonProcessingException {
      String enumAsString = new ObjectMapper().writeValueAsString(TypeEnumWithValue.TYPE1);

      assertThat(enumAsString, is("\"Type A\""));
    }

    /**
     * in an enum, we annotate the getName with @JsonValue so that any such entity is serialized via
     * its name:
     */
    public enum TypeEnumWithValue {
      TYPE1(1, "Type A"),
      TYPE2(2, "Type 2");

      private final Integer id;
      private final String name;

      TypeEnumWithValue(Integer id, String name) {
        this.id = id;
        this.name = name;
      }

      @JsonValue
      public String getName() {
        return name;
      }
    }
  }

  /** 如果启用了包装，则使用 {@link JsonRootName} 注释来指定要使用的根包装器的名称。 */
  static class JsonRootNameTest {

    @Test
    @DisplayName("whenSerializingUsingJsonRootName_thenCorrect")
    void whenSerializingUsingJsonRootName_thenCorrect() throws JsonProcessingException {
      UserWithRoot user = new UserWithRoot(1, "John");

      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);

      String result = objectMapper.writeValueAsString(user);

      /* { "user":{ "id":1, "name":"John" } } */

      assertThat(result, containsString("John"));
      assertThat(result, containsString("user"));
    }

    @JsonRootName(value = "user")
    static class UserWithRoot {
      public int id;
      public String name;

      public UserWithRoot(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    /**
     * Since Jackson 2.4, a new optional argument namespace is available to use with data formats
     * such as XML. If we add it, it will become part of the fully qualified name:
     */
    @JsonRootName(value = "user", namespace = "users")
    static class UserWithRootXml {
      public int id;
      public String name;

      public UserWithRootXml(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }
  }

  /** {@link JsonSerialize} indicates a custom serializer to use when marshalling the entity. */
  static class JsonSerializeTest {

    @Test
    @DisplayName("whenSerializingUsingJsonSerialize_thenCorrect")
    void whenSerializingUsingJsonSerialize_thenCorrect() throws JsonProcessingException {

      LocalDateTime localDateTime = LocalDateTime.of(2022, 6, 1, 12, 0, 0);

      EventWithSerializer eventWithSerializer = new EventWithSerializer("party", localDateTime);

      String result = new ObjectMapper().writeValueAsString(eventWithSerializer);

      System.out.println(result);

      assertThat(result, is("{\"name\":\"party\",\"eventDate\":\"2022-06-01 12:00:00\"}"));
    }

    /**
     * We're going to use @JsonSerialize to serialize the eventDate property with a
     * CustomDateSerializer:
     */
    static class EventWithSerializer {
      public String name;

      @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
      public LocalDateTime eventDate;

      public EventWithSerializer(String name, LocalDateTime eventDate) {
        this.name = name;
        this.eventDate = eventDate;
      }
    }

    static class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

      private static final DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

      public CustomLocalDateTimeSerializer() {
        this(LocalDateTime.class);
      }

      protected CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
      }

      @Override
      public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
          throws IOException {
        if (value == null) {
          gen.writeString("");
        } else {
          gen.writeString(value.format(formatter));
        }
      }
    }
  }
}
