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

package io.github.code13.spring.security.oauth2.server.authorization.extension.token.pkce.introspection;

import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2EndpointUtils;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * PkceTokenIntrospectionClientAuthenticationConverter.
 *
 * @see
 *     org.springframework.security.oauth2.server.authorization.web.authentication.PublicClientAuthenticationConverter
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/10 21:49
 */
public final class PkceTokenIntrospectionClientAuthenticationConverter
    implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {

    MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

    // client_id (REQUIRED for public clients)
    String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
    if (!StringUtils.hasText(clientId)
        || parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
    }

    parameters.remove(OAuth2ParameterNames.CLIENT_ID);

    return new OAuth2ClientAuthenticationToken(
        clientId,
        ClientAuthenticationMethod.NONE,
        null,
        new HashMap<>(parameters.toSingleValueMap()));
  }
}
