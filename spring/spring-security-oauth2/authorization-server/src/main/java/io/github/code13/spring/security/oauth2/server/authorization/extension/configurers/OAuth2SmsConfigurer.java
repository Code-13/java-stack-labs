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

package io.github.code13.spring.security.oauth2.server.authorization.extension.configurers;

import io.github.code13.spring.security.oauth2.server.authorization.extension.sms.OAuth2SmsAuthenticationConverter;
import io.github.code13.spring.security.oauth2.server.authorization.extension.sms.OAuth2SmsAuthenticationProvider;
import io.github.code13.spring.security.sms.SmsAuthenticationProvider;
import io.github.code13.spring.security.sms.SmsService;
import io.github.code13.spring.security.sms.SmsUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * OAuth2SmsConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 22:38
 */
public class OAuth2SmsConfigurer extends AbstractOAuth2Configurer {

  private SmsService smsService;
  private SmsUserDetailsService smsUserDetailsService;

  public OAuth2SmsConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
    super(objectPostProcessor);
  }

  @Override
  public void init(HttpSecurity http) {
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .tokenEndpoint(
            token -> token.accessTokenRequestConverter(new OAuth2SmsAuthenticationConverter()));
  }

  @Override
  public void configure(HttpSecurity http) {
    // ----- OAuth2SmsAuthenticationProvider / OAuth2SmsAuthenticationToken -----
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    OAuth2AuthorizationService authorizationService =
        OAuth2ConfigurerUtils.getAuthorizationService(http);

    OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
        OAuth2ConfigurerUtils.getTokenGenerator(http);

    OAuth2SmsAuthenticationProvider oAuth2PasswordAuthenticationProvider =
        new OAuth2SmsAuthenticationProvider(
            authorizationService, tokenGenerator, authenticationManager);

    http.authenticationProvider(oAuth2PasswordAuthenticationProvider);

    // ----- SmsAuthenticationProvider / SmsAuthenticationToken -----
    if (smsService == null) {
      smsService = OAuth2ConfigurerUtils.getBean(http, SmsService.class);
    }

    if (smsUserDetailsService == null) {
      smsUserDetailsService = OAuth2ConfigurerUtils.getBean(http, SmsUserDetailsService.class);
    }

    http.authenticationProvider(new SmsAuthenticationProvider(smsService, smsUserDetailsService));
  }

  public OAuth2SmsConfigurer smsService(SmsService smsService) {
    this.smsService = smsService;
    return this;
  }

  public OAuth2SmsConfigurer smsUserDetailsService(SmsUserDetailsService smsUserDetailsService) {
    this.smsUserDetailsService = smsUserDetailsService;
    return this;
  }
}
