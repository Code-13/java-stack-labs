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
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.code13.javastack.libs.jackson2.annotations.PolymorphicTypeHandlingAnnotations.Zoo.Dog;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * PolymorphicTypeHandlingAnnotations.
 *
 * <p>Jackson Polymorphic Type Handling Annotations
 *
 * <p>每一个静态内部类都只测试了一个注解，顺序如下：
 *
 * @see com.fasterxml.jackson.annotation.JsonTypeInfo
 * @see com.fasterxml.jackson.annotation.JsonSubTypes
 * @see com.fasterxml.jackson.annotation.JsonTypeName
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/1 22:01
 */
public class PolymorphicTypeHandlingAnnotations {

  @Getter
  @Setter
  static class Zoo {

    public Animal animal;

    public Zoo() {}

    public Zoo(Animal animal) {
      this.animal = animal;
    }

    @Getter
    @Setter
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
    @JsonSubTypes({
      @JsonSubTypes.Type(value = Dog.class, name = "dog"),
      @JsonSubTypes.Type(value = Cat.class, name = "cat")
    })
    static class Animal {
      private String name;

      public Animal() {}

      public Animal(String name) {
        this.name = name;
      }
    }

    @Getter
    @Setter
    @JsonTypeName("dog")
    static class Dog extends Animal {
      private double barkVolume;

      public Dog() {}

      public Dog(String name) {
        super(name);
      }
    }

    @Getter
    @Setter
    @JsonTypeName("cat")
    static class Cat extends Animal {
      boolean likesCream;
      public int lives;

      public Cat() {
        super();
      }

      public Cat(String name) {
        super(name);
      }
    }
  }

  @Test
  @DisplayName("whenSerializingPolymorphic_thenCorrect")
  void whenSerializingPolymorphic_thenCorrect() throws JsonProcessingException {

    Dog dog = new Dog("lacy");
    Zoo zoo = new Zoo(dog);

    String result = new ObjectMapper().writeValueAsString(zoo);

    assertThat(result, containsString("type"));
    assertThat(result, containsString("dog"));
  }

  @Test
  @DisplayName("whenDeserializingPolymorphic_thenCorrect")
  void whenDeserializingPolymorphic_thenCorrect() throws JsonProcessingException {
    String json = "{\"animal\":{\"name\":\"lacy\",\"type\":\"cat\"}}";

    Zoo bean = new ObjectMapper().readerFor(Zoo.class).readValue(json);

    assertEquals("lacy", bean.animal.name);
    assertEquals(Zoo.Cat.class, bean.animal.getClass());
  }
}
