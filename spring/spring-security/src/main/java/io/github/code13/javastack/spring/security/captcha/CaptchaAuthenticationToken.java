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

package io.github.code13.javastack.spring.security.captcha;

import java.io.Serial;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * CaptchaAuthenticationToken.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/10/2022 2:29 PM
 */
public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

  @Serial private static final long serialVersionUID = -3762501227561702153L;

  private final Object principal;
  private String captcha;

  public CaptchaAuthenticationToken(Object principal, String captcha) {
    super(null);
    this.principal = principal;
    this.captcha = captcha;
    setAuthenticated(false);
  }

  /**
   * 此构造函数用来初始化授信凭据.
   *
   * @param principal the principal
   * @param captcha the captcha
   * @param authorities the authorities
   */
  public CaptchaAuthenticationToken(
      Object principal, String captcha, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.captcha = captcha;
    super.setAuthenticated(true); // must use super, as we override
  }

  @Override
  public Object getCredentials() {
    return captcha;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) {
    if (isAuthenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list"
              + " instead");
    }
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    captcha = null;
  }
}
