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

package io.github.code13.javastack.libs.jackson2.skipfields;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.io.IOException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SkipObjectsConditionally.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/8 17:24
 */
@SuppressWarnings("unchecked")
public class SkipObjectsConditionally {

  @JsonIgnoreProperties("hidden")
  public interface Hidable {
    boolean isHidden();
  }

  @AllArgsConstructor
  @Getter
  @Setter
  static class Person implements Hidable {
    private String name;
    private Address address;
    private boolean hidden;
  }

  @AllArgsConstructor
  @Getter
  @Setter
  static class Address implements Hidable {
    private String city;
    private String country;
    private boolean hidden;
  }

  static class HidableSerializer extends JsonSerializer<Hidable> {

    private final JsonSerializer<Object> defaultSerializer;

    public HidableSerializer(JsonSerializer<Object> defaultSerializer) {
      this.defaultSerializer = defaultSerializer;
    }

    @Override
    public void serialize(Hidable value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      if (value.isHidden()) {
        return;
      }

      // When the object will not be skipped, we delegate the serialization to the default injected
      // serializer.
      defaultSerializer.serialize(value, gen, serializers);
    }

    /**
     * We overridden the method isEmpty() – to make sure that in case of Hidable object is a
     * property, property name is also excluded from JSON.
     */
    @Override
    public boolean isEmpty(SerializerProvider provider, Hidable value) {
      return value == null || value.isHidden();
    }
  }

  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setup() {
    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
    objectMapper.registerModule(
        new SimpleModule() {
          @Override
          public void setupModule(SetupContext context) {
            super.setupModule(context);
            context.addBeanSerializerModifier(
                new BeanSerializerModifier() {
                  @Override
                  public JsonSerializer<?> modifySerializer(
                      SerializationConfig config,
                      BeanDescription beanDesc,
                      JsonSerializer<?> serializer) {

                    if (Hidable.class.isAssignableFrom(beanDesc.getBeanClass())) {
                      return new HidableSerializer((JsonSerializer<Object>) serializer);
                    }
                    return serializer;
                  }
                });
          }
        });
  }

  @Test
  @DisplayName("whenNotHidden_thenCorrect")
  void whenNotHidden_thenCorrect() throws JsonProcessingException {
    Address ad = new Address("ny", "usa", false);
    Person person = new Person("john", ad, false);

    String result = objectMapper.writeValueAsString(person);

    // {"name":"john","address":{"city":"ny","country":"usa"}}

    assertTrue(result.contains("name"));
    assertTrue(result.contains("john"));
    assertTrue(result.contains("address"));
    assertTrue(result.contains("usa"));
  }

  @Test
  @DisplayName("whenAddressHidden_thenCorrect")
  void whenAddressHidden_thenCorrect() throws JsonProcessingException {
    Address ad = new Address("ny", "usa", true);
    Person person = new Person("john", ad, false);

    String result = objectMapper.writeValueAsString(person);

    // {"name":"john"}

    assertTrue(result.contains("name"));
    assertTrue(result.contains("john"));
    assertFalse(result.contains("address"));
    assertFalse(result.contains("usa"));
  }

  @Test
  @DisplayName("whenAllHidden_thenCorrect")
  void whenAllHidden_thenCorrect() throws JsonProcessingException {
    Address ad = new Address("ny", "usa", false);
    Person person = new Person("john", ad, true);
    String result = objectMapper.writeValueAsString(person);

    assertEquals(0, result.length());
  }

  @Test
  @DisplayName("whenSerializeList_thenCorrect")
  void whenSerializeList_thenCorrect() throws JsonProcessingException {
    Address ad1 = new Address("tokyo", "jp", true);
    Address ad2 = new Address("london", "uk", false);
    Address ad3 = new Address("ny", "usa", false);
    Person p1 = new Person("john", ad1, false);
    Person p2 = new Person("tom", ad2, true);
    Person p3 = new Person("adam", ad3, false);

    String result = objectMapper.writeValueAsString(Arrays.asList(p1, p2, p3));

    // [{"name":"john"},{"name":"adam","address":{"city":"ny","country":"usa"}}]

    System.out.println(result);
  }
}
