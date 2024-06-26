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

package io.github.code13.libs.jackson2.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * TreeModelTest.
 *
 * @see com.fasterxml.jackson.databind.ObjectMapper#valueToTree(Object)
 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(String)
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/6/25 17:48
 */
public class TreeModelTest {

  record Dog(String name, Integer age) {}

  record Person(String name, Integer age, Dog dog) {

    public Person(String name, Integer age) {
      this(name, age, null);
    }
  }

  /** 序列化 */
  @Nested
  class WriteTree {

    /**
     * 该方法属相对较为常用：将任意对象（包括null）写为一个JsonNode树模型。
     *
     * @see ObjectMapper#valueToTree(Object)
     */
    @Test
    @DisplayName("valueToTree")
    void valueToTree() {
      ObjectMapper objectMapper = new ObjectMapper();

      Dog dog = new Dog("小黄", 3);
      Person person = new Person("张三", 18, dog);

      JsonNode jsonNode = objectMapper.valueToTree(person);

      for (JsonNode node : jsonNode) {
        if (node.isContainerNode()) {
          if (node.isObject()) {
            System.out.println("狗的属性：：：");

            System.out.println(node.get("name"));
            System.out.println(node.get("age"));
          }
        } else {
          System.out.println(node.asText());
        }
      }

      // 直接获取
      System.out.println("---------------------------------------");
      System.out.println(jsonNode.get("dog").get("name"));
      System.out.println(jsonNode.get("dog").get("age"));
    }

    /**
     * 将一个JsonNode使用JsonGenerator写到输出流里，此方法直接使用到了JsonGenerator这个API
     *
     * @see ObjectMapper#writeTree(JsonGenerator, JsonNode)
     * @see ObjectMapper#writeTree(JsonGenerator, com.fasterxml.jackson.core.TreeNode)
     */
    @Test
    @DisplayName("writeTree")
    void writeTree() throws IOException {
      ObjectMapper mapper = new ObjectMapper();

      try (JsonGenerator jsonGenerator = mapper.createGenerator(System.err, JsonEncoding.UTF8)) {

        // 得到一个jsonNode
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        ObjectNode objectNode = jsonNodeFactory.objectNode();
        objectNode.put("name", "张三");
        objectNode.put("age", "18");

        // 使用JsonGenerator写到输出流
        mapper.writeTree(jsonGenerator, objectNode);
      }
    }
  }

  /** 反序列化 */
  @Nested
  class ReadTree {

    /**
     * 将一个资源（如字符串）读取为一个JsonNode树模型。
     *
     * <p>这是典型的方法重载设计，API更加友好，所有方法底层均为_readTreeAndClose()这个protected方法，可谓“万剑归宗”。
     *
     * @see ObjectMapper#readTree(String)
     * @see ObjectMapper#readTree(Reader)
     * @see ObjectMapper#readTree(File)
     * @see ObjectMapper#readTree(URL)
     * @see ObjectMapper#readTree(JsonParser)
     * @see ObjectMapper#readTree(byte[])
     * @see ObjectMapper#readTree(InputStream)
     * @see ObjectMapper#readTree(byte[], int, int)
     * @see ObjectMapper#_readTreeAndClose(JsonParser)
     */
    @Test
    @DisplayName("readTree")
    void readTree() throws JsonProcessingException {

      ObjectMapper mapper = new ObjectMapper();

      String jsonStr =
          """
          {"name":"张三","age":18,"dog":null}""";
      // 直接映射为一个实体对象
      mapper.readValue(jsonStr, Person.class);
      // 读取为一个树模型
      JsonNode node = mapper.readTree(jsonStr);
    }
  }

  @Nested
  class AddProp {

    @Test
    @DisplayName("whenAddPropsUsingTree_thenCorrect")
    void whenAddPropsUsingTree_thenCorrect() throws IOException {
      String jsonStr =
          """
          {"name":"Car","age":18}""";

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jsonStr);

      // add name1 property
      ((ObjectNode) jsonNode).put("name1", "Car");

      // add aaa property
      jsonNode.withObject("aaa").put("name2", "Car");

      // write
      try (SegmentedStringWriter stringWriter =
          new SegmentedStringWriter(objectMapper.getFactory()._getBufferRecycler())) {
        JsonGenerator generator = objectMapper.createGenerator(stringWriter);
        objectMapper.writeTree(generator, jsonNode);

        String result = stringWriter.getAndClear();

        System.out.println(result);

        assertThat(result, containsString("name1"));
        assertThat(result, containsString("aaa"));
        assertThat(result, containsString("name2"));
      }
    }
  }
}
