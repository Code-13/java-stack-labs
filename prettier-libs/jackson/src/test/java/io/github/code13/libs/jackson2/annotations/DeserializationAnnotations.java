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

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.InjectableValues.Std;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * DeserializationAnnotationsRunner.
 *
 * <p>每一个静态内部类都只测试了一个注解，顺序如下：
 *
 * @see com.fasterxml.jackson.annotation.JsonCreator
 * @see com.fasterxml.jackson.annotation.JacksonInject
 * @see com.fasterxml.jackson.annotation.JsonAnySetter
 * @see com.fasterxml.jackson.annotation.JsonSetter
 * @see com.fasterxml.jackson.databind.annotation.JsonDeserialize
 * @see com.fasterxml.jackson.annotation.JsonAlias
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/1 14:58
 */
public class DeserializationAnnotations {

  /**
   * We can use the {@link JsonCreator} annotation to tune the constructor/factory used in
   * deserialization.
   */
  static class JsonCreatorTest {

    /** deserialize the following JSON */
    static final String json = "{ \"id\":1, \"theName\":\"My bean\" }";

    /**
     * However, there is no theName field in our target entity, there is only a name field. Now we
     * don't want to change the entity itself, we just need a little more control over the
     * unmarshalling process by annotating the constructor with @JsonCreator, and using
     * the @JsonProperty annotation as well:
     */
    static class BeanWithCreator {
      public final int id;
      public final String name;

      @JsonCreator
      BeanWithCreator(@JsonProperty("id") int id, @JsonProperty("theName") String name) {
        this.id = id;
        this.name = name;
      }
    }

    static class BeanWithCreatorFactory {
      public final int id;
      public final String name;

      private BeanWithCreatorFactory(int id, String name) {
        this.id = id;
        this.name = name;
      }

      @JsonCreator
      public static BeanWithCreatorFactory from(
          @JsonProperty("id") int id, @JsonProperty("theName") String name) {
        return new BeanWithCreatorFactory(id, name);
      }
    }

    @Test
    @DisplayName("test JsonCreator use constructor")
    void whenDeserializingUsingJsonCreator_withConstructor_thenCorrect()
        throws JsonProcessingException {
      BeanWithCreator bean = new ObjectMapper().readerFor(BeanWithCreator.class).readValue(json);

      assertEquals("My bean", bean.name);
    }

    @Test
    @DisplayName("test JsonCreator use factory method")
    void whenDeserializingUsingJsonCreator_withFactory_thenCorrect()
        throws JsonProcessingException {
      BeanWithCreatorFactory bean =
          new ObjectMapper().readerFor(BeanWithCreatorFactory.class).readValue(json);

      assertEquals("My bean", bean.name);
    }
  }

  /** {@link JacksonInject} 表示属性将被注入而不是从 JSON 数据中获取其值。 */
  static class JsonInjectTest {

    /** deserialize the following JSON */
    static final String json = "{ \"name\":\"My bean\" }";

    /** we use @JacksonInject to inject the property id: */
    static class BeanWithInject {

      @JacksonInject public int id;
      public String name;
    }

    @Test
    @DisplayName("whenDeserializingUsingJsonInject_thenCorrect")
    void whenDeserializingUsingJsonInject_thenCorrect() throws JsonProcessingException {
      InjectableValues inject = new Std().addValue(int.class, 1);

      BeanWithInject bean =
          new ObjectMapper().reader(inject).forType(BeanWithInject.class).readValue(json);

      assertEquals("My bean", bean.name);
      assertEquals(1, bean.id);
    }
  }

  /** {@link JsonAnySetter} 允许我们灵活地使用 Map 作为标准属性。在反序列化时，来自 JSON 的属性将简单地添加到 map 中。 */
  static class JsonAnySetterTest {
    /** deserialize the following JSON */
    static final String json = "{ \"name\":\"My bean\", \"attr2\":\"val2\", \"attr1\":\"val1\" }";

    static class ExtendableBean {
      public String name;
      private final Map<String, String> properties = new HashMap<>();

      @JsonAnySetter
      public void add(String key, String value) {
        properties.put(key, value);
      }
    }

    @Test
    @DisplayName("whenDeserializingUsingJsonAnySetter_thenCorrect")
    void whenDeserializingUsingJsonAnySetter_thenCorrect() throws JsonProcessingException {
      ExtendableBean bean = new ObjectMapper().readerFor(ExtendableBean.class).readValue(json);

      assertEquals("My bean", bean.name);
      assertEquals("val2", bean.properties.get("attr2"));
    }
  }

  /**
   * {@link JsonSetter} 是 @JsonProperty 的替代方法，将方法标记为 setter 方法。 当我们需要读取一些 JSON
   * 数据但目标实体类与该数据不完全匹配时，这非常有用，因此我们需要调整该过程以使其适合。
   */
  static class JsonSetterTest {
    /** deserialize the following JSON */
    static final String json = "{\"id\":1,\"name\":\"My bean\"}";

    static class MyBean {
      public int id;
      private String name;

      @JsonSetter("name")
      public void setTheName(String name) {
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenDeserializingUsingJsonSetter_thenCorrect")
    void whenDeserializingUsingJsonSetter_thenCorrect() throws JsonProcessingException {
      MyBean bean = new ObjectMapper().readerFor(MyBean.class).readValue(json);

      assertEquals("My bean", bean.name);
    }
  }

  /** {@link JsonDeserialize} 表示使用自定义反序列化器。 */
  static class JsonDeserializeTest {
    /** deserialize the following JSON */
    static final String json = "{\"name\":\"party\",\"eventDate\":\"2014-12-20 02:30:00\"}";

    static class EventWithDeserializer {
      public String name;

      @JsonDeserialize(using = CustomDateDeserializer.class)
      public LocalDateTime eventDate;
    }

    static class CustomDateDeserializer extends StdDeserializer<LocalDateTime> {

      private static final DateTimeFormatter formatter =
          DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

      protected CustomDateDeserializer() {
        super(LocalDateTime.class);
      }

      @Override
      public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JacksonException {
        String text = p.getText();

        TemporalAccessor temporalAccessor = formatter.parse(text);

        return LocalDateTime.of(
            secureGet(temporalAccessor, ChronoField.YEAR),
            secureGet(temporalAccessor, ChronoField.MONTH_OF_YEAR),
            secureGet(temporalAccessor, ChronoField.DAY_OF_MONTH),
            secureGet(temporalAccessor, ChronoField.HOUR_OF_AMPM),
            secureGet(temporalAccessor, ChronoField.MINUTE_OF_HOUR),
            secureGet(temporalAccessor, ChronoField.SECOND_OF_MINUTE),
            secureGet(temporalAccessor, ChronoField.NANO_OF_SECOND));
      }

      /**
       * 安全获取时间的某个属性
       *
       * @param temporalAccessor 需要获取的时间对象
       * @param chronoField 需要获取的属性
       * @return 时间的值，如果无法获取则默认为 0
       */
      private static int secureGet(TemporalAccessor temporalAccessor, ChronoField chronoField) {
        if (temporalAccessor.isSupported(chronoField)) {
          return temporalAccessor.get(chronoField);
        }
        return 0;
      }
    }

    @Test
    @DisplayName("whenDeserializingUsingJsonDeserialize_thenCorrect")
    void whenDeserializingUsingJsonDeserialize_thenCorrect() throws JsonProcessingException {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

      EventWithDeserializer bean =
          new ObjectMapper().readerFor(EventWithDeserializer.class).readValue(json);

      assertEquals("2014-12-20 02:30:00", formatter.format(bean.eventDate));
    }
  }

  /** {@link JsonAlias} 在反序列化期间为属性定义一个或多个替代名称。 */
  static class JsonAliasTest {
    /** deserialize the following JSON */
    static final String json = "{\"fName\": \"John\", \"lastName\": \"Green\"}";

    static class AliasBean {
      @JsonAlias({"fName", "f_name"})
      public String firstName;

      public String lastName;
    }

    @Test
    @DisplayName("whenDeserializingUsingJsonAlias_thenCorrect")
    void whenDeserializingUsingJsonAlias_thenCorrect() throws JsonProcessingException {
      AliasBean bean = new ObjectMapper().readerFor(AliasBean.class).readValue(json);

      assertEquals("John", bean.firstName);
    }
  }
}
