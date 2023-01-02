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

package io.github.code13.spring.security.oauth2.authorization.server.extension.configurer;

import io.github.code13.spring.security.oauth2.authorization.server.extension.password.OAuth2PasswordConfigurer;
import io.github.code13.spring.security.oauth2.authorization.server.extension.sms.OAuth2SmsConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * OAuth2AuthorizationServerExtensionConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 22:10
 */
public class OAuth2AuthorizationServerExtensionConfigurer
    extends AbstractHttpConfigurer<OAuth2AuthorizationServerExtensionConfigurer, HttpSecurity> {

  OAuth2PasswordConfigurer passwordConfigurer = new OAuth2PasswordConfigurer();
  OAuth2SmsConfigurer smsConfigurer = new OAuth2SmsConfigurer();

  @Override
  public void init(HttpSecurity http) throws Exception {
    passwordConfigurer.init(http);
    smsConfigurer.init(http);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    passwordConfigurer.configure(http);
    smsConfigurer.configure(http);
  }

  public OAuth2AuthorizationServerExtensionConfigurer password(
      Customizer<OAuth2PasswordConfigurer> passwordConfigurerCustomizer) {
    passwordConfigurerCustomizer.customize(passwordConfigurer);
    return this;
  }

  public OAuth2AuthorizationServerExtensionConfigurer sms(
      Customizer<OAuth2SmsConfigurer> smsConfigurerCustomizer) {
    smsConfigurerCustomizer.customize(smsConfigurer);
    return this;
  }
}
