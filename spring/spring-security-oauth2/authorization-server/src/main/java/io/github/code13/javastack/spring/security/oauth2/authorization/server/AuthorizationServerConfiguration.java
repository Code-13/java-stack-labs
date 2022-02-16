/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.spring.security.oauth2.authorization.server;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * AuthorizationServerConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 20:14
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

  @Bean
  SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer<HttpSecurity> configurer =
        new OAuth2AuthorizationServerConfigurer<>();

    RequestMatcher authorizationServerEndpointsMatcher = configurer.getEndpointsMatcher();

    http.requestMatcher(authorizationServerEndpointsMatcher)
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerEndpointsMatcher))
        .formLogin()
        .and()
        .apply(configurer);

    return http.build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
    //    生产上 注册客户端需要使用接口 不应该采用下面的方式
    RegisteredClient registeredClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            // 客户端ID和密码
            .clientId("felord-client")
            .clientSecret("{noop}secret")
            .clientName("felord")
            // 授权方法
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            // 授权类型
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            // 回调地址名单，不在此列将被拒绝 而且只能使用IP或者域名  不能使用 localhost
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/felord-auth")
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/felord-oidc")
            //            .redirectUri("http://127.0.0.1:8080/authorized")
            //            .redirectUri("http://127.0.0.1:8080/foo/bar")
            //            .redirectUri("https://www.baidu.com")
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
  JWKSource<SecurityContext> jwkSource()
      throws KeyStoreException, IOException, JOSEException, CertificateException,
          NoSuchAlgorithmException {
    // 这里优化到配置
    String path = "felordcn.jks";
    String alias = "felordcn";
    String pass = "123456";

    ClassPathResource resource = new ClassPathResource(path);
    KeyStore jks = KeyStore.getInstance("jks");
    char[] pin = pass.toCharArray();
    jks.load(resource.getInputStream(), pin);
    RSAKey rsaKey = RSAKey.load(jks, alias, pin);

    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  ProviderSettings providerSettings(@Value("${server.port}") Integer port) {
    return ProviderSettings.builder().issuer("http://localhost:" + port).build();
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
