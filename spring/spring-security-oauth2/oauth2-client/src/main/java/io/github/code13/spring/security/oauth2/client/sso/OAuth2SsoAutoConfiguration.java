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

package io.github.code13.spring.security.oauth2.client.sso;

import static org.springframework.boot.web.servlet.filter.OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * OAuth2SsoAutoConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/12 15:54
 */
@Conditional(OAuth2SsoCondition.class) // Only active on SSO condition
@EnableConfigurationProperties(OAuth2SsoProperties.class)
class OAuth2SsoAutoConfiguration {

  @Bean
  public FilterRegistrationBean<OAuth2SsoFilter> oAuth2SsoFilterRegistrationBean(
      OAuth2SsoProperties oAuth2SsoProperties) {
    FilterRegistrationBean<OAuth2SsoFilter> bean =
        new FilterRegistrationBean<>(new OAuth2SsoFilter(oAuth2SsoProperties));
    // Order defaults to after Request Context filter
    bean.setOrder(REQUEST_WRAPPER_FILTER_MAX_ORDER - 103);
    return bean;
  }

  @Bean
  SecurityFilterChain ssoSecurityFilterChain(
      HttpSecurity http, OAuth2SsoProperties oAuth2SsoProperties) throws Exception {

    if (!oAuth2SsoProperties.isEnable()) {
      return http.csrf()
          .disable()
          .authorizeRequests(registry -> registry.anyRequest().permitAll())
          .build();
    }

    return http.csrf()
        .disable()
        .cors()
        .disable()
        .authorizeRequests(
            registry ->
                registry
                    .antMatchers(oAuth2SsoProperties.getWhiteList().toArray(new String[0]))
                    .permitAll())
        .authorizeRequests(registry -> registry.anyRequest().authenticated())
        .oauth2Login()
        .userInfoEndpoint(
            userInfoEndpointConfig -> {
              // 此处可以配置 userInfoService
            })
        .successHandler(
            (request, response, authentication) -> {
              String redirectUri =
                  (String)
                      request.getSession().getAttribute(oAuth2SsoProperties.getRedirectUriKey());
              request.getSession().removeAttribute(oAuth2SsoProperties.getRedirectUriKey());
              response.sendRedirect(redirectUri);
            })
        .and()
        .build();
  }

  /** 允许跨域 */
  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean<CorsFilter> filterRegistrationBean =
        new FilterRegistrationBean<>(new CorsFilter(source));
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return filterRegistrationBean;
  }
}
