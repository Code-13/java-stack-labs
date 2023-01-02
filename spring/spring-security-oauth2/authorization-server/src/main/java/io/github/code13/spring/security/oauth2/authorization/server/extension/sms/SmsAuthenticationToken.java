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

package io.github.code13.spring.security.oauth2.authorization.server.extension.sms;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * SmsAuthenticationToken.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 19:33
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

  @Serial private static final long serialVersionUID = 1485804859841911266L;

  private final Object principal;

  private Object credentials;

  /**
   * Creates a token with the supplied array of authorities.
   *
   * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented
   *     by this authentication object.
   */
  public SmsAuthenticationToken(Object principal, Object credentials) {
    super(Collections.emptyList());
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  public SmsAuthenticationToken(
      Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> grantedAuthorities) {
    super(grantedAuthorities);
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(true);
  }

  public static SmsAuthenticationToken unauthenticated(Object principal, Object credentials) {
    return new SmsAuthenticationToken(principal, credentials);
  }

  public static SmsAuthenticationToken authenticated(
      Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> grantedAuthorities) {
    return new SmsAuthenticationToken(principal, credentials, grantedAuthorities);
  }

  @Override
  public Object getCredentials() {
    return "";
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
