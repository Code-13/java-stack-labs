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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OAuth2SsoFilter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/12 15:33
 */
class OAuth2SsoFilter extends OncePerRequestFilter {

  private static final String SSO_LOGIN_URI = "/oauth2/authorization/**";

  private static final String[] OAUTH2_CLIENT_WHITE_LIST = {
    "/oauth2/authorization/*", "/login/oauth2/code/*"
  };

  private SecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  private final OAuth2SsoProperties oAuth2SsoProperties;

  public OAuth2SsoFilter(OAuth2SsoProperties oAuth2SsoProperties) {
    this.oAuth2SsoProperties = oAuth2SsoProperties;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    if (!oAuth2SsoProperties.isEnable()) {
      chain.doFilter(request, response);
      return;
    }

    boolean ssoLogin = isSsoLogin(request);

    // return 401 code for request client
    if (!ssoLogin && !hasLogin(request) && !isInWhiteList(request)) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    if (ssoLogin) {
      // set redirectUri for redirect
      String redirectUri = request.getParameter(oAuth2SsoProperties.getRedirectUriKey());
      request.getSession().setAttribute(oAuth2SsoProperties.getRedirectUriKey(), redirectUri);
    }

    chain.doFilter(request, response);
  }

  /**
   * @see HttpSessionSecurityContextRepository#containsContext(HttpServletRequest)
   */
  private boolean hasLogin(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return false;
    }
    Object contextFromSession =
        session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    if (contextFromSession == null) {
      return false;
    }
    return contextFromSession instanceof SecurityContext;
  }

  private boolean isInWhiteList(HttpServletRequest request) {
    List<String> whiteList = new ArrayList<>();
    whiteList.addAll(oAuth2SsoProperties.getWhiteList());
    whiteList.addAll(Arrays.asList(OAUTH2_CLIENT_WHITE_LIST));
    return whiteList.stream().anyMatch(s -> new AntPathRequestMatcher(s).matches(request));
  }

  private boolean isSsoLogin(HttpServletRequest request) {
    return new AntPathRequestMatcher(SSO_LOGIN_URI).matches(request);
  }
}
