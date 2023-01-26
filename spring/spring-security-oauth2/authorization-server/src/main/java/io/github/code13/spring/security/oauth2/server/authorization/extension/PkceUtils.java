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

package io.github.code13.spring.security.oauth2.server.authorization.extension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

/**
 * PkceUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/8 17:06
 */
public final class PkceUtils {

  public static String codeVerifier() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * 明文 + 摘要算法，生成 密文
   *
   * @see
   *     org.springframework.security.oauth2.server.authorization.authentication.CodeVerifierAuthenticator#codeVerifierValid(java.lang.String,
   *     java.lang.String, java.lang.String)
   */
  public static String calcCodeChallenge(String codeVerifier) {
    return calcCodeChallenge(codeVerifier, "S256");
  }

  /**
   * 明文 + 摘要算法，生成 密文
   *
   * @see
   *     org.springframework.security.oauth2.server.authorization.authentication.CodeVerifierAuthenticator#codeVerifierValid(java.lang.String,
   *     java.lang.String, java.lang.String)
   */
  public static String calcCodeChallenge(String codeVerifier, String codeChallengeMethod) {
    if ("S256".equals(codeChallengeMethod)) {
      byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);

      MessageDigest md;
      try {
        md = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException e) {
        // It is unlikely that SHA-256 is not available on the server. If it is not available,
        // there will likely be bigger issues as well. We default to SERVER_ERROR.
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
      }

      byte[] digest = md.digest(bytes);
      return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    return codeVerifier;
  }

  private PkceUtils() {
    // no instance
  }
}
