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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JsonNodeAndJsonNodeFactoryTest.
 *
 * @see com.fasterxml.jackson.core.TreeNode
 * @see com.fasterxml.jackson.databind.node.JsonNodeFactory
 * @see com.fasterxml.jackson.databind.JsonNode
 * @see com.fasterxml.jackson.databind.node.BaseJsonNode
 * @see com.fasterxml.jackson.databind.node.ContainerNode
 * @see com.fasterxml.jackson.databind.node.ArrayNode
 * @see com.fasterxml.jackson.databind.node.ObjectNode
 * @see com.fasterxml.jackson.databind.node.ValueNode
 * @see com.fasterxml.jackson.databind.node.NumericNode
 * @see com.fasterxml.jackson.databind.node.DoubleNode
 * @see com.fasterxml.jackson.databind.node.IntNode
 * @see com.fasterxml.jackson.databind.node.POJONode
 * @see com.fasterxml.jackson.databind.node.BooleanNode
 * @see com.fasterxml.jackson.databind.node.NullNode
 * @see com.fasterxml.jackson.databind.node.TextNode
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/6/26 22:44
 */
public class JsonNodeAndJsonNodeFactoryTest {

  record Person(String name, Integer age) {}

  @Test
  @DisplayName("whenCreateValueNodeUsingJsonNodeFactory_thenCorrect")
  void whenCreateValueNodeUsingJsonNodeFactory_thenCorrect() {
    JsonNodeFactory factory = JsonNodeFactory.instance;

    System.out.println("------ValueNode值节点示例------");
    // 数字节点
    JsonNode node = factory.numberNode(1);
    System.out.println(node.isNumber() + ":" + node.intValue());

    // null节点
    node = factory.nullNode();
    System.out.println(node.isNull() + ":" + node.asText());

    // missing节点
    node = factory.missingNode();
    System.out.println(node.isMissingNode() + "_" + node.asText());

    // POJONode节点
    node = factory.pojoNode(new Person("China", 70));
    System.out.println(node.isPojo() + ":" + node.asText());

    System.out.println("---" + node.isValueNode() + "---");
  }

  @Test
  @DisplayName("whenCreateContainerNodeUsingJsonNodeFactory_thenCorrect")
  void whenCreateContainerNodeUsingJsonNodeFactory_thenCorrect() {
    JsonNodeFactory factory = JsonNodeFactory.instance;

    System.out.println("------构建一个JSON结构数据------");
    ObjectNode rootNode = factory.objectNode();

    // 添加普通值节点
    rootNode.put("zhName", "中国"); // 效果完全同：rootNode.set("zhName", factory.textNode("中国"))
    rootNode.put("enName", "PRC");
    rootNode.put("age", 70);

    // 添加数组容器节点
    ArrayNode arrayNode = factory.arrayNode();
    arrayNode.add("java").add("javascript").add("python");
    rootNode.set("languages", arrayNode);

    // 添加对象节点
    ObjectNode dogNode = factory.objectNode();
    dogNode.put("name", "大黄").put("age", 3);
    rootNode.set("dog", dogNode);

    System.out.println(rootNode);
    System.out.println(rootNode.get("dog").get("name"));
  }
}
