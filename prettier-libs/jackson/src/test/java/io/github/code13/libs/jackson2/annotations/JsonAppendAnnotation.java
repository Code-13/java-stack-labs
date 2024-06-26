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
import static org.hamcrest.Matchers.containsString;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link JsonAppend} Annotation.
 *
 * <p>在序列化阶段，使用 JsonAppend 添加虚拟字段
 *
 * @see com.fasterxml.jackson.databind.annotation.JsonAppend
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/6/25 09:53
 */
public class JsonAppendAnnotation {

  /** 测试 JsonAppend attrs 属性 */
  @Nested
  class JsonAppendAttrsTest {

    @JsonAppend(attrs = {@JsonAppend.Attr(value = "age"), @JsonAppend.Attr(value = "height")})
    static class TestBean {
      private String id;

      public String getId() {
        return id;
      }
    }

    @Test
    @DisplayName("whenUsingJsonAppendAttrs_thenCorrect")
    void whenUsingJsonAppendAttrs_thenCorrect() throws JsonProcessingException {
      TestBean testBean = new TestBean();
      testBean.id = "1";

      String value =
          new ObjectMapper()
              .writerFor(TestBean.class)
              .withAttribute("age", "11")
              .withAttribute("height", "22")
              .writeValueAsString(testBean);

      System.out.println(value);

      assertThat(value, containsString("age"));
      assertThat(value, containsString("height"));
    }
  }

  @Nested
  class JsonAppendProsTest {

    @JsonAppend(
        props = {
          @Prop(name = "age", value = AgePropertyWriter.class, type = String.class),
        })
    static class TestBean {
      private String id;

      public String getId() {
        return id;
      }
    }

    static class AgePropertyWriter extends VirtualBeanPropertyWriter {

      public AgePropertyWriter() {}

      public AgePropertyWriter(
          BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
        super(propDef, contextAnnotations, declaredType);
      }

      @Override
      protected Object value(Object bean, JsonGenerator gen, SerializerProvider prov)
          throws Exception {
        return "22";
      }

      @Override
      public VirtualBeanPropertyWriter withConfig(
          MapperConfig<?> config,
          AnnotatedClass declaringClass,
          BeanPropertyDefinition propDef,
          JavaType type) {
        return new AgePropertyWriter(propDef, declaringClass.getAnnotations(), type);
      }
    }

    @Test
    @DisplayName("whenUsingJsonAppendProps_thenCorrect")
    void whenUsingJsonAppendProps_thenCorrect() throws JsonProcessingException {
      TestBean testBean = new TestBean();
      testBean.id = "1";

      String value = new ObjectMapper().writeValueAsString(testBean);
      System.out.println(value);

      assertThat(value, containsString("age"));
    }
  }
}
