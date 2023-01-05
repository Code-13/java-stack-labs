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

package io.github.code13.spring.security.oauth2.resource.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.StringUtils;

/**
 * ResourceServerAuthenticationManagerResolver.
 *
 * <p>Supporting both JWT and Opaque Token
 *
 * @see <a
 *     href="https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/multitenancy.html#oauth2reourceserver-opaqueandjwt">Supporting
 *     both JWT and Opaque Token</a>
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/5 17:28
 */
class ResourceServerAuthenticationManagerResolver
    implements AuthenticationManagerResolver<HttpServletRequest> {

  private static final Pattern JWT_PATTERN =
      Pattern.compile("^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-+/=]*)");

  private final BearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();

  private final AuthenticationManager jwt;
  private final AuthenticationManager opaqueToken;
  private final ObjectMapper objectMapper;

  public ResourceServerAuthenticationManagerResolver(
      AuthenticationManager jwt, AuthenticationManager opaqueToken, ObjectMapper objectMapper) {
    this.jwt = jwt;
    this.opaqueToken = opaqueToken;
    this.objectMapper = objectMapper;
  }

  @Override
  public AuthenticationManager resolve(HttpServletRequest context) {
    String token = tokenResolver.resolve(context);
    return isJwt(token) ? jwt : opaqueToken;
  }

  private boolean isJwt(String token) {
    if (!JWT_PATTERN.matcher(token).matches()) {
      return false;
    }

    String[] jwtSplit = token.split("\\.");
    if (jwtSplit.length != 3) {
      return false;
    }

    try {
      String jsonFirstPart = new String(Base64.getDecoder().decode(jwtSplit[0]));
      JsonNode jsonNode = objectMapper.readTree(jsonFirstPart);
      JsonNode algNode = jsonNode.get("alg");
      if (algNode == null || !StringUtils.hasText(algNode.asText())) {
        return false;
      }

      String jsonSecondPart = new String(Base64.getDecoder().decode(jwtSplit[1]));
      objectMapper.readTree(jsonSecondPart);
    } catch (JsonProcessingException e) {
      return false;
    }

    return true;
  }
}
