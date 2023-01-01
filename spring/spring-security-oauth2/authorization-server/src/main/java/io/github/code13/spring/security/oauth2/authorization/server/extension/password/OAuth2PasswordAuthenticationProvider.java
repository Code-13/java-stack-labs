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

package io.github.code13.spring.security.oauth2.authorization.server.extension.password;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

/**
 * OAuth2PasswordAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 14:15
 */
public class OAuth2PasswordAuthenticationProvider implements AuthenticationProvider {
  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

  private final Log logger = LogFactory.getLog(getClass());

  private final OAuth2AuthorizationService authorizationService;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
  private final AuthenticationManager authenticationManager;

  public OAuth2PasswordAuthenticationProvider(
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      AuthenticationManager authenticationManager) {
    this.authorizationService = authorizationService;
    this.tokenGenerator = tokenGenerator;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    OAuth2PasswordAuthenticationToken passwordAuthenticationToken =
        (OAuth2PasswordAuthenticationToken) authentication;

    OAuth2ClientAuthenticationToken clientPrincipal =
        getAuthenticatedClientElseThrowInvalidClient(passwordAuthenticationToken);
    RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

    // check client
    if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

    Set<String> authorizedScopes;
    // Default to configured scopes
    if (!CollectionUtils.isEmpty(passwordAuthenticationToken.getScopes())) {
      for (String requestedScope : passwordAuthenticationToken.getScopes()) {
        if (!registeredClient.getScopes().contains(requestedScope)) {
          throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }
      }
      authorizedScopes = new LinkedHashSet<>(passwordAuthenticationToken.getScopes());
    } else {
      throw new OAuth2AuthenticationException("scope_empty");
    }

    // 此处可以自定义拓展
    Map<String, Object> additionalParameters =
        passwordAuthenticationToken.getAdditionalParameters();
    String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
    String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    // DaoAuthenticationProvider -> UserDetailsService
    Authentication usernamePasswordAuthentication =
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    DefaultOAuth2TokenContext.Builder tokenContextBuilder =
        DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(usernamePasswordAuthentication)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizedScopes(authorizedScopes)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizationGrant(passwordAuthenticationToken);

    OAuth2Authorization.Builder authorizationBuilder =
        OAuth2Authorization.withRegisteredClient(registeredClient)
            .principalName(usernamePasswordAuthentication.getName())
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            .authorizedScopes(authorizedScopes);

    // ----- Access token -----
    OAuth2TokenContext tokenContext =
        tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
    OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
    if (generatedAccessToken == null) {
      OAuth2Error error =
          new OAuth2Error(
              OAuth2ErrorCodes.SERVER_ERROR,
              "The token generator failed to generate the access token.",
              ERROR_URI);
      throw new OAuth2AuthenticationException(error);
    }

    if (logger.isTraceEnabled()) {
      logger.trace("Generated access token");
    }

    OAuth2AccessToken accessToken =
        new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(),
            generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(),
            tokenContext.getAuthorizedScopes());
    if (generatedAccessToken instanceof ClaimAccessor) {
      authorizationBuilder
          .token(
              accessToken,
              metadata ->
                  metadata.put(
                      OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                      ((ClaimAccessor) generatedAccessToken).getClaims()))
          .authorizedScopes(authorizedScopes)
          .attribute(Principal.class.getName(), usernamePasswordAuthentication);
    } else {
      authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
    }

    // ----- Refresh token -----
    OAuth2RefreshToken refreshToken = null;
    if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)
        &&
        // Do not issue refresh token to public client
        !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

      tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
      OAuth2Token generatedRefreshToken = tokenGenerator.generate(tokenContext);
      if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
        OAuth2Error error =
            new OAuth2Error(
                OAuth2ErrorCodes.SERVER_ERROR,
                "The token generator failed to generate the refresh token.",
                ERROR_URI);
        throw new OAuth2AuthenticationException(error);
      }

      if (logger.isTraceEnabled()) {
        logger.trace("Generated refresh token");
      }

      refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
      authorizationBuilder.refreshToken(refreshToken);
    }

    OAuth2Authorization authorization = authorizationBuilder.build();
    authorizationService.save(authorization);

    return new OAuth2AccessTokenAuthenticationToken(
        registeredClient,
        clientPrincipal,
        accessToken,
        refreshToken,
        Objects.requireNonNull(authorization.getAccessToken().getClaims()));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  /**
   * copy form {@link
   * org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthenticationProviderUtils#getAuthenticatedClientElseThrowInvalidClient(Authentication)}
   */
  static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
      Authentication authentication) {
    OAuth2ClientAuthenticationToken clientPrincipal = null;
    if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
        authentication.getPrincipal().getClass())) {
      clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
    }
    if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
      return clientPrincipal;
    }
    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
  }
}
