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

import io.github.code13.spring.security.oauth2.server.authorization.extension.AuthorizationGrantTypes;
import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ParameterNamesExtension;
import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ResourceOwnerAuthenticationProvider;
import io.github.code13.spring.security.sms.SmsAuthenticationToken;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * OAuth2SmsAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 18:22
 */
public class OAuth2SmsAuthenticationProvider
    extends OAuth2ResourceOwnerAuthenticationProvider<OAuth2SmsAuthenticationToken> {

  public OAuth2SmsAuthenticationProvider(
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      AuthenticationManager authenticationManager) {
    super(authorizationService, tokenGenerator, authenticationManager);
  }

  @Override
  protected AuthorizationGrantType getSupportGrantType() {
    return AuthorizationGrantTypes.SMS;
  }

  @Override
  protected Authentication buildAuthenticationToken(OAuth2SmsAuthenticationToken token) {
    Map<String, Object> additionalParameters = token.getAdditionalParameters();
    String phone =
        (String) additionalParameters.get(OAuth2ParameterNamesExtension.SMS_PHONE_PARAMETER_NAME);
    String code =
        (String) additionalParameters.get(OAuth2ParameterNamesExtension.SMS_CODE_PARAMETER_NAME);
    return SmsAuthenticationToken.unauthenticated(phone, code);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2SmsAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
