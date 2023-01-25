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

package org.springframework.security.oauth2.jwt;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWEEncryptionKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.net.URI;
import java.net.URL;
import java.security.Key;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.oauth2.jose.JWEAlgorithm;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * NimbusJweEncoder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:20
 */
public class NimbusJweEncoder implements JweEncoder {

  private static final String ENCODING_ERROR_MESSAGE_TEMPLATE =
      "An error occurred while attempting to encode the Jwt: %s";

  private static final JweHeader DEFAULT_JWE_HEADER =
      JweHeader.with(JWEAlgorithm.RSA_OAEP_256).header("enc", "A256GCM").build();

  private final JWKSource<SecurityContext> jwkSource;

  public NimbusJweEncoder(JWKSource<SecurityContext> jwkSource) {
    Assert.notNull(jwkSource, "jwkSource cannot be null");
    this.jwkSource = jwkSource;
  }

  @Override
  public Jwt encode(JweEncoderParameters parameters) throws JwtEncodingException {
    Assert.notNull(parameters, "parameters cannot be null");

    JweHeader jweHeader = parameters.getJweHeader();
    if (jweHeader == null) {
      jweHeader = DEFAULT_JWE_HEADER;
    }

    JwtClaimsSet claims = parameters.getClaims();

    JWEHeader header = convert(jweHeader);
    JWTClaimsSet jwtClaims = convert(claims);

    JWEEncryptionKeySelector<SecurityContext> selector =
        new JWEEncryptionKeySelector<>(
            com.nimbusds.jose.JWEAlgorithm.parse(jweHeader.getAlgorithm().name()),
            EncryptionMethod.parse(jweHeader.getHeader("enc")),
            jwkSource);

    try {
      List<? extends Key> jweKeys = selector.selectJWEKeys(header, null);
      List<PublicKey> keys =
          jweKeys.stream().filter(PublicKey.class::isInstance).map(PublicKey.class::cast).toList();

      PublicKey publicKey = keys.get(0);

      String jwe = serialize(header, jwtClaims, (RSAPublicKey) publicKey);

      return new Jwt(
          jwe,
          claims.getIssuedAt(),
          claims.getExpiresAt(),
          jweHeader.getHeaders(),
          claims.getClaims());
    } catch (KeySourceException e) {
      throw new RuntimeException(e);
    }
  }

  private String serialize(JWEHeader header, JWTClaimsSet jwtClaims, RSAPublicKey publicKey) {
    EncryptedJWT jwt = new EncryptedJWT(header, jwtClaims);
    RSAEncrypter encrypter = new RSAEncrypter(publicKey);
    encrypter.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());
    try {
      jwt.encrypt(encrypter);
    } catch (JOSEException e) {
      throw new JwtEncodingException(
          String.format(
              ENCODING_ERROR_MESSAGE_TEMPLATE, "Failed to sign the JWT -> " + e.getMessage()),
          e);
    }
    return jwt.serialize();
  }

  private static JWEHeader convert(JweHeader headers) {
    JWEHeader.Builder builder =
        new JWEHeader.Builder(
            com.nimbusds.jose.JWEAlgorithm.parse(headers.getAlgorithm().getName()),
            EncryptionMethod.A256GCM);

    if (headers.getJwkSetUrl() != null) {
      builder.jwkURL(convertAsURI(JoseHeaderNames.JKU, headers.getJwkSetUrl()));
    }

    Map<String, Object> jwk = headers.getJwk();
    if (!CollectionUtils.isEmpty(jwk)) {
      try {
        builder.jwk(JWK.parse(jwk));
      } catch (Exception ex) {
        throw new JwtEncodingException(
            String.format(
                ENCODING_ERROR_MESSAGE_TEMPLATE,
                "Unable to convert '" + JoseHeaderNames.JWK + "' JOSE header"),
            ex);
      }
    }

    String keyId = headers.getKeyId();
    if (StringUtils.hasText(keyId)) {
      builder.keyID(keyId);
    }

    if (headers.getX509Url() != null) {
      builder.x509CertURL(convertAsURI(JoseHeaderNames.X5U, headers.getX509Url()));
    }

    List<String> x509CertificateChain = headers.getX509CertificateChain();
    if (!CollectionUtils.isEmpty(x509CertificateChain)) {
      List<Base64> x5cList = new ArrayList<>();
      x509CertificateChain.forEach((x5c) -> x5cList.add(new Base64(x5c)));
      if (!x5cList.isEmpty()) {
        builder.x509CertChain(x5cList);
      }
    }

    String x509SHA1Thumbprint = headers.getX509SHA1Thumbprint();
    if (StringUtils.hasText(x509SHA1Thumbprint)) {
      builder.x509CertThumbprint(new Base64URL(x509SHA1Thumbprint));
    }

    String x509SHA256Thumbprint = headers.getX509SHA256Thumbprint();
    if (StringUtils.hasText(x509SHA256Thumbprint)) {
      builder.x509CertSHA256Thumbprint(new Base64URL(x509SHA256Thumbprint));
    }

    String type = headers.getType();
    if (StringUtils.hasText(type)) {
      builder.type(new JOSEObjectType(type));
    }

    String contentType = headers.getContentType();
    if (StringUtils.hasText(contentType)) {
      builder.contentType(contentType);
    }

    Set<String> critical = headers.getCritical();
    if (!CollectionUtils.isEmpty(critical)) {
      builder.criticalParams(critical);
    }

    Map<String, Object> customHeaders = new HashMap<>();
    headers
        .getHeaders()
        .forEach(
            (name, value) -> {
              if (!JWSHeader.getRegisteredParameterNames().contains(name)) {
                customHeaders.put(name, value);
              }
            });
    if (!customHeaders.isEmpty()) {
      builder.customParams(customHeaders);
    }

    return builder.build();
  }

  private static JWTClaimsSet convert(JwtClaimsSet claims) {
    JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

    // NOTE: The value of the 'iss' claim is a String or URL (StringOrURI).
    Object issuer = claims.getClaim(JwtClaimNames.ISS);
    if (issuer != null) {
      builder.issuer(issuer.toString());
    }

    String subject = claims.getSubject();
    if (StringUtils.hasText(subject)) {
      builder.subject(subject);
    }

    List<String> audience = claims.getAudience();
    if (!CollectionUtils.isEmpty(audience)) {
      builder.audience(audience);
    }

    Instant expiresAt = claims.getExpiresAt();
    if (expiresAt != null) {
      builder.expirationTime(Date.from(expiresAt));
    }

    Instant notBefore = claims.getNotBefore();
    if (notBefore != null) {
      builder.notBeforeTime(Date.from(notBefore));
    }

    Instant issuedAt = claims.getIssuedAt();
    if (issuedAt != null) {
      builder.issueTime(Date.from(issuedAt));
    }

    String jwtId = claims.getId();
    if (StringUtils.hasText(jwtId)) {
      builder.jwtID(jwtId);
    }

    Map<String, Object> customClaims = new HashMap<>();
    claims
        .getClaims()
        .forEach(
            (name, value) -> {
              if (!JWTClaimsSet.getRegisteredNames().contains(name)) {
                customClaims.put(name, value);
              }
            });
    if (!customClaims.isEmpty()) {
      customClaims.forEach(builder::claim);
    }

    return builder.build();
  }

  private static URI convertAsURI(String header, URL url) {
    try {
      return url.toURI();
    } catch (Exception ex) {
      throw new JwtEncodingException(
          String.format(
              ENCODING_ERROR_MESSAGE_TEMPLATE,
              "Unable to convert '" + header + "' JOSE header to a URI"),
          ex);
    }
  }
}
