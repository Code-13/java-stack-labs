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

package io.github.code13.javastack.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * UserDetailsServiceConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/9 21:42
 */
@Configuration(proxyBeanMethods = false)
public class UserDetailsServiceConfiguration {

  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        User.withUsername(username)
            .password("password")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .authorities("ROLE_USER", "ROLE_ADMIN")
            .build();
  }
}
