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

package io.github.code13.spring.security.oauth2.authorization.server.extension;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
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
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * OAuth2ResourceOwnerAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 22:36
 */
public abstract class OAuth2ResourceOwnerAuthenticationProvider<
        T extends OAuth2ResourceOwnerAuthenticationToken>
    implements AuthenticationProvider {

  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

  private final Log logger = LogFactory.getLog(getClass());

  private final OAuth2AuthorizationService authorizationService;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
  private final AuthenticationManager authenticationManager;

  protected MessageSourceAccessor messages = AuthorizationServerMessageSource.getAccessor();

  protected OAuth2ResourceOwnerAuthenticationProvider(
      OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      AuthenticationManager authenticationManager) {
    this.authorizationService = authorizationService;
    this.tokenGenerator = tokenGenerator;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    @SuppressWarnings("unchecked")
    T token = (T) authentication;

    OAuth2ClientAuthenticationToken clientPrincipal =
        getAuthenticatedClientElseThrowInvalidClient(token);

    // client
    RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
    Assert.notNull(registeredClient, "registeredClient must not be null!");

    // GrantType
    if (!registeredClient.getAuthorizationGrantTypes().contains(getSupportGrantType())) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

    // scopes
    Set<String> authorizedScopes = getAuthorizedScopes(token, registeredClient);

    Authentication tokenNeedAuthenticate = buildAuthenticationToken(token);
    Authentication authenticated = authenticationManager.authenticate(tokenNeedAuthenticate);

    DefaultOAuth2TokenContext.Builder tokenContextBuilder =
        DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(authenticated)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizedScopes(authorizedScopes)
            .authorizationGrantType(getSupportGrantType())
            .authorizationGrant(token);

    OAuth2Authorization.Builder authorizationBuilder =
        OAuth2Authorization.withRegisteredClient(registeredClient)
            .principalName(authenticated.getName())
            .authorizationGrantType(getSupportGrantType())
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
    if (generatedAccessToken instanceof ClaimAccessor claimaccessor) {
      authorizationBuilder
          .token(
              accessToken,
              metadata ->
                  metadata.put(
                      OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimaccessor.getClaims()))
          .authorizedScopes(authorizedScopes)
          .attribute(Principal.class.getName(), authenticated);
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

    // ----- open_id token is not required -----

    OAuth2Authorization authorization = authorizationBuilder.build();
    authorizationService.save(authorization);

    return new OAuth2AccessTokenAuthenticationToken(
        registeredClient,
        clientPrincipal,
        accessToken,
        refreshToken,
        Objects.requireNonNull(authorization.getAccessToken().getClaims()));
  }

  /**
   * Gets support grant_type.
   *
   * @return the support grant_type
   */
  protected abstract AuthorizationGrantType getSupportGrantType();

  protected Set<String> getAuthorizedScopes(T token, RegisteredClient registeredClient) {
    Set<String> authorizedScopes = Collections.emptySet();

    // Default to configured scopes
    if (!CollectionUtils.isEmpty(token.getScopes())) {
      for (String requestedScope : token.getScopes()) {
        if (!registeredClient.getScopes().contains(requestedScope)) {
          throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }
      }
      authorizedScopes = new LinkedHashSet<>(token.getScopes());
    }
    return authorizedScopes;
  }

  /** Authentication for custom grant_type */
  protected abstract Authentication buildAuthenticationToken(T token);

  /**
   * Sets MessageSource.
   *
   * @param messageSource the message source
   */
  public void setMessageSource(MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
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
