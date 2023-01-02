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

import java.util.Collection;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * SmsAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 21:49
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
  private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  private final SmsService smsService;
  private final SmsUserDetailsService smsUserDetailsService;

  public SmsAuthenticationProvider(
      SmsService smsService, SmsUserDetailsService smsUserDetailsService) {
    this.smsService = smsService;
    this.smsUserDetailsService = smsUserDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    Assert.isInstanceOf(
        SmsAuthenticationToken.class,
        authentication,
        () ->
            messages.getMessage(
                "SmsAuthenticationProvider.onlySupports",
                "Only SmsAuthenticationToken is supported"));

    SmsAuthenticationToken unAuthenticationToken = (SmsAuthenticationToken) authentication;

    String phone = (String) unAuthenticationToken.getPrincipal();
    String code = (String) unAuthenticationToken.getCredentials();

    if (!smsService.verifyCode(phone, code)) {
      throw new BadCredentialsException("sms is not matched");
    }

    UserDetails userDetails = smsUserDetailsService.loadUserByPhone(phone);

    return createSuccessAuthentication(authentication, userDetails);
  }

  private Authentication createSuccessAuthentication(
      Authentication authentication, UserDetails userDetails) {
    Collection<? extends GrantedAuthority> grantedAuthorities =
        authoritiesMapper.mapAuthorities(userDetails.getAuthorities());
    SmsAuthenticationToken token =
        SmsAuthenticationToken.authenticated(userDetails, null, grantedAuthorities);
    token.setDetails(authentication.getDetails());
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SmsAuthenticationToken.class.isAssignableFrom(authentication);
  }

  public void setMessages(MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }
}
