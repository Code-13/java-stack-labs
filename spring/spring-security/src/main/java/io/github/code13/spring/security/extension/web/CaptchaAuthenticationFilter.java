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

import io.github.code13.spring.security.extension.authentication.captcha.CaptchaAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

/**
 * CaptchaAuthenticationFilter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/10/2022 2:52 PM
 */
public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public static final String SPRING_SECURITY_CAPTCHA_USERNAME_KEY = "phone";
  public static final String SPRING_SECURITY_CAPTCHA_KEY = "captcha";
  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/login/captcha", "POST");
  private String usernameParameter = SPRING_SECURITY_CAPTCHA_USERNAME_KEY;
  private String captchaParameter = SPRING_SECURITY_CAPTCHA_KEY;
  private AuthenticationConverter converter;

  public CaptchaAuthenticationFilter() {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    converter = defaultConverter();
  }

  public CaptchaAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    converter = defaultConverter();
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    Authentication authenticationToken = converter.convert(request);

    Assert.isInstanceOf(CaptchaAuthenticationToken.class, authenticationToken);
    CaptchaAuthenticationToken authentication = (CaptchaAuthenticationToken) authenticationToken;

    setDetails(request, authentication);

    return getAuthenticationManager().authenticate(authenticationToken);
  }

  protected void setDetails(HttpServletRequest request, CaptchaAuthenticationToken authRequest) {
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
  }

  private AuthenticationConverter defaultConverter() {
    return request -> {
      String username = request.getParameter(usernameParameter);
      username = (username != null) ? username : "";
      String captcha = request.getParameter(captchaParameter);
      captcha = (captcha != null) ? captcha : "";
      return new CaptchaAuthenticationToken(username, captcha);
    };
  }

  public String getUsernameParameter() {
    return usernameParameter;
  }

  public void setUsernameParameter(String usernameParameter) {
    this.usernameParameter = usernameParameter;
  }

  public String getCaptchaParameter() {
    return captchaParameter;
  }

  public void setCaptchaParameter(String captchaParameter) {
    this.captchaParameter = captchaParameter;
  }

  public void setConverter(AuthenticationConverter converter) {
    this.converter = converter;
  }
}
