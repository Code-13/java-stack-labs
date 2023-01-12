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

package io.github.code13.spring.security.oauth2.authorization.server.extension.password;

import io.github.code13.spring.security.oauth2.authorization.server.extension.AuthorizationGrantTypes;
import io.github.code13.spring.security.oauth2.authorization.server.extension.OAuth2EndpointUtils;
import io.github.code13.spring.security.oauth2.authorization.server.extension.OAuth2ResourceOwnerAuthenticationConverter;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * OAuth2PasswordAuthenticationConverter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 13:37
 */
public class OAuth2PasswordAuthenticationConverter
    extends OAuth2ResourceOwnerAuthenticationConverter<OAuth2PasswordAuthenticationToken> {

  @Override
  protected boolean supportGrantType(String grantType) {
    return AuthorizationGrantTypes.PASSWORD.getValue().equals(grantType);
  }

  @Override
  protected void checkNeededParameters(
      HttpServletRequest request, MultiValueMap<String, String> parameters) {
    // username (REQUIRED)
    String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
    if (!StringUtils.hasText(username)
        || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
      OAuth2EndpointUtils.throwError(
          OAuth2ErrorCodes.INVALID_REQUEST,
          OAuth2ParameterNames.USERNAME,
          OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }

    // password (REQUIRED)
    String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
    if (!StringUtils.hasText(password)
        || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
      OAuth2EndpointUtils.throwError(
          OAuth2ErrorCodes.INVALID_REQUEST,
          OAuth2ParameterNames.PASSWORD,
          OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }
  }

  @Override
  protected OAuth2PasswordAuthenticationToken buildOAuth2AuthenticationToken(
      Authentication clientPrincipal,
      Set<String> requestedScopes,
      Map<String, Object> additionalParameters) {
    return new OAuth2PasswordAuthenticationToken(
        AuthorizationGrantTypes.PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
  }
}
