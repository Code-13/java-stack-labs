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

package io.github.code13.spring.security.oauth2.authorization.server.extension.jackson2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.code13.spring.security.oauth2.authorization.server.extension.sms.SmsAuthenticationToken;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * SmsAuthenticationTokenDeserializer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/3 16:17
 */
final class SmsAuthenticationTokenDeserializer extends JsonDeserializer<SmsAuthenticationToken> {

  private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST =
      new TypeReference<List<GrantedAuthority>>() {};

  private static final TypeReference<Object> OBJECT = new TypeReference<Object>() {};

  /**
   * This method construct {@link UsernamePasswordAuthenticationToken} object from serialized json.
   *
   * @param jp the JsonParser
   * @param ctxt the DeserializationContext
   * @return the user
   * @throws IOException if a exception during IO occurs
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @Override
  public SmsAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    JsonNode jsonNode = mapper.readTree(jp);
    Boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
    JsonNode principalNode = readJsonNode(jsonNode, "principal");
    Object principal = getPrincipal(mapper, principalNode);
    JsonNode credentialsNode = readJsonNode(jsonNode, "credentials");
    Object credentials = getCredentials(credentialsNode);
    List<GrantedAuthority> authorities =
        mapper.readValue(
            readJsonNode(jsonNode, "authorities").traverse(mapper), GRANTED_AUTHORITY_LIST);
    SmsAuthenticationToken token =
        (!authenticated)
            ? SmsAuthenticationToken.unauthenticated(principal, credentials)
            : SmsAuthenticationToken.authenticated(principal, credentials, authorities);
    JsonNode detailsNode = readJsonNode(jsonNode, "details");
    if (detailsNode.isNull() || detailsNode.isMissingNode()) {
      token.setDetails(null);
    } else {
      Object details = mapper.readValue(detailsNode.toString(), OBJECT);
      token.setDetails(details);
    }
    return token;
  }

  private Object getCredentials(JsonNode credentialsNode) {
    if (credentialsNode.isNull() || credentialsNode.isMissingNode()) {
      return null;
    }
    return credentialsNode.asText();
  }

  private Object getPrincipal(ObjectMapper mapper, JsonNode principalNode)
      throws IOException, JsonParseException, JsonMappingException {
    if (principalNode.isObject()) {
      return mapper.readValue(principalNode.traverse(mapper), Object.class);
    }
    return principalNode.asText();
  }

  private JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }
}
