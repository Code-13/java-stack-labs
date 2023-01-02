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

package io.github.code13.spring.security.oauth2.authorization.server.extension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * OAuth2ResourceOwnerAuthenticationConverter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 21:04
 */
public abstract class OAuth2ResourceOwnerAuthenticationConverter<
        T extends OAuth2ResourceOwnerAuthenticationToken>
    implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    // grant_type (REQUIRED)
    String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
    if (!supportGrantType(grantType)) {
      return null;
    }

    MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

    // scope (OPTIONAL)
    String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
    if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
      OAuth2EndpointUtils.throwError(
          OAuth2ErrorCodes.INVALID_REQUEST,
          OAuth2ParameterNames.SCOPE,
          OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }
    Set<String> requestedScopes = null;
    if (StringUtils.hasText(scope)) {
      requestedScopes =
          new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
    }

    // client
    Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
    if (clientPrincipal == null) {
      OAuth2EndpointUtils.throwError(
          OAuth2ErrorCodes.INVALID_REQUEST,
          OAuth2ErrorCodes.INVALID_CLIENT,
          OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
    }

    checkNeededParameters(request, parameters);

    // additionalParameters
    Map<String, Object> additionalParameters = new HashMap<>();
    parameters.forEach(
        (key, value) -> {
          if (!key.equals(OAuth2ParameterNames.GRANT_TYPE)
              && !key.equals(OAuth2ParameterNames.SCOPE)) {
            additionalParameters.put(key, value.get(0));
          }
        });

    return buildOAuth2AuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
  }

  protected abstract boolean supportGrantType(String grantType);

  protected void checkNeededParameters(
      HttpServletRequest request, MultiValueMap<String, String> parameters) {}

  protected abstract T buildOAuth2AuthenticationToken(
      Authentication clientPrincipal,
      Set<String> requestedScopes,
      Map<String, Object> additionalParameters);
}
