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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * JwksConfiguration.
 *
 * <pre>
 * - `JWT`：指的是 `JSON Web Token`，由 `header.payload.signature` 组成。不存在签名的 `JWT` 是不安全的，存在签名的 `JWT` 是不可窜改的。
 * - `JWS`：指的是签过名的 `JWT`，即拥有签名的 `JWT`。
 * - `JWK`：既然涉及到签名，就涉及到签名算法，对称加密还是非对称加密，那么就需要加密的 密钥或者公私钥对。此处我们将 `JWT` 的密钥或者公私钥对统一称为 `JSON WEB KEY`，即 `JWK`。
 * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/31 13:49
 */
@Configuration
class JwksConfiguration {

  @Bean
  JWKSource<SecurityContext> jwkSource()
      throws KeyStoreException, IOException, JOSEException, CertificateException,
          NoSuchAlgorithmException {
    // 这里优化到配置
    String path = "jwt.jks";
    String alias = "authorization-server";
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
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

  /**
   * OAuth2TokenCustomizer for JWT
   *
   * <p><a
   * href="https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html#customize-user-info">customize-user-info</a>
   */
  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
    return context -> {
      if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
        return;
      }
      if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
        context
            .getClaims()
            .claims(
                claims -> {
                  claims.put("user_info", context.getPrincipal().getPrincipal());
                  claims.put("测试字段", "这是一个测试字段");
                });
      }
    };
  }
}
