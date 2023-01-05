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

package io.github.code13.spring.security.oauth2.resource.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * ResourceServerAutoConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/4 23:11
 */
@Configuration
class ResourceServerComponentConfiguration {

  /**
   * Supporting both JWT and Opaque Token
   *
   * @see <a
   *     href="https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/multitenancy.html#oauth2reourceserver-opaqueandjwt">Supporting
   *     both JWT and Opaque Token</a>
   */
  @Bean
  AuthenticationManagerResolver<HttpServletRequest> tokenAuthenticationManagerResolver(
      JwtDecoder jwtDecoder,
      OpaqueTokenIntrospector opaqueTokenIntrospector,
      ObjectMapper objectMapper) {
    AuthenticationManager jwt = new ProviderManager(new JwtAuthenticationProvider(jwtDecoder));
    AuthenticationManager opaqueToken =
        new ProviderManager(new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector));
    return new ResourceServerAuthenticationManagerResolver(jwt, opaqueToken, objectMapper);
  }
}
