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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.code13.libs.jackson2.annotations.PropertyInclusionAnnotations.JsonIgnoreTypeTest.User.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * PropertyInclusionAnnotations.
 *
 * <p>Jackson Property Inclusion Annotations.
 *
 * <p>每一个静态内部类都只测试了一个注解，顺序如下：
 *
 * @see com.fasterxml.jackson.annotation.JsonIgnoreProperties
 * @see com.fasterxml.jackson.annotation.JsonIgnore
 * @see com.fasterxml.jackson.annotation.JsonIgnoreType
 * @see com.fasterxml.jackson.annotation.JsonInclude
 * @see com.fasterxml.jackson.annotation.JsonAutoDetect
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/1 19:56
 */
public class PropertyInclusionAnnotations {

  /** {@link JsonIgnoreProperties} 是一个类级别的注释，用于标记 Jackson 将忽略的属性或属性列表。 */
  @Nested
  class JsonIgnorePropertiesTest {

    @JsonIgnoreProperties({"id"})
    static class BeanWithIgnore {
      public int id;
      public String name;

      public BeanWithIgnore(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonIgnoreProperties_thenCorrect")
    void whenSerializingUsingJsonIgnoreProperties_thenCorrect() throws JsonProcessingException {
      BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");
      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("My bean"));
      assertThat(result, not(containsString("id")));
    }
  }

  /** {@link JsonIgnore} 注解用于标记要在字段级别忽略的属性。 */
  @Nested
  class JsonIgnoreTest {

    static class BeanWithIgnore {
      @JsonIgnore public int id;

      public String name;

      public BeanWithIgnore(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonIgnore_thenCorrect")
    void whenSerializingUsingJsonIgnore_thenCorrect() throws JsonProcessingException {
      BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");
      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("My bean"));
      assertThat(result, not(containsString("id")));
    }
  }

  /** {@link JsonIgnoreType} 标记要忽略的带注释类型的所有属性。 */
  @Nested
  class JsonIgnoreTypeTest {

    static class User {
      public int id;
      public Name name;

      public User(int id, Name name) {
        this.id = id;
        this.name = name;
      }

      @JsonIgnoreType
      public static class Name {
        public String firstName;
        public String lastName;

        public Name(String firstName, String lastName) {
          this.firstName = firstName;
          this.lastName = lastName;
        }
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonIgnoreType_thenCorrect")
    void whenSerializingUsingJsonIgnoreType_thenCorrect() throws JsonProcessingException {
      Name name = new Name("John", "Doe");
      User user = new User(1, name);

      String bean = new ObjectMapper().writeValueAsString(user);

      assertThat(bean, containsString("1"));
      assertThat(bean, not(containsString("name")));
      assertThat(bean, not(containsString("John")));
    }
  }

  /** We can use {@link JsonInclude} to exclude properties with empty/null/default values. */
  @Nested
  class JsonIncludeTest {

    @JsonInclude(Include.NON_NULL)
    static class MyBean {
      public int id;
      public String name;

      public MyBean(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonInclude_thenCorrect")
    void whenSerializingUsingJsonInclude_thenCorrect() throws JsonProcessingException {
      MyBean bean = new MyBean(1, null);

      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("1"));
      assertThat(result, not(containsString("name")));
    }
  }

  /** {@link JsonAutoDetect} 可以覆盖哪些属性可见哪些不可见的默认语义。 */
  @Nested
  class JsonAutoDetectTest {

    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    static class PrivateBean {
      private int id;
      private String name;

      public PrivateBean(int id, String name) {
        this.id = id;
        this.name = name;
      }
    }

    @Test
    @DisplayName("whenSerializingUsingJsonAutoDetect_thenCorrect")
    void whenSerializingUsingJsonAutoDetect_thenCorrect() throws JsonProcessingException {
      PrivateBean bean = new PrivateBean(1, "My bean");

      String result = new ObjectMapper().writeValueAsString(bean);

      assertThat(result, containsString("1"));
      assertThat(result, containsString("My bean"));
    }
  }
}
