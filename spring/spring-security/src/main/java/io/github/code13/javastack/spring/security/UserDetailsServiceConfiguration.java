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

import io.github.code13.javastack.spring.security.captcha.CaptchaAuthenticationFilterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

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

  @Bean
  SecurityFilterChain defaultSecurityChain(HttpSecurity http, UserDetailsService userDetailsService)
      throws Exception {

    http.csrf()
        .disable()
        .authorizeHttpRequests()
        .mvcMatchers("/foo/**")
        .hasAuthority("ROLE_USER")
        .anyRequest()
        .authenticated()
        .and()
        .apply(new CaptchaAuthenticationFilterConfigurer<>())
        .captchaService((phone, rawCode) -> true) // 此处自己去自定义
        .captchaUserDetailsService(
            phone -> userDetailsService.loadUserByUsername("code13")) // 此处应该自定义用户信息
        .successHandler(
            (request, response, authentication) -> {
              // 这里把认证信息以JSON形式返回
              ServletServerHttpResponse servletServerHttpResponse =
                  new ServletServerHttpResponse(response);
              MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                  new MappingJackson2HttpMessageConverter();
              mappingJackson2HttpMessageConverter.write(
                  authentication, MediaType.APPLICATION_JSON, servletServerHttpResponse);
            });

    return http.build();
  }
}
