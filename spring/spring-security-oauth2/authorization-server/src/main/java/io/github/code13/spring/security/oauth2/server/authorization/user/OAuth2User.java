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

  private final String id;

  private final String username;

  private String password;

  private final String phone;

  /** 账户是否过期,过期无法验证 */
  private final boolean accountNonExpired;

  /** 指定用户是否被锁定或者解锁,锁定的用户无法进行身份验证 */
  private final boolean accountNonLocked;

  /** 指示是否已过期的用户的凭据(密码),过期的凭据防止认证 */
  private final boolean credentialsNonExpired;

  /** 是否被禁用,禁用的用户不能身份验证 */
  private final boolean enabled;

  private final Collection<? extends GrantedAuthority> authorities;

  private OAuth2User(Builder builder) {
    id = builder.id;
    username = builder.username;
    password = builder.password;
    phone = builder.phone;
    accountNonExpired = builder.accountNonExpired;
    accountNonLocked = builder.accountNonLocked;
    credentialsNonExpired = builder.credentialsNonExpired;
    enabled = builder.enabled;
    authorities = builder.authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public String getId() {
    return id;
  }

  public String getPhone() {
    return phone;
  }

  @Override
  public void eraseCredentials() {
    password = null;
  }

  // ==============================
  //      builder start
  // ==============================

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private String id;
    private String username;
    private String password;
    private String phone;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    private Builder() {}

    public Builder withId(String val) {
      id = val;
      return this;
    }

    public Builder withUsername(String val) {
      username = val;
      return this;
    }

    public Builder withPassword(String val) {
      password = val;
      return this;
    }

    public Builder withPhone(String val) {
      phone = val;
      return this;
    }

    public Builder withAccountNonExpired(boolean val) {
      accountNonExpired = val;
      return this;
    }

    public Builder withAccountNonLocked(boolean val) {
      accountNonLocked = val;
      return this;
    }

    public Builder withCredentialsNonExpired(boolean val) {
      credentialsNonExpired = val;
      return this;
    }

    public Builder withEnabled(boolean val) {
      enabled = val;
      return this;
    }

    public Builder withAuthorities(Collection<? extends GrantedAuthority> val) {
      authorities = val;
      return this;
    }

    public OAuth2User build() {
      return new OAuth2User(this);
    }
  }

  // ==============================
  //      builder end
  // ==============================

}
