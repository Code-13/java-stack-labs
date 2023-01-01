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

package io.github.code13.spring.security.oauth2.authorization.server.config;

import io.github.code13.spring.security.oauth2.authorization.server.extension.password.OAuth2PasswordAuthenticationConverter;
import io.github.code13.spring.security.oauth2.authorization.server.extension.password.OAuth2PasswordAuthenticationProvider;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * AuthorizationServerConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 20:14
 */
@Configuration
class AuthorizationServerConfiguration {

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        new OAuth2AuthorizationServerConfigurer();
    RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

    http.requestMatcher(endpointsMatcher)
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
        .apply(authorizationServerConfigurer)
        .tokenEndpoint(
            tokenEndpoint ->
                // 注入自定义的授权认证Converter
                tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()));

    // Enable OpenID Connect 1.0
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

    // @formatter:off
    http.exceptionHandling(
        exceptions ->
            exceptions.authenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint("/login.html"))); // 此处可直接跳转至登录页面，也可中转
    // new LoginUrlAuthenticationEntryPoint("/login/oauth2")
    // @formatter:on

    http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

    SecurityFilterChain securityFilterChain = http.build();

    // 注入自定义授权模式实现
    // 必须在 HttpSecurity.build 之后注入
    addCustomOAuth2GrantAuthenticationProvider(http);

    return securityFilterChain;
  }

  private AuthenticationConverter accessTokenRequestConverter() {
    return new DelegatingAuthenticationConverter(
        Arrays.asList(
            new OAuth2PasswordAuthenticationConverter(),
            new OAuth2RefreshTokenAuthenticationConverter(),
            new OAuth2ClientCredentialsAuthenticationConverter(),
            new OAuth2AuthorizationCodeAuthenticationConverter(),
            new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
  }

  private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
    AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
    OAuth2AuthorizationService authorizationService =
        http.getSharedObject(OAuth2AuthorizationService.class);
    OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
        http.getSharedObject(OAuth2TokenGenerator.class);

    OAuth2PasswordAuthenticationProvider oAuth2PasswordAuthenticationProvider =
        new OAuth2PasswordAuthenticationProvider(
            authorizationService, tokenGenerator, authenticationManager);

    // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
    http.authenticationProvider(oAuth2PasswordAuthenticationProvider);
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
    //    生产上 注册客户端需要使用接口 不应该采用下面的方式
    RegisteredClient registeredClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            // 客户端ID和密码
            .clientId("test-client")
            .clientSecret("{noop}secret")
            .clientName("test")
            // client 认证方法
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            // 授权类型 (implicit 和 password 均已被废弃)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
            // 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名  不能使用 localhost
            // 根据 Oauth2 标准，回调地址应该是 oauth2 client 端
            .redirectUri("http://127.0.0.1:9001/login/oauth2/code/iam")
            .redirectUri("http://127.0.0.1:9001/login/oauth2/code/iam-oidc")
            //            .redirectUri("http://127.0.0.1:9001/authorized")
            //            .redirectUri("http://127.0.0.1:9001/foo/bar")
            .redirectUri("https://www.baidu.com")
            .scope(OidcScopes.OPENID)
            .scope("message.read")
            .scope("message.write")
            .tokenSettings(TokenSettings.builder().build())
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

    // 每次都会初始化  生产的话 只初始化JdbcRegisteredClientRepository
    JdbcRegisteredClientRepository registeredClientRepository =
        new JdbcRegisteredClientRepository(jdbcTemplate);
    registeredClientRepository.save(registeredClient);

    return registeredClientRepository;
  }

  @Bean
  OAuth2AuthorizationService authorizationService(
      JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
  }

  @Bean
  OAuth2AuthorizationConsentService authorizationConsentService(
      JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
  }

  @Bean
  AuthorizationServerSettings providerSettings() {
    return AuthorizationServerSettings.builder().build();
  }

  @Bean
  EmbeddedDatabase embeddedDatabase() {
    return new EmbeddedDatabaseBuilder()
        .setName("authorization-server")
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addScript(
            "org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
        .addScript(
            "org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
        .addScript(
            "org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
        .build();
  }
}
