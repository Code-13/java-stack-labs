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

package org.springframework.security.oauth2.server.authorization.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.JWEAlgorithm;
import org.springframework.security.oauth2.jwt.JweEncoder;
import org.springframework.security.oauth2.jwt.JweEncoderParameters;
import org.springframework.security.oauth2.jwt.JweHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * JweGenerator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:19
 */
public class JweGenerator implements OAuth2TokenGenerator<Jwt> {

  private final JweEncoder jweEncoder;
  private OAuth2TokenCustomizer<JweEncodingContext> jweCustomizer;

  /**
   * Constructs a {@code JweGenerator} using the provided parameters.
   *
   * @param jweEncoder the jwe encoder
   */
  public JweGenerator(JweEncoder jweEncoder) {
    Assert.notNull(jweEncoder, "jwtEncoder cannot be null");
    this.jweEncoder = jweEncoder;
  }

  @Override
  public Jwt generate(OAuth2TokenContext context) {
    if (context.getTokenType() == null
        || (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())
            && !OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue()))) {
      return null;
    }
    if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())
        && !OAuth2TokenFormat.SELF_CONTAINED.equals(
            context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
      return null;
    }

    String issuer = null;
    if (context.getAuthorizationServerContext() != null) {
      issuer = context.getAuthorizationServerContext().getIssuer();
    }
    RegisteredClient registeredClient = context.getRegisteredClient();

    Instant issuedAt = Instant.now();
    Instant expiresAt;
    JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
    if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
      // TODO Allow configuration for ID Token time-to-live
      expiresAt = issuedAt.plus(30, ChronoUnit.MINUTES);
    } else {
      expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());
    }

    // @formatter:off
    JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
    if (StringUtils.hasText(issuer)) {
      claimsBuilder.issuer(issuer);
    }
    claimsBuilder
        .subject(context.getPrincipal().getName())
        .audience(Collections.singletonList(registeredClient.getClientId()))
        .issuedAt(issuedAt)
        .expiresAt(expiresAt);
    if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
      claimsBuilder.notBefore(issuedAt);
      if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
        claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
      }
    } else if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
      claimsBuilder.claim(IdTokenClaimNames.AZP, registeredClient.getClientId());
      if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())) {
        OAuth2AuthorizationRequest authorizationRequest =
            context.getAuthorization().getAttribute(OAuth2AuthorizationRequest.class.getName());
        String nonce =
            (String) authorizationRequest.getAdditionalParameters().get(OidcParameterNames.NONCE);
        if (StringUtils.hasText(nonce)) {
          claimsBuilder.claim(IdTokenClaimNames.NONCE, nonce);
        }
      }
      // TODO Add 'auth_time' claim
    }
    // @formatter:on

    JweHeader.Builder jweHeaderBuilder = JweHeader.with(jweAlgorithm).header("enc", "A256GCM");
    if (jweCustomizer != null) {
      // @formatter:off
      JweEncodingContext.Builder jweContextBuilder =
          JweEncodingContext.with(jweHeaderBuilder, claimsBuilder)
              .registeredClient(context.getRegisteredClient())
              .principal(context.getPrincipal())
              .authorizationServerContext(context.getAuthorizationServerContext())
              .authorizedScopes(context.getAuthorizedScopes())
              .tokenType(context.getTokenType())
              .authorizationGrantType(context.getAuthorizationGrantType());
      if (context.getAuthorization() != null) {
        jweContextBuilder.authorization(context.getAuthorization());
      }
      if (context.getAuthorizationGrant() != null) {
        jweContextBuilder.authorizationGrant(context.getAuthorizationGrant());
      }
      // @formatter:on

      JweEncodingContext jwtContext = jweContextBuilder.build();
      jweCustomizer.customize(jwtContext);
    }

    JweHeader jweHeader = jweHeaderBuilder.build();
    JwtClaimsSet claims = claimsBuilder.build();

    Jwt jwt = jweEncoder.encode(JweEncoderParameters.from(jweHeader, claims));

    return jwt;
  }

  /**
   * Sets the {@link OAuth2TokenCustomizer} that customizes the {@link
   * JweEncodingContext#getJweHeader()} () JWS headers} and/or {@link JweEncodingContext#getClaims()
   * claims} for the generated {@link Jwt}.
   *
   * @param jwtCustomizer the {@link OAuth2TokenCustomizer} that customizes the headers and/or
   *     claims for the generated {@code Jwt}
   */
  public void setJweCustomizer(OAuth2TokenCustomizer<JweEncodingContext> jweCustomizer) {
    Assert.notNull(jweCustomizer, "jwtCustomizer cannot be null");
    this.jweCustomizer = jweCustomizer;
  }
}
