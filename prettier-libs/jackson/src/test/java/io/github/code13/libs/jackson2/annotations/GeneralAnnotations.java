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
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Jackson General Annotations.
 *
 * <p>每一个静态内部类都只测试了一个注解，顺序如下：
 *
 * @see com.fasterxml.jackson.annotation.JsonProperty
 * @see com.fasterxml.jackson.annotation.JsonFormat
 * @see com.fasterxml.jackson.annotation.JsonUnwrapped
 * @see com.fasterxml.jackson.annotation.JsonView
 * @see com.fasterxml.jackson.annotation.JsonManagedReference
 * @see com.fasterxml.jackson.annotation.JsonBackReference
 * @see com.fasterxml.jackson.annotation.JsonIdentityInfo
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/1 22:46
 */
public class GeneralAnnotations {

  /** 我们可以添加 {@link JsonProperty} 注解来表示 JSON 中的属性名称。 */
  static class JsonPropertyTest {

    static class MyBean {
      public int id;
      private String name;

      public MyBean(int id, String name) {
        this.id = id;
        this.name = name;
      }

      @JsonProperty("name")
      public void setTheName(String name) {
        this.name = name;
      }

      @JsonProperty("name")
      public String getTheName() {
        return name;
      }
    }

    @Test
    @DisplayName("whenUsingJsonProperty_thenCorrect")
    void whenUsingJsonProperty_thenCorrect() throws JsonProcessingException {

      MyBean bean = new MyBean(1, "My bean");

      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("My bean"));
      assertThat(result, containsString("1"));

      MyBean resultBean = new ObjectMapper().readerFor(MyBean.class).readValue(result);
      assertEquals("My bean", resultBean.getTheName());
    }
  }

  /** The {@link JsonFormat} annotation specifies a format when serializing Date/Time values. */
  static class JsonFormatTest {

    static class EventWithFormat {
      public String name;

      @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
      public LocalDateTime eventDate;

      public EventWithFormat(String name, LocalDateTime eventDate) {
        this.name = name;
        this.eventDate = eventDate;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonFormat_thenCorrect")
    void whenSerializingUsingJsonFormat_thenCorrect() throws JsonProcessingException {

      LocalDateTime dateTime = LocalDateTime.of(2022, 6, 2, 9, 0, 0);

      EventWithFormat bean = new EventWithFormat("party", dateTime);

      // Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module
      // "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
      String result =
          new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(bean);

      assertThat(result, containsString("2022-06-02 09:00:00"));
    }
  }

  /** {@link JsonUnwrapped} 定义了在序列化反序列化时应该展开扁平化的值。 */
  static class JsonUnwrappedTest {

    static class UnwrappedUser {
      public int id;
      @JsonUnwrapped public Name name;

      public UnwrappedUser(int id, Name name) {
        this.id = id;
        this.name = name;
      }

      static class Name {
        public String firstName;
        public String lastName;

        public Name(String firstName, String lastName) {
          this.firstName = firstName;
          this.lastName = lastName;
        }
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonUnwrapped_thenCorrect")
    void whenSerializingUsingJsonUnwrapped_thenCorrect() throws JsonProcessingException {
      UnwrappedUser.Name name = new UnwrappedUser.Name("John", "Doe");
      UnwrappedUser bean = new UnwrappedUser(1, name);

      String result = new ObjectMapper().writeValueAsString(bean);

      /* { "id":1, "firstName":"John", "lastName":"Doe" } */

      assertThat(result, containsString("John"));
      assertThat(result, not(containsString("name")));
    }
  }

  /** {@link JsonView} 指示将在其中包含属性以进行序列化反序列化的视图。 */
  static class JsonViewTest {

    static class Views {
      public static class Public {}

      public static class Internal extends Public {}
    }

    static class Item {
      @JsonView(Views.Public.class)
      public int id;

      @JsonView(Views.Public.class)
      public String itemName;

      @JsonView(Views.Internal.class)
      public String ownerName;

      public Item(int id, String itemName, String ownerName) {
        this.id = id;
        this.itemName = itemName;
        this.ownerName = ownerName;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonView_thenCorrect")
    void whenSerializingUsingJsonView_thenCorrect() throws JsonProcessingException {
      Item item = new Item(2, "book", "John");

      String result =
          new ObjectMapper().writerWithView(Views.Public.class).writeValueAsString(item);

      // {"id":2,"itemName":"book"}

      assertThat(result, containsString("book"));
      assertThat(result, containsString("2"));
      assertThat(result, not(containsString("John")));
    }
  }

  /** {@link JsonManagedReference} 和 {@link JsonBackReference} 注释可以处理父子关系并绕过循环。 */
  static class JsonManagedReferenceAndJsonBackReferenceTest {

    static class ItemWithRef {
      public int id;
      public String itemName;

      @JsonManagedReference public UserWithRef owner;

      public ItemWithRef(int id, String itemName, UserWithRef owner) {
        this.id = id;
        this.itemName = itemName;
        this.owner = owner;
      }
    }

    static class UserWithRef {
      public int id;
      public String name;

      @JsonBackReference public List<ItemWithRef> userItems;

      public UserWithRef(int id, String name) {
        this.id = id;
        this.name = name;
      }

      public void addItem(ItemWithRef item) {
        if (userItems == null) {
          userItems = new ArrayList<>();
        }
        userItems.add(item);
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJacksonReferenceAnnotation_thenCorrect")
    void whenSerializingUsingJacksonReferenceAnnotation_thenCorrect()
        throws JsonProcessingException {
      UserWithRef user = new UserWithRef(1, "John");
      ItemWithRef item = new ItemWithRef(2, "book", user);
      user.addItem(item);

      String result = new ObjectMapper().writeValueAsString(item);

      // {"id":2,"itemName":"book","owner":{"id":1,"name":"John"}}

      assertThat(result, containsString("book"));
      assertThat(result, containsString("John"));
      assertThat(result, not(containsString("userItems")));
    }
  }

  /** {@link JsonIdentityInfo} 表示在序列化反序列化值时应该使用对象标识，例如在处理无限递归类型的问题时。 */
  static class JsonIdentityInfoTest {

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    static class ItemWithIdentity {
      public int id;
      public String itemName;
      public UserWithIdentity owner;

      public ItemWithIdentity(int id, String itemName, UserWithIdentity owner) {
        this.id = id;
        this.itemName = itemName;
        this.owner = owner;
      }
    }

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    static class UserWithIdentity {
      public int id;
      public String name;
      public List<ItemWithIdentity> userItems;

      public UserWithIdentity(int id, String name) {
        this.id = id;
        this.name = name;
      }

      public void addItem(ItemWithIdentity item) {
        if (userItems == null) {
          userItems = new ArrayList<>();
        }
        userItems.add(item);
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonIdentityInfo_thenCorrect")
    void whenSerializingUsingJsonIdentityInfo_thenCorrect() throws JsonProcessingException {
      UserWithIdentity user = new UserWithIdentity(1, "John");
      ItemWithIdentity item = new ItemWithIdentity(2, "book", user);

      user.addItem(item);

      String result = new ObjectMapper().writeValueAsString(item);

      // {"id":2,"itemName":"book","owner":{"id":1,"name":"John","userItems":[2]}}

      assertThat(result, containsString("book"));
      assertThat(result, containsString("John"));
      assertThat(result, containsString("userItems"));
    }
  }

  /** {@link JsonFilter} 注解指定在序列化期间使用的过滤器。 */
  static class JsonFilterTest {

    @JsonFilter("myFilter")
    static class BeanWithFilter {
      public int id;
      public String name;

      public BeanWithFilter(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonFilter_thenCorrect")
    void whenSerializingUsingJsonFilter_thenCorrect() throws JsonProcessingException {
      BeanWithFilter bean = new BeanWithFilter(1, "My bean");

      FilterProvider filter =
          new SimpleFilterProvider()
              .addFilter("myFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name"));

      String result = new ObjectMapper().writer(filter).writeValueAsString(bean);

      // {"name":"My bean"}

      assertThat(result, containsString("My bean"));
      assertThat(result, not(containsString("id")));
    }
  }
}
