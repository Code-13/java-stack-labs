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

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ResourceServerAutoConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/5 17:53
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ResourceServerWhiteListProperties.class)
class ResourceServerConfiguration {

  final ResourceServerWhiteListProperties properties;
  final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

  public ResourceServerConfiguration(
      ResourceServerWhiteListProperties properties,
      AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) {
    this.properties = properties;
    this.authenticationManagerResolver = authenticationManagerResolver;
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorizeRequests ->
                authorizeRequests
                    .antMatchers(properties.getUrls().toArray(new String[0]))
                    .permitAll())
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .oauth2ResourceServer(
            // Supporting both JWT and Opaque Token
            oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver))
        .csrf()
        .disable();

    return http.build();
  }
}
