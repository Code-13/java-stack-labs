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

package io.github.code13.spring.security.oauth2.authorization.server.extension.configurers;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * OAuth2AuthorizationServerExtensionConfigurer.
 *
 * <p>参考 {@link
 * org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer}
 *
 * @see
 *     org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 22:10
 */
public class OAuth2AuthorizationServerExtensionConfigurer
    extends AbstractHttpConfigurer<OAuth2AuthorizationServerExtensionConfigurer, HttpSecurity> {

  private final Map<Class<? extends AbstractOAuth2Configurer>, AbstractOAuth2Configurer>
      configurers = createConfigurers();

  private Map<Class<? extends AbstractOAuth2Configurer>, AbstractOAuth2Configurer>
      createConfigurers() {
    Map<Class<? extends AbstractOAuth2Configurer>, AbstractOAuth2Configurer> initConfigurers =
        new LinkedHashMap<>();
    initConfigurers.put(
        OAuth2PasswordConfigurer.class, new OAuth2PasswordConfigurer(this::postProcess));
    initConfigurers.put(OAuth2SmsConfigurer.class, new OAuth2SmsConfigurer(this::postProcess));
    return initConfigurers;
  }

  @Override
  public void init(HttpSecurity http) throws Exception {
    configurers.values().forEach(configurer -> configurer.init(http));
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    configurers.values().forEach(configurer -> configurer.configure(http));
  }

  public OAuth2AuthorizationServerExtensionConfigurer password(
      Customizer<OAuth2PasswordConfigurer> passwordConfigurerCustomizer) {
    passwordConfigurerCustomizer.customize(getConfigurer(OAuth2PasswordConfigurer.class));
    return this;
  }

  public OAuth2AuthorizationServerExtensionConfigurer sms(
      Customizer<OAuth2SmsConfigurer> smsConfigurerCustomizer) {
    smsConfigurerCustomizer.customize(getConfigurer(OAuth2SmsConfigurer.class));
    return this;
  }

  @SuppressWarnings("unchecked")
  private <T> T getConfigurer(Class<T> type) {
    return (T) configurers.get(type);
  }
}
