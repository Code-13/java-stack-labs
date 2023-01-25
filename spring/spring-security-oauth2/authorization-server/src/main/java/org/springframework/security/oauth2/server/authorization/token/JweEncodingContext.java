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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.jwt.JweHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.util.Assert;

/**
 * JweEncodingContext.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:53
 */
public class JweEncodingContext implements OAuth2TokenContext {

  private final Map<Object, Object> context;

  private JweEncodingContext(Map<Object, Object> context) {
    this.context = Collections.unmodifiableMap(new HashMap<>(context));
  }

  @SuppressWarnings("unchecked")
  @Nullable
  @Override
  public <V> V get(Object key) {
    return hasKey(key) ? (V) context.get(key) : null;
  }

  @Override
  public boolean hasKey(Object key) {
    Assert.notNull(key, "key cannot be null");
    return context.containsKey(key);
  }

  /**
   * Returns the {@link JweHeader.Builder JWS headers} allowing the ability to add, replace, or
   * remove.
   *
   * @return the {@link JweHeader.Builder}
   */
  public JweHeader.Builder getJweHeader() {
    return get(JweHeader.Builder.class);
  }

  /**
   * Returns the {@link JwtClaimsSet.Builder claims} allowing the ability to add, replace, or
   * remove.
   *
   * @return the {@link JwtClaimsSet.Builder}
   */
  public JwtClaimsSet.Builder getClaims() {
    return get(JwtClaimsSet.Builder.class);
  }

  /**
   * Constructs a new {@link JwtEncodingContext.Builder} with the provided JWS headers and claims.
   *
   * @param jweHeaderBuilder the JWS headers to initialize the builder
   * @param claimsBuilder the claims to initialize the builder
   * @return the {@link JwtEncodingContext.Builder}
   */
  public static JweEncodingContext.Builder with(
      JweHeader.Builder jweHeaderBuilder, JwtClaimsSet.Builder claimsBuilder) {
    return new JweEncodingContext.Builder(jweHeaderBuilder, claimsBuilder);
  }

  /** A builder for {@link JwtEncodingContext}. */
  public static final class Builder
      extends AbstractBuilder<JweEncodingContext, JweEncodingContext.Builder> {

    private Builder(JweHeader.Builder jweHeaderBuilder, JwtClaimsSet.Builder claimsBuilder) {
      Assert.notNull(jweHeaderBuilder, "jweHeaderBuilder cannot be null");
      Assert.notNull(claimsBuilder, "claimsBuilder cannot be null");
      put(JweHeader.Builder.class, jweHeaderBuilder);
      put(JwtClaimsSet.Builder.class, claimsBuilder);
    }

    /**
     * Builds a new {@link JwtEncodingContext}.
     *
     * @return the {@link JwtEncodingContext}
     */
    @Override
    public JweEncodingContext build() {
      return new JweEncodingContext(getContext());
    }
  }
}
