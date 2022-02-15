/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.spring.security.oauth2.client;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2ClientSecurityConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 22:01
 */
@EnableWebSecurity(debug = true)
public class OAuth2ClientSecurityConfiguration {

  /**
   * 放开对{@code redirect_uri}的访问，否则会出现{@code 403}，授权服务器需要回调该地址
   *
   * @param httpSecurity the http security
   * @return the security filter chain
   * @throws Exception the exception
   */
  @Bean
  SecurityFilterChain oauth2ClientSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .mvcMatchers(HttpMethod.GET, "/foo/bar")
        .hasAnyAuthority("ROLE_USER", "ROLE_ANONYMOUS")
        .anyRequest()
        .authenticated()
        .and()
        // oauth2.0 login
        .oauth2Login(
            oauth2clientLogin -> oauth2clientLogin.loginPage("/oauth2/authorization/felord-oidc"))
        // oauth2.0 client
        .oauth2Client();
    return httpSecurity.build();
  }
}
