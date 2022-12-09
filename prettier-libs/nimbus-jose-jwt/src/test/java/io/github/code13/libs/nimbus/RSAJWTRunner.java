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

package io.github.code13.libs.nimbus;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.crypto.impl.RSAKeyUtils;
import com.nimbusds.jose.crypto.opts.AllowWeakRSAKey;
import com.nimbusds.jose.jwk.RSAKey;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * RSAJWTRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/9 8:55 PM
 */
public class RSAJWTRunner {

  //  keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks

  static RSAKey genRsaKey()
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
          JOSEException {
    // 这里优化到配置
    String path = "jwt.jks";
    String alias = "jwt";
    String pass = "123456";

    ClassPathResource resource = new ClassPathResource(path);
    KeyStore jks = KeyStore.getInstance("jks");
    char[] pin = pass.toCharArray();
    jks.load(resource.getInputStream(), pin);
    return RSAKey.load(jks, alias, pin);
  }

  @Test
  @DisplayName("testRsaSign")
  void testSign()
      throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException,
          JOSEException {
    RSAKey rsaKey = genRsaKey();

    JWSHeader jwsHeader = new Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build();

    Payload payload = new Payload("hello world");

    JWSObject jwsObject = new JWSObject(jwsHeader, payload);
    JWSSigner signer =
        new RSASSASigner(
            RSAKeyUtils.toRSAPrivateKey(rsaKey),
            Collections.singleton(AllowWeakRSAKey.getInstance()));

    jwsObject.sign(signer);

    String jwt = jwsObject.serialize();

    System.out.println(jwt);
  }

  @Test
  @DisplayName("testParse")
  void testParse()
      throws ParseException, CertificateException, KeyStoreException, IOException,
          NoSuchAlgorithmException, JOSEException {
    String token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.aGVsbG8gd29ybGQ.Hxi7ZpzuQBeuUwgHx5nv3kIyP9U1P4mvK9YDdYN7NgIGNlWOiF4HhpYzRaMqOF8RlxCy-mTkEZ-wlSFJ64p_xfVWWVo0HMzu9hwl_d7J9cj54SRanXg3sdMxAsC3VzHxgy9ACCWAjlRBWsvbrziq-5bQi_YFR7XaVEHXr8Okk0kbonn1QvRtew6Qh3fxjdcdCf5cF9WpWPSbZlL82e1lgIQealD_RMDJmEoVl2u7WikJ1vcHx_0YlIRQrspKdkWogFZx8OywTbEWJjkMs72imM91FxA3eJMuaPn0ZYgUMdV-3svqdEkJSk_JfRGdWN0IJPzabE1vD5FvlR4MlhP8DQ";

    JWSObject jwsObject = JWSObject.parse(token);

    RSAKey rsaKey = genRsaKey();
    RSAKey publicJWK = rsaKey.toPublicJWK();
    System.out.println(publicJWK);

    RSASSAVerifier verifier = new RSASSAVerifier(publicJWK);

    System.out.println(jwsObject.verify(verifier));

    System.out.println(jwsObject.getPayload().toString());
  }
}
