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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PermitResourceSecurityConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/27 21:13
 */
@Configuration(proxyBeanMethods = false)
class PermitResourceSecurityConfig implements WebMvcConfigurer {

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

  static RequestMatcher permitMatcher = new OrRequestMatcher(createAntMatchers(permitResources));

  private final WebMvcProperties mvcProperties;
  private final WebProperties webProperties;

  public PermitResourceSecurityConfig(WebMvcProperties mvcProperties, WebProperties webProperties) {
    this.mvcProperties = mvcProperties;
    this.webProperties = webProperties;
  }

  private static List<RequestMatcher> createAntMatchers(String... patterns) {
    return Arrays.stream(patterns)
        .map(AntPathRequestMatcher::new)
        .map(RequestMatcher.class::cast)
        .toList();
  }

  /**
   * @see https://github.com/spring-projects/spring-security/issues/10938
   */
  @Bean
  @Order(0)
  SecurityFilterChain resources(HttpSecurity http) throws Exception {
    http.securityMatcher(permitMatcher)
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .csrf(csrf -> csrf.ignoringRequestMatchers(permitMatcher))
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
    String[] staticLocations = webProperties.getResources().getStaticLocations();
    registry
        .addResourceHandler(mvcProperties.getStaticPathPattern())
        .addResourceLocations(
            combineArray(staticLocations, "classpath:/site/", "classpath:/site/assets/"));
  }

  private static String[] combineArray(String[] a, String... rest) {
    Set<String> r = new HashSet<>(Arrays.asList(a));
    r.addAll(Arrays.asList(rest));
    return r.toArray(new String[0]);
  }
}
