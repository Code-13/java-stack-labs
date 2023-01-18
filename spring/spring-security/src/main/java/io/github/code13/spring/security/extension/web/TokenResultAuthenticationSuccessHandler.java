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

package io.github.code13.spring.security.extension.web;

import io.github.code13.spring.security.extension.token.JwtUtils;
import io.github.code13.spring.security.extension.token.TokenResult;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * AuthenticationSuccessHandler.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/13 17:36
 */
public class TokenResultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  public TokenResultAuthenticationSuccessHandler() {}

  public TokenResultAuthenticationSuccessHandler(
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
    this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    // 这里把认证信息以JSON形式返回
    ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
    mappingJackson2HttpMessageConverter.write(
        genToken(authentication), MediaType.APPLICATION_JSON, servletServerHttpResponse);
  }

  @NonNull
  private Object genToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Duration accessTokenExpire = Duration.ofMinutes(10);
    String accessToken =
        JwtUtils.createToken(
            Map.of("userinfo", user), "qwertyuiopasdfghjklzxcvbnm1234567890", accessTokenExpire);

    Duration refreshTokenExpire = Duration.ofDays(1);
    String refreshToken =
        JwtUtils.createToken(
            Map.of("userinfo", user), "qwertyuiopasdfghjklzxcvbnm1234567890", refreshTokenExpire);

    return new TokenResult(
        accessToken,
        (int) accessTokenExpire.getSeconds(),
        refreshToken,
        (int) refreshTokenExpire.getSeconds());
  }

  public void setMappingJackson2HttpMessageConverter(
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
    this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
  }
}
