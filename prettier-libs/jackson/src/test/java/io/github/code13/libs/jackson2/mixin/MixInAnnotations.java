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

package io.github.code13.libs.jackson2.mixin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import io.github.code13.libs.jackson2.mixin.MixInAnnotations.Overriding_Custom_Serializers_and_Deserializers.Person.PersonDeserializer;
import io.github.code13.libs.jackson2.mixin.MixInAnnotations.Overriding_Custom_Serializers_and_Deserializers.Person.PersonSerializer;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * MixInAnnotations.
 *
 * <p>Jackson MixIn Annotations.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/2 10:35
 */
public class MixInAnnotations {

  /*
   * Jackson mixins is a mechanism to add Jackson annotations to classes without modifying the actual class.
   * It was created for those cases where we can’t modify a class such as when working with third-party classes.
   */

  /*
   * We can use any Jackson annotation but we don’t add them directly to the class.
   * We use them in a mixin class instead that can be either an abstract class or an interface.
   * They can be used for both Jackson serialization and deserialization and they have to be added to the ObjectMapper configuration.
   */

  /**
   * when we need to use third-party classes that can’t be serialized or deserialized by Jackson
   * because they aren’t following the Jackson conventions. Since we can’t modify these classes we
   * have to use mixins to add all the necessary pieces that Jackson needs for serialization
   * purposes.
   */
  @Nested
  class Making_a_ThirdParty_Class_Jackson_Serializable_And_DerSerializable {

    /**
     * We can’t serialize this class with Jackson because the properties are private and there
     * aren’t getters and setters. Hence, Jackson won’t recognize any property and will throw an
     * exception :
     */
    static class Person {

      private final String firstName;
      private final String lastName;

      public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
      }
    }

    @Test
    @DisplayName("whenSerializing_thenThrow_InvalidDefinitionException")
    void whenSerializing_thenThrow_InvalidDefinitionException() {
      Person person = new Person("foo", "bar");

      assertThrowsExactly(
          InvalidDefinitionException.class,
          () -> {
            new ObjectMapper().writeValueAsString(person);
          });
    }

    abstract static class PersonMixin {
      @JsonProperty private String firstName;
      @JsonProperty private String lastName;

      @JsonCreator
      public PersonMixin(
          @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName) {}
    }

    @Test
    @DisplayName("whenSerializingUsingMixInAnnotationOnJsonProperty_thenCorrect")
    void whenSerializingUsingMixInAnnotationOnJsonProperty_thenCorrect()
        throws JsonProcessingException {
      String value =
          new ObjectMapper()
              .addMixIn(Person.class, PersonMixin.class)
              .writeValueAsString(new Person("foo", "bar"));

      System.out.println(value);

      assertThat(value, containsString("foo"));
    }

    @Test
    @DisplayName("whenDerSerializingUsingMixInOnJsonCreator_thenCorrect")
    void whenDerSerializingUsingMixInOnJsonCreator_thenCorrect() throws JsonProcessingException {

      String json =
          """
          { "firstName": "foo", "lastName": "bar" }
          """;

      assertThrowsExactly(
          InvalidDefinitionException.class,
          () -> {
            new ObjectMapper().readValue(json, Person.class);
          });

      Person person =
          new ObjectMapper()
              .addMixIn(Person.class, PersonMixin.class)
              .readValue(json, Person.class);

      assertEquals("foo", person.firstName);
    }
  }

  /**
   * There are other scenarios where we find classes that are using custom serializers and
   * deserializers but we want to override them. And of course, we can’t or we don’t want to modify
   * these classes.
   */
  @Nested
  class Overriding_Custom_Serializers_and_Deserializers {

    @JsonSerialize(using = PersonSerializer.class)
    @JsonDeserialize(using = PersonDeserializer.class)
    public static class Person {

      private final String firstName;
      private final String lastName;

      public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
      }

      public String getFirstName() {
        return firstName;
      }

      public String getLastName() {
        return lastName;
      }

      public static class PersonSerializer extends JsonSerializer<Person> {

        static final String SEPARATOR = " ";

        @Override
        public void serialize(
            Person person, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
          jsonGenerator.writeString(person.getFirstName() + SEPARATOR + person.getLastName());
        }
      }

      public static class PersonDeserializer extends JsonDeserializer<Person> {

        @Override
        public Person deserialize(
            JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
          String[] fields = jsonParser.getValueAsString().split(PersonSerializer.SEPARATOR);
          return new Person(fields[0], fields[1]);
        }
      }
    }

    /*
     * Let’s create a different serializer and deserializer for our class:
     */

    public static class PersonReversedSerializer extends JsonSerializer<Person> {

      static final String SEPARATOR = ", ";

      @Override
      public void serialize(
          Person person, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
          throws IOException {
        jsonGenerator.writeString(person.getLastName() + SEPARATOR + person.getFirstName());
      }
    }

    public static class PersonReversedDeserializer extends JsonDeserializer<Person> {

      @Override
      public Person deserialize(
          JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String[] fields = jsonParser.getValueAsString().split(PersonReversedSerializer.SEPARATOR);
        return new Person(fields[1], fields[0]);
      }
    }

    /*
     * To use this new serializer and deserializer without modifying Person we need to specify it in our mixin class:
     *
     * Now, Jackson will use these serializers and will ignore the ones specified in Person.
     *
     * It would be the same if we wanted to do this just for a specific property.
     */
    @JsonSerialize(using = PersonReversedSerializer.class)
    @JsonDeserialize(using = PersonReversedDeserializer.class)
    abstract static class PersonMixin {}

    @Test
    @DisplayName("whenSerOrDerUsingMixinOnOverridingCustomSersAndDesers")
    void whenSerOrDerUsingMixinOnOverridingCustomSersAndDesers() throws JsonProcessingException {
      // does not use mix
      String value = new ObjectMapper().writeValueAsString(new Person("firstName", "lastName"));
      System.out.println(value);
      assertEquals(value, "\"firstName lastName\"");

      String v = "\"firstName lastName\"";
      assertDoesNotThrow(
          () -> {
            Person person = new ObjectMapper().readValue(v, Person.class);
          });

      // using mix

      value =
          new ObjectMapper()
              .addMixIn(Person.class, PersonMixin.class)
              .writeValueAsString(new Person("firstName", "lastName"));
      System.out.println(value);
      assertEquals(value, "\"lastName, firstName\"");

      String v1 = "\"lastName, firstName\"";
      assertDoesNotThrow(
          () -> {
            new ObjectMapper()
                .addMixIn(Person.class, PersonMixin.class)
                .readValue(v1, Person.class);
          });
    }
  }

  @Nested
  class IgnoreType {
    static class Item {
      public int id;
      public String itemName;
      public User owner;

      public Item(int id, String itemName, User owner) {
        this.id = id;
        this.itemName = itemName;
        this.owner = owner;
      }
    }

    static class User {
      public int id;

      public User(int id) {
        this.id = id;
      }
    }

    @JsonIgnoreType
    static class MyMixInForIgnoreType {}

    @Test
    @DisplayName("@JsonIgnoreType")
    void whenSerializingUsingMixInAnnotationOnIgnoreType_thenCorrect()
        throws JsonProcessingException {
      Item item = new Item(1, "My bean", null);

      String result = new ObjectMapper().writeValueAsString(item);
      assertThat(result, containsString("owner"));

      result =
          new ObjectMapper()
              .addMixIn(User.class, MyMixInForIgnoreType.class)
              .writeValueAsString(item);

      assertThat(result, not(containsString("owner")));
    }
  }
}
