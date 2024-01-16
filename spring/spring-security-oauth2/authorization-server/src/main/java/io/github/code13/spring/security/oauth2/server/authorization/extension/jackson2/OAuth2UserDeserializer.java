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

package io.github.code13.spring.security.oauth2.server.authorization.extension.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.code13.spring.security.oauth2.server.authorization.user.OAuth2User;
import java.io.IOException;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * OAuth2UserDeserializer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/1/15 17:45
 */
class OAuth2UserDeserializer extends JsonDeserializer<OAuth2User> {

  private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET =
      new TypeReference<Set<SimpleGrantedAuthority>>() {};

  @Override
  public OAuth2User deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    JsonNode jsonNode = mapper.readTree(jp);
    Set<? extends GrantedAuthority> authorities =
        mapper.convertValue(jsonNode.get("authorities"), SIMPLE_GRANTED_AUTHORITY_SET);
    String id = readJsonNode(jsonNode, "id").asText();
    JsonNode passwordNode = readJsonNode(jsonNode, "password");
    String username = readJsonNode(jsonNode, "username").asText();
    String phone = readJsonNode(jsonNode, "phone").asText();
    String password = passwordNode.asText("");
    boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
    boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
    boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
    boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
    OAuth2User result =
        OAuth2User.newBuilder()
            .withId(id)
            .withUsername(username)
            .withPassword(password)
            .withPhone(phone)
            .withAccountNonExpired(accountNonExpired)
            .withAccountNonLocked(accountNonLocked)
            .withCredentialsNonExpired(credentialsNonExpired)
            .withEnabled(enabled)
            .withAuthorities(authorities)
            .build();

    if (passwordNode.asText(null) == null) {
      result.eraseCredentials();
    }
    return result;
  }

  private JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }
}
