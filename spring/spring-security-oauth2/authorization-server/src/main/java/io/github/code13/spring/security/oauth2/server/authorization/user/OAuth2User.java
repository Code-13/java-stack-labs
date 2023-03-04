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

package io.github.code13.spring.security.oauth2.server.authorization.user;

import java.util.Collection;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * OAuth2User.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/3/4 17:48
 */
public class OAuth2User implements UserDetails, CredentialsContainer {
  @Override public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
  @Override public String getPassword() {
    return null;
  }
  @Override public String getUsername() {
    return null;
  }
  @Override public boolean isAccountNonExpired() {
    return false;
  }
  @Override public boolean isAccountNonLocked() {
    return false;
  }
  @Override public boolean isCredentialsNonExpired() {
    return false;
  }
  @Override public boolean isEnabled() {
    return false;
  }

  @Override
  public void eraseCredentials() {}
}
