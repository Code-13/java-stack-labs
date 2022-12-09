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

package io.github.code13.spring.security.commons.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * TokenGeneratorService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022 /9/26 21:49
 */
public final class JwtUtils {

  private JwtUtils() {
    // no instance
  }

  /**
   * Create token string.
   *
   * @param claims the claims
   * @param secret the secret
   * @param timeout the timeout
   * @return the string
   */
  public static String createToken(Map<String, Object> claims, String secret, Duration timeout) {
    JWSHeader jwsHeader =
        (new JWSHeader.Builder(JWSAlgorithm.HS256)).type(JOSEObjectType.JWT).build();
    Date exp = new Date(System.currentTimeMillis() + timeout.toMillis());

    JWTClaimsSet.Builder builder = (new JWTClaimsSet.Builder()).expirationTime(exp);

    claims.forEach(builder::claim);

    JWTClaimsSet claimsSet = builder.build();

    try {
      MACSigner macSigner = new MACSigner(secret);
      SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
      signedJWT.sign(macSigner);
      return signedJWT.serialize();
    } catch (JOSEException e) {
      throw new TokenException(e.getMessage(), e);
    }
  }

  /**
   * Parse token map.
   *
   * @param token the token
   * @param secret the secret
   * @return the map
   */
  public static Map<String, Object> parseToken(String token, String secret) {
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(secret);
      if (jwt.verify(verifier)) {
        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
        if (new Date().after(expirationTime)) {
          throw new TokenException("The token had expired");
        } else {
          return jwt.getJWTClaimsSet().getClaims();
        }
      } else {
        throw new TokenException("The token is invalid");
      }

    } catch (JOSEException | ParseException e) {
      throw new TokenException(e.getMessage(), e);
    }
  }

  /**
   * Verify token boolean.
   *
   * @param token the token
   * @param secret the secret
   * @return the boolean
   */
  public static boolean verifyToken(String token, String secret) {
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(secret);
      if (!jwt.verify(verifier)) {
        return false;
      } else {
        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
        return !(new Date()).after(expirationTime);
      }
    } catch (ParseException | JOSEException e) {
      throw new TokenException(e.getMessage(), e);
    }
  }
}
