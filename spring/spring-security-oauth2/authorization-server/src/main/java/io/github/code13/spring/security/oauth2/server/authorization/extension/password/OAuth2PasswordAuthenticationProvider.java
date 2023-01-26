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

package io.github.code13.spring.security.oauth2.server.authorization.extension.password;

import io.github.code13.spring.security.oauth2.server.authorization.extension.AuthorizationGrantTypes;
import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ErrorCodesExtension;
import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ResourceOwnerAuthenticationProvider;
import java.util.Map;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * OAuth2PasswordAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 14:15
 */
public class OAuth2PasswordAuthenticationProvider
    extends OAuth2ResourceOwnerAuthenticationProvider<OAuth2PasswordAuthenticationToken> {

  public OAuth2PasswordAuthenticationProvider(
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      AuthenticationManager authenticationManager) {
    super(authorizationService, tokenGenerator, authenticationManager);
  }

  @Override
  protected AuthorizationGrantType getSupportGrantType() {
    return AuthorizationGrantTypes.PASSWORD;
  }

  @Override
  protected Set<String> getAuthorizedScopes(
      OAuth2PasswordAuthenticationToken token, RegisteredClient registeredClient) {
    Set<String> authorizedScopes = super.getAuthorizedScopes(token, registeredClient);
    if (authorizedScopes.isEmpty()) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodesExtension.SCOPE_IS_EMPTY);
    }
    return authorizedScopes;
  }

  @Override
  protected Authentication buildAuthenticationToken(OAuth2PasswordAuthenticationToken token) {
    Map<String, Object> additionalParameters = token.getAdditionalParameters();
    String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
    String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
    return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
