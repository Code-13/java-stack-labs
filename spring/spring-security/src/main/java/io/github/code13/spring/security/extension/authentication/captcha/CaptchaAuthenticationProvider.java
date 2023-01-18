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

package io.github.code13.spring.security.extension.authentication.captcha;

import java.util.Collection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
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
 * CaptchaAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/10/2022 2:37 PM
 */
public class CaptchaAuthenticationProvider
    implements AuthenticationProvider, InitializingBean, MessageSourceAware {

  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
  private final CaptchaUserDetailsService captchaUserDetailsService;
  private final CaptchaService captchaService;
  private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  public CaptchaAuthenticationProvider(
      CaptchaUserDetailsService captchaUserDetailsService, CaptchaService captchaService) {
    this.captchaUserDetailsService = captchaUserDetailsService;
    this.captchaService = captchaService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    Assert.isInstanceOf(
        CaptchaAuthenticationToken.class,
        authentication,
        () ->
            messages.getMessage(
                "CaptchaAuthenticationProvider.onlySupports",
                "Only CaptchaAuthenticationToken is supported"));

    CaptchaAuthenticationToken unAuthenticationToken = (CaptchaAuthenticationToken) authentication;

    String phone = (String) unAuthenticationToken.getPrincipal();
    String rawCode = (String) unAuthenticationToken.getCredentials();

    if (!captchaService.verifyCaptcha(phone, rawCode)) {
      throw new BadCredentialsException("captcha is not matched");
    }

    UserDetails userDetails = captchaUserDetailsService.loadUserByPhone(phone);

    return createSuccessAuthentication(authentication, userDetails);
  }

  private Authentication createSuccessAuthentication(
      Authentication authentication, UserDetails userDetails) {
    Collection<? extends GrantedAuthority> grantedAuthorities =
        authoritiesMapper.mapAuthorities(userDetails.getAuthorities());

    CaptchaAuthenticationToken token =
        new CaptchaAuthenticationToken(userDetails, null, grantedAuthorities);

    token.setDetails(authentication.getDetails());

    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(captchaUserDetailsService, "captchaUserDetailsService must not be null");
    Assert.notNull(captchaService, "captchaService must not be null");
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }
}
