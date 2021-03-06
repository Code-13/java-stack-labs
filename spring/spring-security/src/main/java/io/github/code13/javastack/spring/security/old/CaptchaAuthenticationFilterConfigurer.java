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

package io.github.code13.javastack.spring.security.old;

import io.github.code13.javastack.spring.security.captcha.CaptchaAuthenticationFilter;
import io.github.code13.javastack.spring.security.captcha.CaptchaAuthenticationProvider;
import io.github.code13.javastack.spring.security.captcha.CaptchaAuthenticationToken;
import io.github.code13.javastack.spring.security.captcha.CaptchaService;
import io.github.code13.javastack.spring.security.captcha.CaptchaUserDetailsService;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * CaptchaAuthenticationFilterConfigurer.
 *
 * @see AbstractAuthenticationFilterConfigurer
 * @see FormLoginConfigurer
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/10/2022 3:02 PM
 */
public class CaptchaAuthenticationFilterConfigurer<H extends HttpSecurityBuilder<H>>
    extends AbstractHttpConfigurer<CaptchaAuthenticationFilterConfigurer<H>, H> {

  // ???????????? ?????????
  private final CaptchaAuthenticationFilter authFilter;
  // ??????????????????????????????
  private CaptchaUserDetailsService captchaUserDetailsService;
  // ?????????????????????
  private CaptchaService captchaService;
  // ?????????????????????????????????
  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
  // ?????????????????????????????????????????????
  private SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler =
      new SavedRequestAwareAuthenticationSuccessHandler();
  // ?????????????????????
  private AuthenticationSuccessHandler successHandler = defaultSuccessHandler;
  // ??????????????????
  private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;
  // ?????? ???????????????
  private boolean customLoginPage;
  // ????????????
  private String loginPage;
  // ????????????url
  private String loginProcessingUrl;
  // ?????????????????????
  private AuthenticationFailureHandler failureHandler;
  // ????????????????????????
  private boolean permitAll;
  // ???????????????url
  private String failureUrl;

  /** Creates a new instance with minimal defaults. */
  public CaptchaAuthenticationFilterConfigurer() {
    setLoginPage("/login/captcha");
    authFilter = new CaptchaAuthenticationFilter();
  }

  @Override
  public void init(H http) throws Exception {
    updateAuthenticationDefaults();
    //    updateAccessDefaults(http);
    //    registerDefaultAuthenticationEntryPoint(http);
    // ????????????????????????????????? ????????????????????????????????? ?????????????????? ?????????FormLogin??????
    // initDefaultLoginFilter(http);
    // ????????????Provider??????init?????????HttpSecurity
    initProvider(http);
  }

  private void registerDefaultAuthenticationEntryPoint(H http) {
    registerAuthenticationEntryPoint(http, authenticationEntryPoint);
  }

  protected final void registerAuthenticationEntryPoint(
      H http, AuthenticationEntryPoint authenticationEntryPoint) {
    ExceptionHandlingConfigurer<H> exceptionHandling =
        http.getConfigurer(ExceptionHandlingConfigurer.class);
    if (exceptionHandling == null) {
      return;
    }
    exceptionHandling.defaultAuthenticationEntryPointFor(
        postProcess(authenticationEntryPoint), getAuthenticationEntryPointMatcher(http));
  }

  protected final RequestMatcher getAuthenticationEntryPointMatcher(H http) {
    ContentNegotiationStrategy contentNegotiationStrategy =
        http.getSharedObject(ContentNegotiationStrategy.class);
    if (contentNegotiationStrategy == null) {
      contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
    }
    MediaTypeRequestMatcher mediaMatcher =
        new MediaTypeRequestMatcher(
            contentNegotiationStrategy,
            MediaType.APPLICATION_XHTML_XML,
            new MediaType("image", "*"),
            MediaType.TEXT_HTML,
            MediaType.TEXT_PLAIN);
    mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
    RequestMatcher notXRequestedWith =
        new NegatedRequestMatcher(
            new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
  }

  @Override
  public void configure(H http) throws Exception {

    PortMapper portMapper = http.getSharedObject(PortMapper.class);
    if (portMapper != null) {
      authenticationEntryPoint.setPortMapper(portMapper);
    }
    RequestCache requestCache = http.getSharedObject(RequestCache.class);
    if (requestCache != null) {
      defaultSuccessHandler.setRequestCache(requestCache);
    }

    authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
    authFilter.setAuthenticationSuccessHandler(successHandler);
    authFilter.setAuthenticationFailureHandler(failureHandler);
    if (authenticationDetailsSource != null) {
      authFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
    }
    SessionAuthenticationStrategy sessionAuthenticationStrategy =
        http.getSharedObject(SessionAuthenticationStrategy.class);
    if (sessionAuthenticationStrategy != null) {
      authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    }
    RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
    if (rememberMeServices != null) {
      authFilter.setRememberMeServices(rememberMeServices);
    }

    CaptchaAuthenticationFilter filter = postProcess(authFilter);
    // ???????????????????????????????????????
    http.addFilterBefore(filter, LogoutFilter.class);
  }

  private void initProvider(H http) {
    ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
    // ????????????CaptchaUserDetailsService??????Spring IoC??????
    if (captchaUserDetailsService == null) {
      captchaUserDetailsService =
          getBeanOrNull(applicationContext, CaptchaUserDetailsService.class);
    }
    // ????????????CaptchaService??????Spring IoC??????
    if (captchaService == null) {
      captchaService = getBeanOrNull(applicationContext, CaptchaService.class);
    }
    // ????????? Provider
    CaptchaAuthenticationProvider captchaAuthenticationProvider =
        postProcess(new CaptchaAuthenticationProvider(captchaUserDetailsService, captchaService));
    // ????????????ProviderManager??????????????????
    http.authenticationProvider(captchaAuthenticationProvider);
  }

  private static <T> T getBeanOrNull(ApplicationContext applicationContext, Class<T> type) {
    try {
      return applicationContext.getBean(type);
    } catch (NoSuchBeanDefinitionException notFound) {
      return null;
    }
  }

  private void setLoginPage(String loginPage) {
    this.loginPage = loginPage;
    authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
  }

  public CaptchaAuthenticationFilterConfigurer<H> formLoginDisabled() {
    //    this.formLoginEnabled = false;
    return this;
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
    authFilter.setUsernameParameter(usernameParameter);
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> captchaParameter(String captchaParameter) {
    authFilter.setCaptchaParameter(captchaParameter);
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> parametersConverter(
      Converter<HttpServletRequest, CaptchaAuthenticationToken> converter) {
    authFilter.setConverter(converter);
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> successHandler(
      AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> authenticationDetailsSource(
      AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
    this.authenticationDetailsSource = authenticationDetailsSource;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> permitAll() {
    return permitAll(true);
  }

  public CaptchaAuthenticationFilterConfigurer<H> permitAll(boolean permitAll) {
    this.permitAll = permitAll;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> failureHandler(
      AuthenticationFailureHandler authenticationFailureHandler) {
    failureUrl = null;
    failureHandler = authenticationFailureHandler;
    return this;
  }

  public CaptchaAuthenticationFilterConfigurer<H> loginProcessingUrl(String loginProcessingUrl) {
    this.loginProcessingUrl = loginProcessingUrl;
    authFilter.setRequiresAuthenticationRequestMatcher(
        createLoginProcessingUrlMatcher(loginProcessingUrl));
    return this;
  }

  public final CaptchaAuthenticationFilterConfigurer<H> failureUrl(
      String authenticationFailureUrl) {
    CaptchaAuthenticationFilterConfigurer<H> result =
        failureHandler(new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
    failureUrl = authenticationFailureUrl;
    return result;
  }

  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }

  protected final void updateAuthenticationDefaults() {
    if (loginProcessingUrl == null) {
      loginProcessingUrl(loginPage);
    }
    if (failureHandler == null) {
      failureUrl(loginPage + "?error");
    }
    //    LogoutConfigurer<B> logoutConfigurer = getBuilder().getConfigurer(LogoutConfigurer.class);
    //    if (logoutConfigurer != null && !logoutConfigurer.isCustomLogoutSuccess()) {
    //      logoutConfigurer.logoutSuccessUrl(loginPage + "?logout");
    //    }
  }
}
