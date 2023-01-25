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

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * JweEncoderParameters.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:59
 */
public class JweEncoderParameters {

  private final JweHeader jweHeader;

  private final JwtClaimsSet claims;

  private JweEncoderParameters(JweHeader jweHeader, JwtClaimsSet claims) {
    this.jweHeader = jweHeader;
    this.claims = claims;
  }

  /**
   * Returns a new {@link JweEncoderParameters}, initialized with the provided {@link JwtClaimsSet}.
   *
   * @param claims the {@link JwtClaimsSet}
   * @return the {@link JweEncoderParameters}
   */
  public static JweEncoderParameters from(JwtClaimsSet claims) {
    Assert.notNull(claims, "claims cannot be null");
    return new JweEncoderParameters(null, claims);
  }

  /**
   * Returns a new {@link JweEncoderParameters}, initialized with the provided {@link JweHeader} and
   * {@link JwtClaimsSet}.
   *
   * @param JweHeader the {@link JweHeader}
   * @param claims the {@link JwtClaimsSet}
   * @return the {@link JweEncoderParameters}
   */
  public static JweEncoderParameters from(JweHeader jweHeader, JwtClaimsSet claims) {
    Assert.notNull(jweHeader, "JweHeader cannot be null");
    Assert.notNull(claims, "claims cannot be null");
    return new JweEncoderParameters(jweHeader, claims);
  }

  /**
   * Returns the {@link JweHeader JWE headers}.
   *
   * @return the {@link JweHeader}, or {@code null} if not specified
   */
  @Nullable
  public JweHeader getJweHeader() {
    return jweHeader;
  }

  /**
   * Returns the {@link JwtClaimsSet claims}.
   *
   * @return the {@link JwtClaimsSet}
   */
  public JwtClaimsSet getClaims() {
    return claims;
  }
}
