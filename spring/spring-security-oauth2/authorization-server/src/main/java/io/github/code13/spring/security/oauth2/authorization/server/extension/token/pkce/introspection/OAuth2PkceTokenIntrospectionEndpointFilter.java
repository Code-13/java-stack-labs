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

package io.github.code13.spring.security.oauth2.authorization.server.extension.token.pkce.introspection;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2TokenIntrospectionHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2TokenIntrospectionAuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OAuth2PkceTokenIntrospectionEndpointFilter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/11 20:07
 */
public class OAuth2PkceTokenIntrospectionEndpointFilter extends OncePerRequestFilter {

  /** The default endpoint {@code URI} for token introspection requests. */
  public static final String DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI = "/oauth2/introspect/pkce";

  private final AuthenticationManager authenticationManager;
  private final RequestMatcher tokenIntrospectionEndpointMatcher;
  private AuthenticationConverter authenticationConverter;
  private final HttpMessageConverter<OAuth2TokenIntrospection>
      tokenIntrospectionHttpResponseConverter = new OAuth2TokenIntrospectionHttpMessageConverter();
  private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter =
      new OAuth2ErrorHttpMessageConverter();
  private AuthenticationSuccessHandler authenticationSuccessHandler =
      this::sendIntrospectionResponse;
  private AuthenticationFailureHandler authenticationFailureHandler = this::sendErrorResponse;

  private final AuthenticationConverter clientAuthenticationConverter;

  /**
   * Constructs an {@code OAuth2TokenIntrospectionEndpointFilter} using the provided parameters.
   *
   * @param authenticationManager the authentication manager
   */
  public OAuth2PkceTokenIntrospectionEndpointFilter(AuthenticationManager authenticationManager) {
    this(authenticationManager, DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI);
  }

  /**
   * Constructs an {@code OAuth2TokenIntrospectionEndpointFilter} using the provided parameters.
   *
   * @param authenticationManager the authentication manager
   * @param tokenIntrospectionEndpointUri the endpoint {@code URI} for token introspection requests
   */
  public OAuth2PkceTokenIntrospectionEndpointFilter(
      AuthenticationManager authenticationManager, String tokenIntrospectionEndpointUri) {
    Assert.notNull(authenticationManager, "authenticationManager cannot be null");
    Assert.hasText(tokenIntrospectionEndpointUri, "tokenIntrospectionEndpointUri cannot be empty");
    this.authenticationManager = authenticationManager;
    tokenIntrospectionEndpointMatcher =
        new AntPathRequestMatcher(tokenIntrospectionEndpointUri, HttpMethod.POST.name());
    authenticationConverter = new OAuth2TokenIntrospectionAuthenticationConverter();
    clientAuthenticationConverter = new PublicClientAuthenticationConverter();
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (!tokenIntrospectionEndpointMatcher.matches(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // client
      Authentication clientAuthentication = clientAuthenticationConverter.convert(request);
      Authentication clientAuthenticationResult =
          authenticationManager.authenticate(clientAuthentication);

      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(clientAuthenticationResult);
      SecurityContextHolder.setContext(securityContext);
      if (logger.isDebugEnabled()) {
        logger.debug(
            LogMessage.format(
                "Set SecurityContextHolder authentication to %s",
                clientAuthenticationResult.getClass().getSimpleName()));
      }

      Authentication tokenIntrospectionAuthentication = authenticationConverter.convert(request);
      Authentication tokenIntrospectionAuthenticationResult =
          authenticationManager.authenticate(tokenIntrospectionAuthentication);
      authenticationSuccessHandler.onAuthenticationSuccess(
          request, response, tokenIntrospectionAuthenticationResult);
    } catch (OAuth2AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      if (logger.isTraceEnabled()) {
        logger.trace(
            LogMessage.format("Token introspection request failed: %s", ex.getError()), ex);
      }
      authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
    }
  }

  /**
   * Sets the {@link AuthenticationConverter} used when attempting to extract an Introspection
   * Request from {@link HttpServletRequest} to an instance of {@link
   * OAuth2TokenIntrospectionAuthenticationToken} used for authenticating the request.
   *
   * @param authenticationConverter the {@link AuthenticationConverter} used when attempting to
   *     extract an Introspection Request from {@link HttpServletRequest}
   * @since 0.2.3
   */
  public void setAuthenticationConverter(AuthenticationConverter authenticationConverter) {
    Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
    this.authenticationConverter = authenticationConverter;
  }

  /**
   * Sets the {@link AuthenticationSuccessHandler} used for handling an {@link
   * OAuth2TokenIntrospectionAuthenticationToken}.
   *
   * @param authenticationSuccessHandler the {@link AuthenticationSuccessHandler} used for handling
   *     an {@link OAuth2TokenIntrospectionAuthenticationToken}
   * @since 0.2.3
   */
  public void setAuthenticationSuccessHandler(
      AuthenticationSuccessHandler authenticationSuccessHandler) {
    Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
    this.authenticationSuccessHandler = authenticationSuccessHandler;
  }

  /**
   * Sets the {@link AuthenticationFailureHandler} used for handling an {@link
   * OAuth2AuthenticationException} and returning the {@link OAuth2Error Error Resonse}.
   *
   * @param authenticationFailureHandler the {@link AuthenticationFailureHandler} used for handling
   *     an {@link OAuth2AuthenticationException}
   * @since 0.2.3
   */
  public void setAuthenticationFailureHandler(
      AuthenticationFailureHandler authenticationFailureHandler) {
    Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
    this.authenticationFailureHandler = authenticationFailureHandler;
  }

  private void sendIntrospectionResponse(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OAuth2TokenIntrospectionAuthenticationToken tokenIntrospectionAuthentication =
        (OAuth2TokenIntrospectionAuthenticationToken) authentication;
    OAuth2TokenIntrospection tokenClaims = tokenIntrospectionAuthentication.getTokenClaims();
    ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
    tokenIntrospectionHttpResponseConverter.write(tokenClaims, null, httpResponse);
  }

  private void sendErrorResponse(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
    ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
    httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
    errorHttpResponseConverter.write(error, null, httpResponse);
  }
}
