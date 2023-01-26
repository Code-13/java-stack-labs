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

package io.github.code13.spring.security.oauth2.server.authorization.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusJweEncoder;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JweEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JweGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * JweConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/25 18:08
 */
// @Configuration
class JweConfiguration {

  // @Bean
  OAuth2TokenGenerator oAuth2TokenGenerator(
      JWKSource<SecurityContext> jwkSource, ApplicationContext applicationContext) {
    JweGenerator jweGenerator = new JweGenerator(new NimbusJweEncoder(jwkSource));
    OAuth2TokenCustomizer<JweEncodingContext> jweCustomizer = getJweCustomizer(applicationContext);
    if (jweCustomizer != null) {
      jweGenerator.setJweCustomizer(jweCustomizer);
    }

    OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
    OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer =
        getAccessTokenCustomizer(applicationContext);
    if (accessTokenCustomizer != null) {
      accessTokenGenerator.setAccessTokenCustomizer(accessTokenCustomizer);
    }

    OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

    return new DelegatingOAuth2TokenGenerator(
        jweGenerator, accessTokenGenerator, refreshTokenGenerator);
  }

  private static OAuth2TokenCustomizer<JweEncodingContext> getJweCustomizer(
      ApplicationContext context) {
    ResolvableType type =
        ResolvableType.forClassWithGenerics(OAuth2TokenCustomizer.class, JweEncodingContext.class);
    return getOptionalBean(context, type);
  }

  private static OAuth2TokenCustomizer<OAuth2TokenClaimsContext> getAccessTokenCustomizer(
      ApplicationContext applicationContext) {
    ResolvableType type =
        ResolvableType.forClassWithGenerics(
            OAuth2TokenCustomizer.class, OAuth2TokenClaimsContext.class);
    return getOptionalBean(applicationContext, type);
  }

  public static <T> T getOptionalBean(HttpSecurity httpSecurity, ResolvableType type) {
    ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
    return getOptionalBean(context, type);
  }

  public static <T> T getOptionalBean(ApplicationContext context, ResolvableType type) {
    String[] names = context.getBeanNamesForType(type);
    if (names.length > 1) {
      throw new NoUniqueBeanDefinitionException(type, names);
    }
    return names.length == 1 ? (T) context.getBean(names[0]) : null;
  }
}
