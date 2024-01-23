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

import io.github.code13.spring.security.oauth2.server.authorization.user.MyBaitsUserDetailsService;
import io.github.code13.spring.security.oauth2.server.authorization.user.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityCoreConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 20:15
 */
@EnableWebSecurity(debug = true)
class SecurityCoreConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login/account"));

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    DelegatingPasswordEncoder delegatingPasswordEncoder =
        ((DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder());
    delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
    return delegatingPasswordEncoder;
  }

  /**
   * Users user details service.
   *
   * @return the user details service
   */
  @Bean
  UserDetailsService users(UserMapper userMapper) {
    return new MyBaitsUserDetailsService(userMapper);
  }
}
