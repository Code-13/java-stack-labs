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

import java.io.Serial;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

/**
 * OAuth2ResourceOwnerAuthenticationToken.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/1 21:18
 */
public abstract class OAuth2ResourceOwnerAuthenticationToken extends AbstractAuthenticationToken {

  @Serial private static final long serialVersionUID = -2431803107147115583L;

  private final AuthorizationGrantType authorizationGrantType;
  private final Authentication clientPrincipal;
  private final Set<String> scopes;
  private final Map<String, Object> additionalParameters;

  protected OAuth2ResourceOwnerAuthenticationToken(
      AuthorizationGrantType authorizationGrantType,
      Authentication clientPrincipal,
      Set<String> scopes,
      Map<String, Object> additionalParameters) {
    super(Collections.emptyList());
    Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
    Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
    this.authorizationGrantType = authorizationGrantType;
    this.clientPrincipal = clientPrincipal;
    this.scopes =
        Collections.unmodifiableSet(
            scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    this.additionalParameters =
        Collections.unmodifiableMap(
            additionalParameters != null
                ? new HashMap<>(additionalParameters)
                : Collections.emptyMap());
  }

  @Override
  public Object getCredentials() {
    return "";
  }

  @Override
  public Object getPrincipal() {
    return clientPrincipal;
  }

  public AuthorizationGrantType getAuthorizationGrantType() {
    return authorizationGrantType;
  }

  public Authentication getClientPrincipal() {
    return clientPrincipal;
  }

  public Set<String> getScopes() {
    return scopes;
  }

  public Map<String, Object> getAdditionalParameters() {
    return additionalParameters;
  }
}
