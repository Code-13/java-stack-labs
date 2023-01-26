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

package io.github.code13.spring.security.oauth2.authorization.server.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SecurityCoreConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 20:15
 */
@EnableWebSecurity(debug = true)
class SecurityCoreConfig {

  // @formatter:off
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeHttpRequests(
            authorizeRequests -> authorizeRequests.requestMatchers("/login").permitAll())
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login/form")
        .failureHandler(
            (request, response, exception) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            });
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
            .username("test")
            .password("test")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
  }
  // @formatter:on

  @Configuration(proxyBeanMethods = false)
  public static class PermitResourceSecurityConfig implements WebMvcConfigurer {

    private static final String[] permitResources =
        new String[] {
          // "/",
          "/actuator/**",
          "/h2-console/**",
          "/favicon.ico",
          "/**/*.json",
          "/**/*.html",
          "/**/*.js",
          "/**/*.js.gz",
          "/**/*.css",
          "/**/*.ttf",
          "/**/*.png",
          "/**/*.jpg",
          "/**/*.jpeg",
          "/**/*.svg",
        };

    static RequestMatcher securityMatcher =
        new OrRequestMatcher(createAntMatchers(permitResources));

    private static List<RequestMatcher> createAntMatchers(String... patterns) {
      List<RequestMatcher> matchers = new ArrayList<>(patterns.length);
      for (String pattern : patterns) {
        matchers.add(new AntPathRequestMatcher(pattern));
      }
      return matchers;
    }

    /**
     * @see https://github.com/spring-projects/spring-security/issues/10938
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
      http.securityMatcher(securityMatcher)
          .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
          .csrf(csrf -> csrf.ignoringRequestMatchers(securityMatcher))
          .requestCache()
          .disable()
          .securityContext()
          .disable()
          .sessionManagement()
          .disable();

      return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry
          .addResourceHandler("/**")
          .addResourceLocations("classpath:/site/", "classpath:/site/assets/");
    }
  }
}
