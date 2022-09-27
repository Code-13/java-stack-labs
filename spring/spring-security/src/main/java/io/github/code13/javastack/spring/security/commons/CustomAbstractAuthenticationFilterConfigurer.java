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

package io.github.code13.javastack.spring.security.commons;

import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/**
 * CustomAbstractAuthenticationFilterConfigurer.
 *
 * <p>Because AbstractAuthenticationFilterConfigurer is intended for internal use only.
 *
 * @see AbstractAuthenticationFilterConfigurer
 * @see ExceptionHandlingConfigurer
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/13 16:50
 */
public abstract class CustomAbstractAuthenticationFilterConfigurer<
        B extends HttpSecurityBuilder<B>,
        T extends CustomAbstractAuthenticationFilterConfigurer<B, T, F>,
        F extends AbstractAuthenticationProcessingFilter>
    extends AbstractHttpConfigurer<T, B> {

  private F authFilter;

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  private AuthenticationSuccessHandler successHandler;

  private AuthenticationFailureHandler failureHandler;

  private AuthenticationEntryPoint authenticationEntryPoint =
      new JsonHttp401AuthenticationEntryPoint();

  private AccessDeniedHandler accessDeniedHandler;

  /**
   * Creates a new instance
   *
   * @param authenticationFilter the {@link AbstractAuthenticationProcessingFilter} to use
   */
  protected CustomAbstractAuthenticationFilterConfigurer(F authenticationFilter) {
    this(authenticationFilter, null);
  }

  /**
   * Creates a new instance
   *
   * @param authenticationFilter the {@link AbstractAuthenticationProcessingFilter} to use
   * @param defaultLoginProcessingUrl the default URL to use for {@link #loginProcessingUrl(String)}
   */
  protected CustomAbstractAuthenticationFilterConfigurer(
      F authenticationFilter, String defaultLoginProcessingUrl) {
    authFilter = authenticationFilter;
    if (defaultLoginProcessingUrl != null) {
      loginProcessingUrl(defaultLoginProcessingUrl);
    }
  }

  /**
   * Specifies the URL to validate the credentials.
   *
   * @param loginProcessingUrl the URL to validate username and password
   * @return the {@link FormLoginConfigurer} for additional customization
   */
  public T loginProcessingUrl(String loginProcessingUrl) {
    authFilter.setRequiresAuthenticationRequestMatcher(
        createLoginProcessingUrlMatcher(loginProcessingUrl));
    return getSelf();
  }

  /**
   * Create the {@link RequestMatcher} given a loginProcessingUrl
   *
   * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the loginProcessingUrl
   * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
   */
  protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

  /**
   * Specifies a custom {@link AuthenticationDetailsSource}. The default is {@link
   * WebAuthenticationDetailsSource}.
   *
   * @param authenticationDetailsSource the custom {@link AuthenticationDetailsSource}
   * @return the {@link FormLoginConfigurer} for additional customization
   */
  public final T authenticationDetailsSource(
      AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
    this.authenticationDetailsSource = authenticationDetailsSource;
    return getSelf();
  }

  /**
   * Specifies the {@link AuthenticationSuccessHandler} to be used. The default is {@link
   * SavedRequestAwareAuthenticationSuccessHandler} with no additional properties set.
   *
   * @param successHandler the {@link AuthenticationSuccessHandler}.
   * @return the {@link FormLoginConfigurer} for additional customization
   */
  public final T successHandler(AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
    return getSelf();
  }

  /**
   * Specifies the {@link AuthenticationFailureHandler} to use when authentication fails. The
   * default is redirecting to "/login?error" using {@link SimpleUrlAuthenticationFailureHandler}
   *
   * @param authenticationFailureHandler the {@link AuthenticationFailureHandler} to use when
   *     authentication fails.
   * @return the Configurer for additional customization
   */
  public final T failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
    failureHandler = authenticationFailureHandler;
    return getSelf();
  }

  public final T authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    return getSelf();
  }

  public final T accessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
    this.accessDeniedHandler = accessDeniedHandler;
    return getSelf();
  }

  @Override
  public void init(B http) throws Exception {
    updateAuthenticationDefaults();
    updateAccessDefaults(http);
    registerDefaultAuthenticationEntryPoint(http);
    registerDefaultAccessDeniedHandler(http);
    registerSuccessHandler(http);
    registerFailureHandler(http);
    initProvider(http);
  }

  private void registerSuccessHandler(B http) {
    if (successHandler == null) {
      ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
          getBeanOrNull(applicationContext, MappingJackson2HttpMessageConverter.class);
      successHandler =
          new TokenResultAuthenticationSuccessHandler(mappingJackson2HttpMessageConverter);
    }
  }

  private void registerFailureHandler(B http) {
    Assert.notNull(
        authenticationEntryPoint, "authenticationEntryPoint must not nll when set failureHandler");
    failureHandler = new AuthenticationEntryPointFailureHandler(authenticationEntryPoint);
  }

  protected final void registerDefaultAuthenticationEntryPoint(B http) {
    registerAuthenticationEntryPoint(http, authenticationEntryPoint);
  }

  @SuppressWarnings("unchecked")
  protected final void registerAuthenticationEntryPoint(
      B http, AuthenticationEntryPoint authenticationEntryPoint) {
    ExceptionHandlingConfigurer<B> exceptionHandling =
        http.getConfigurer(ExceptionHandlingConfigurer.class);
    if (exceptionHandling == null) {
      return;
    }
    exceptionHandling.defaultAuthenticationEntryPointFor(
        postProcess(authenticationEntryPoint), getAuthenticationEntryPointMatcher(http));
  }

  protected final void registerDefaultAccessDeniedHandler(B http) {
    if (accessDeniedHandler != null) {
      registerDefaultAccessDeniedHandler(http, accessDeniedHandler);
    }
  }

  private void registerDefaultAccessDeniedHandler(B http, AccessDeniedHandler accessDeniedHandler) {
    ExceptionHandlingConfigurer<B> exceptionHandling =
        http.getConfigurer(ExceptionHandlingConfigurer.class);
    if (exceptionHandling == null) {
      return;
    }
    exceptionHandling.defaultAccessDeniedHandlerFor(
        postProcess(accessDeniedHandler), getAuthenticationEntryPointMatcher(http));
  }

  protected final RequestMatcher getAuthenticationEntryPointMatcher(B http) {
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

  private void initProvider(B http) {
    AuthenticationProvider authenticationProvider = doGetAuthenticationProvider(http);

    if (authenticationProvider != null) {
      // 会增加到ProviderManager的注册列表中
      http.authenticationProvider(postProcess(authenticationProvider));
    }
  }

  /**
   * 某些认证方式需要提供自定义的 AuthenticationProvider，如果不需要提供，返回 null
   *
   * @return AuthenticationProvider
   */
  protected abstract AuthenticationProvider doGetAuthenticationProvider(B http);

  @Override
  public void configure(B http) throws Exception {
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

    F filter = doConfigure(authFilter, http);
    // 这里改为使用前插过滤器方法
    http.addFilterBefore(postProcess(filter), LogoutFilter.class);
  }

  /**
   * 对 Filter 进行 其他的配置
   *
   * @param authFilter 已经进行了初步配置的Filter
   * @param http config
   * @return authFilter
   */
  protected F doConfigure(F authFilter, B http) {
    return authFilter;
  }

  /** Updates the default values for authentication. */
  protected final void updateAuthenticationDefaults() {
    // do nothing
  }

  /** Updates the default values for access. */
  protected final void updateAccessDefaults(B http) {
    // do nothing
  }

  /**
   * Gets the Authentication Filter
   *
   * @return the Authentication Filter
   */
  protected final F getAuthenticationFilter() {
    return authFilter;
  }

  /**
   * Sets the Authentication Filter
   *
   * @param authFilter the Authentication Filter
   */
  protected final void setAuthenticationFilter(F authFilter) {
    this.authFilter = authFilter;
  }

  /**
   * Gets the Authentication Entry Point
   *
   * @return the Authentication Entry Point
   */
  protected final AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return authenticationEntryPoint;
  }

  protected static <T> T getBeanOrNull(ApplicationContext applicationContext, Class<T> type) {
    try {
      return applicationContext.getBean(type);
    } catch (NoSuchBeanDefinitionException notFound) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private T getSelf() {
    return (T) this;
  }
}
