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

package io.github.code13.spring.security.oauth2.authorization.server.extension.token.pkce.introspection;

import java.time.Instant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * PublicClientAuthenticationProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/11 20:24
 */
public class PublicClientAuthenticationProvider implements AuthenticationProvider {
  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";

  private final Log logger = LogFactory.getLog(getClass());
  private final RegisteredClientRepository registeredClientRepository;

  public PublicClientAuthenticationProvider(RegisteredClientRepository registeredClientRepository) {
    Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
    this.registeredClientRepository = registeredClientRepository;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    OAuth2ClientAuthenticationToken clientAuthentication =
        (OAuth2ClientAuthenticationToken) authentication;

    if (!ClientAuthenticationMethod.NONE.equals(
        clientAuthentication.getClientAuthenticationMethod())) {
      return null;
    }

    String clientId = clientAuthentication.getPrincipal().toString();
    RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
    if (registeredClient == null) {
      throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
    }

    if (logger.isTraceEnabled()) {
      logger.trace("Retrieved registered client");
    }

    if (!registeredClient
        .getClientAuthenticationMethods()
        .contains(clientAuthentication.getClientAuthenticationMethod())) {
      throwInvalidClient("authentication_method");
    }

    if (registeredClient.getClientSecretExpiresAt() != null
        && Instant.now().isAfter(registeredClient.getClientSecretExpiresAt())) {
      throwInvalidClient("client_secret_expires_at");
    }

    if (logger.isTraceEnabled()) {
      logger.trace("Validated client authentication parameters");
    }

    if (logger.isTraceEnabled()) {
      logger.trace("Public Client do not need Authenticated client secret");
    }

    return new OAuth2ClientAuthenticationToken(
        registeredClient,
        clientAuthentication.getClientAuthenticationMethod(),
        clientAuthentication.getCredentials());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private static void throwInvalidClient(String parameterName) {
    OAuth2Error error =
        new OAuth2Error(
            OAuth2ErrorCodes.INVALID_CLIENT,
            "Client authentication failed: " + parameterName,
            ERROR_URI);
    throw new OAuth2AuthenticationException(error);
  }
}
