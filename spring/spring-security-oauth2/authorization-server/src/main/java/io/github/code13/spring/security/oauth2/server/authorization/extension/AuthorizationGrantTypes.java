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

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * extensions for {@link AuthorizationGrantType}. use for {@code AuthorizationGrantType} extension.
 *
 * @see AuthorizationGrantType
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 20:08
 */
public final class AuthorizationGrantTypes {

  /**
   * @deprecated use pkce instead
   */
  @Deprecated(forRemoval = false)
  public static final AuthorizationGrantType IMPLICIT = new AuthorizationGrantType("implicit");

  public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");

  public static final AuthorizationGrantType SMS = new AuthorizationGrantType("sms");

  private AuthorizationGrantTypes() {
    // no instance
  }
}
