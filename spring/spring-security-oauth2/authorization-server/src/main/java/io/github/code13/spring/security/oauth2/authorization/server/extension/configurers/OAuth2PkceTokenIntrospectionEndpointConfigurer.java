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

package io.github.code13.spring.security.oauth2.authorization.server.extension.configurers;

import io.github.code13.spring.security.oauth2.authorization.server.extension.token.pkce.introspection.OAuth2PkceTokenIntrospectionEndpointFilter;
import io.github.code13.spring.security.oauth2.authorization.server.extension.token.pkce.introspection.PublicClientAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * OAuth2PkceTokenIntrospectionEndpointConfigurer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/11 20:33
 */
public class OAuth2PkceTokenIntrospectionEndpointConfigurer extends AbstractOAuth2Configurer {
  private RequestMatcher requestMatcher;

  protected OAuth2PkceTokenIntrospectionEndpointConfigurer(
      ObjectPostProcessor<Object> objectPostProcessor) {
    super(objectPostProcessor);
  }

  @Override
  void init(HttpSecurity http) {
    requestMatcher =
        new AntPathRequestMatcher(
            OAuth2PkceTokenIntrospectionEndpointFilter.DEFAULT_TOKEN_INTROSPECTION_ENDPOINT_URI);

    RegisteredClientRepository registeredClientRepository =
        OAuth2ConfigurerUtils.getRegisteredClientRepository(http);

    PublicClientAuthenticationProvider publicClientAuthenticationProvider =
        new PublicClientAuthenticationProvider(registeredClientRepository);
    http.authenticationProvider(postProcess(publicClientAuthenticationProvider));
  }

  @Override
  void configure(HttpSecurity http) {
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    OAuth2PkceTokenIntrospectionEndpointFilter introspectionEndpointFilter =
        new OAuth2PkceTokenIntrospectionEndpointFilter(authenticationManager);

    http.addFilterAfter(
        postProcess(introspectionEndpointFilter), AbstractPreAuthenticatedProcessingFilter.class);
  }

  @Override
  public RequestMatcher getRequestMatcher() {
    return requestMatcher;
  }
}
