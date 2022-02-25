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

package io.github.code13.javastack.spring.security.oauth2.authorization.server;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityCoreConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 20:15
 */
@EnableWebSecurity(debug = true)
public class SecurityCoreConfig {

  // @formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .formLogin()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(
            (request, response, authException) -> response.sendRedirect("http://localhost:3000/"));
    return http.build();
  }
  // @formatter:on

  /**
   * Users user details service.
   *
   * @return the user details service
   */
  // @formatter:off
  @Bean
  UserDetailsService users() {
    UserDetails user =
        User.builder()
            .username("felord")
            .password("password")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
  // @formatter:on

  /**
   * Web security customizer web security customizer.
   *
   * @return the web security customizer
   */
  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/actuator/health", "/h2-console/**");
  }
}