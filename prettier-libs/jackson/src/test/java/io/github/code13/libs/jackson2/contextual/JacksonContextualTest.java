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

package io.github.code13.libs.jackson2.contextual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JacksonContextualTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/22 16:54
 */
class JacksonContextualTest {

  @Data
  static class User {
    private Long useId;

    private String useNo;

    private String userName;

    @SensitiveInfo(type = SensitiveType.MOBILE_PHONE)
    private String mobile;

    private String sex;

    private int age;

    private String nativePlace;

    @SensitiveInfo(type = SensitiveType.ID_CARD)
    private String idCard;

    private String borrowingLevel;
  }

  @Test
  @DisplayName("testContextualSerialize")
  void testContextualSerialize() throws JsonProcessingException {
    User user = new User();
    user.setUseId(1L);
    user.setUseNo("2022062266668888");
    user.setUserName("张三");
    user.setMobile("19988889999");
    user.setSex("男");
    user.setAge(40);
    user.setNativePlace("热河省承德市");
    user.setIdCard("320321199001018888");
    user.setBorrowingLevel("D");

    ObjectMapper objectMapper = new ObjectMapper();
    String string = objectMapper.writeValueAsString(user);

    // {"useId":1,"useNo":"2022062266668888","userName":"张三","mobile":"19******99","sex":"男","age":40,"nativePlace":"热河省承德市","idCard":"320************888","borrowingLevel":"D"}

    System.out.println(string);

    JsonNode jsonNode = objectMapper.readTree(string);
    String mobile = jsonNode.get("mobile").asText();
    String idCard = jsonNode.get("idCard").asText();

    assertEquals("19******99", mobile);
    assertEquals("320************888", idCard);
  }
}
