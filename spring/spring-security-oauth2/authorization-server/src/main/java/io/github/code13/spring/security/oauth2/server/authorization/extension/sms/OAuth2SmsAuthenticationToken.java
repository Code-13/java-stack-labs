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

package io.github.code13.spring.security.oauth2.server.authorization.extension.sms;

import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ResourceOwnerAuthenticationToken;
import java.io.Serial;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * OAuth2SmsAuthenticationToken.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 18:15
 */
public class OAuth2SmsAuthenticationToken extends OAuth2ResourceOwnerAuthenticationToken {

  @Serial private static final long serialVersionUID = 3517168880376549032L;

  public OAuth2SmsAuthenticationToken(
      AuthorizationGrantType authorizationGrantType,
      Authentication clientPrincipal,
      Set<String> scopes,
      Map<String, Object> additionalParameters) {
    super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
  }
}
