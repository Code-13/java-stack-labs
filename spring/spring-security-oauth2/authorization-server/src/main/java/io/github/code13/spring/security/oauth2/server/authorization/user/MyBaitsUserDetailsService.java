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

import java.util.Collections;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * MyBaitsUserDetailsManager.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/3/4 19:02
 */
public class MyBaitsUserDetailsService implements UserDetailsService {

  protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  private final UserMapper userMapper;

  public MyBaitsUserDetailsService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = userMapper.queryChain().eq(User::getUsername, username).oneOpt();

    if (userOpt.isEmpty()) {
      throw new UsernameNotFoundException(
          this.messages.getMessage(
              "JdbcDaoImpl.notFound", new Object[] {username}, "Username {0} not found"));
    }

    // TODO: 加载权限等。。。

    return convert(userOpt.get());
  }

  OAuth2User convert(User user) {
    return OAuth2User.newBuilder()
        .withId(user.getId())
        .withUsername(user.getUsername())
        .withPassword(user.getPassword())
        .withPhone(user.getPhone())
        .withAccountNonExpired(user.isAccountNonExpired())
        .withAccountNonLocked(user.isAccountNonLocked())
        .withCredentialsNonExpired(user.isCredentialsNonExpired())
        .withEnabled(user.isEnabled())
        .withAuthorities(Collections.emptyList()) //
        .build();
  }

  public MyBaitsUserDetailsService setMessages(MessageSource messageSource) {
    this.messages = new MessageSourceAccessor(messageSource);
    return this;
  }
}
