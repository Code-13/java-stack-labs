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
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/9 21:42
 */
// @EnableMethodSecurity //开启方法权限拦截
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

  private static final List<String> WHITELIST = List.of("/no/**");

  @Bean
  UserDetailsService userDetailsService() {
    return username ->
        User.withUsername(username)
            .password("password")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .authorities("ROLE_USER", "ROLE_ADMIN")
            .build();
  }

  @Bean
  SecurityFilterChain defaultSecurityChain(
      HttpSecurity http,
      UserDetailsService userDetailsService,
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter)
      throws Exception {

    http.csrf()
        .disable()
        // 出去白名单中的，其余全部拦截
        .authorizeRequests()
        .antMatchers(WHITELIST.toArray(String[]::new))
        .permitAll()
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        // 应用 Captcha 的配置
        .apply(new CaptchaAuthenticationFilterConfigurer<>())
        .captchaService((phone, rawCode) -> true) // 此处自己去自定义
        .captchaUserDetailsService(
            phone -> userDetailsService.loadUserByUsername("code13")) // 此处应该自定义用户信息
        .successHandler(
            (request, response, authentication) -> {
              // 这里把认证信息以JSON形式返回
              ServletServerHttpResponse servletServerHttpResponse =
                  new ServletServerHttpResponse(response);
              mappingJackson2HttpMessageConverter.write(
                  authentication, MediaType.APPLICATION_JSON, servletServerHttpResponse);
            })
        .failureHandler(
            (request, response, exception) -> {
              // 这里把认证信息以JSON形式返回
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              ServletServerHttpResponse servletServerHttpResponse =
                  new ServletServerHttpResponse(response);
              mappingJackson2HttpMessageConverter.write(
                  ResponseData.of(HttpStatus.UNAUTHORIZED, request.getRequestURI()),
                  MediaType.APPLICATION_JSON,
                  servletServerHttpResponse);
            })
        .and()
        .exceptionHandling() //
        .authenticationEntryPoint(
            (request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              ServletServerHttpResponse servletServerHttpResponse =
                  new ServletServerHttpResponse(response);
              mappingJackson2HttpMessageConverter.write(
                  ResponseData.of(HttpStatus.UNAUTHORIZED, request.getRequestURI()),
                  MediaType.APPLICATION_JSON,
                  servletServerHttpResponse);
            });

    return http.build();
  }

  @Bean
  WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/h2-console/**");
  }
}

/*
spring security 未登录或登录状态访问无权限资源都会出现访问异常，这个异常由 ExceptionTranslationFilter 处理，其中:
未登录，异常由 AuthenticationEntryPoint 处理，其中重定向至Login页面操作也是个EntryPoint：LoginUrlAuthenticationEntryPoint，他专为formLogin做跳转
已登录，异常由 AccessDeniedHandler 处理，同样可以自定义实现并配置

认证过程中的成功与失败则由 successHandler、failureHandler

如果是认证失败 ，比如密码错误等，就配置login 的 .failureHandler()。如果是权限不够（.accessDeniedHandler）或者未认证（.authenticationEntryPoint），就配置exceptionHandling()。
 */
