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

import java.util.Map;
import org.springframework.security.oauth2.jose.JWEAlgorithm;
import org.springframework.util.Assert;

/**
 * JweHeader.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:37
 */
public final class JweHeader extends JoseHeader {

  private JweHeader(Map<String, Object> headers) {
    super(headers);
  }

  @SuppressWarnings("unchecked")
  @Override
  public JWEAlgorithm getAlgorithm() {
    return super.getAlgorithm();
  }

  /**
   * Returns a new {@link JweHeader.Builder}, initialized with the provided {@link JWEAlgorithm}.
   *
   * @param jweAlgorithm the {@link JWEAlgorithm}
   * @return the {@link JweHeader.Builder}
   */
  public static JweHeader.Builder with(JWEAlgorithm jweAlgorithm) {
    return new JweHeader.Builder(jweAlgorithm);
  }

  /**
   * Returns a new {@link JweHeader.Builder}, initialized with the provided {@code headers}.
   *
   * @param headers the headers
   * @return the {@link JweHeader.Builder}
   */
  public static JweHeader.Builder from(JweHeader headers) {
    return new JweHeader.Builder(headers);
  }

  /** A builder for {@link JweHeader}. */
  public static final class Builder
      extends JoseHeader.AbstractBuilder<JweHeader, JweHeader.Builder> {

    private Builder(JWEAlgorithm jweAlgorithm) {
      Assert.notNull(jweAlgorithm, "JWEAlgorithm cannot be null");
      algorithm(jweAlgorithm);
    }

    private Builder(JweHeader headers) {
      Assert.notNull(headers, "headers cannot be null");
      getHeaders().putAll(headers.getHeaders());
    }

    /**
     * Builds a new {@link JweHeader}.
     *
     * @return a {@link JweHeader}
     */
    @Override
    public JweHeader build() {
      return new JweHeader(getHeaders());
    }
  }
}
