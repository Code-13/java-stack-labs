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

package io.github.code13.javastack.spring.security.captcha;

import io.github.code13.javastack.spring.security.commons.CustomAbstractAuthenticationFilterConfigurer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * CaptchaAuthenticationFilterConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/13 17:46
 */
public class CaptchaAuthenticationFilterConfigurer<H extends HttpSecurityBuilder<H>>
    extends CustomAbstractAuthenticationFilterConfigurer<
        H, CaptchaAuthenticationFilterConfigurer<H>, CaptchaAuthenticationFilter> {

  private CaptchaUserDetailsService captchaUserDetailsService;

  private CaptchaService captchaService;

  public CaptchaAuthenticationFilterConfigurer() {
    super(new CaptchaAuthenticationFilter());
  }

  @Override
  protected AuthenticationProvider doGetAuthenticationProvider(H http) {
    ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
    // 没有配置CaptchaUserDetailsService就去Spring IoC获取
    if (captchaUserDetailsService == null) {
      captchaUserDetailsService =
          getBeanOrNull(applicationContext, CaptchaUserDetailsService.class);
    }
    // 没有配置CaptchaService就去Spring IoC获取
    if (captchaService == null) {
      captchaService = getBeanOrNull(applicationContext, CaptchaService.class);
    }
    // 初始化 Provider
    return new CaptchaAuthenticationProvider(captchaUserDetailsService, captchaService);
  }

  public CaptchaAuthenticationFilterConfigurer<H> captchaUserDetailsService(
      CaptchaUserDetailsService captchaUserDetailsService) {
    this.captchaUserDetailsService = captchaUserDetailsService;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> captchaService(CaptchaService captchaService) {
    this.captchaService = captchaService;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> usernameParameter(String usernameParameter) {
    getAuthenticationFilter().setUsernameParameter(usernameParameter);
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> captchaParameter(String captchaParameter) {
    getAuthenticationFilter().setCaptchaParameter(captchaParameter);
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> parametersConverter(
      Converter<HttpServletRequest, CaptchaAuthenticationToken> converter) {
    getAuthenticationFilter().setConverter(converter);
    return this;
  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }
}
