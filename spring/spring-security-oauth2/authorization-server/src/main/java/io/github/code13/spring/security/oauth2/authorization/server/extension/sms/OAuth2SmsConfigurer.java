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

package io.github.code13.spring.security.oauth2.authorization.server.extension.sms;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

/**
 * OAuth2SmsConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 22:38
 */
public class OAuth2SmsConfigurer extends AbstractHttpConfigurer<OAuth2SmsConfigurer, HttpSecurity> {

  private SmsService smsService;
  private SmsUserDetailsService smsUserDetailsService;

  @Override
  public void init(HttpSecurity http) throws Exception {
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .tokenEndpoint(
            token -> token.accessTokenRequestConverter(new OAuth2SmsAuthenticationConverter()));
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // ----- OAuth2SmsAuthenticationProvider / OAuth2SmsAuthenticationToken -----
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    OAuth2AuthorizationService authorizationService =
        http.getSharedObject(OAuth2AuthorizationService.class);
    OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
        http.getSharedObject(OAuth2TokenGenerator.class);

    OAuth2SmsAuthenticationProvider oAuth2PasswordAuthenticationProvider =
        new OAuth2SmsAuthenticationProvider(
            authorizationService, tokenGenerator, authenticationManager);

    http.authenticationProvider(oAuth2PasswordAuthenticationProvider);

    // ----- SmsAuthenticationProvider / SmsAuthenticationToken -----
    ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
    if (smsService == null) {
      smsService = getBeanOrNull(applicationContext, SmsService.class);
    }
    Assert.notNull(smsService, "SmsService must be configurer");

    if (smsUserDetailsService == null) {
      smsUserDetailsService = getBeanOrNull(applicationContext, SmsUserDetailsService.class);
    }
    Assert.notNull(smsUserDetailsService, "SmsUserDetailsService must be configurer");

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

  protected static <T> T getBeanOrNull(ApplicationContext applicationContext, Class<T> type) {
    try {
      return applicationContext.getBean(type);
    } catch (NoSuchBeanDefinitionException notFound) {
      return null;
    }
  }
}
