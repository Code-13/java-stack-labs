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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * OAuth2PasswordConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 22:13
 */
public class OAuth2PasswordConfigurer
    extends AbstractHttpConfigurer<OAuth2PasswordConfigurer, HttpSecurity> {

  @Override
  public void init(HttpSecurity http) throws Exception {
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .tokenEndpoint(
            token ->
                token.accessTokenRequestConverter(new OAuth2PasswordAuthenticationConverter()));
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    OAuth2AuthorizationService authorizationService =
        http.getSharedObject(OAuth2AuthorizationService.class);
    OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
        http.getSharedObject(OAuth2TokenGenerator.class);

    OAuth2PasswordAuthenticationProvider oAuth2PasswordAuthenticationProvider =
        new OAuth2PasswordAuthenticationProvider(
            authorizationService, tokenGenerator, authenticationManager);

    // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
    http.authenticationProvider(oAuth2PasswordAuthenticationProvider);
  }
}
