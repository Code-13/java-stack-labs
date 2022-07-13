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

package io.github.code13.javastack.libs.nimbus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.security.SecureRandom;
import java.text.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JwtRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/3/9 8:13 PM
 */
public class JwtRunner {

  // 加密

  @Test
  @DisplayName("testHeader")
  void testHeader() {

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

    System.out.println(header.getParsedBase64URL());
  }

  @Test
  @DisplayName("testPayload")
  void testPayload() {

    Payload payload = new Payload("Hello World");

    System.out.println(payload.toBase64URL());
  }

  @Test
  @DisplayName("testSignerAndJWSObject")
  void testSignerAndJWSObject() throws JOSEException {

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
    Payload payload = new Payload("Hello World");
    JWSObject jwsObject = new JWSObject(header, payload);

    JWSSigner jwsSigner =
        new MACSigner("123456789012345678901234567890123456789012345678901234567890");
    jwsObject.sign(jwsSigner);

    String token = jwsObject.serialize();

    System.out.println(token);
  }

  // 解密

  @Test
  @DisplayName("testParse")
  void testParse() throws ParseException {
    JWSObject jwsObject =
        JWSObject.parse(
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.SGVsbG8gV29ybGQ.PahyoaOITXr20BFNFBI1YherhrG-39LA-ai629CaMS8");

    JWSHeader header = jwsObject.getHeader();
    Payload payload = jwsObject.getPayload();

    System.out.println(header.toJSONObject());
    System.out.println(payload.toJSONObject());
  }

  @Test
  @DisplayName("testVerifier")
  void testVerifier() throws JOSEException {

    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
    Payload payload = new Payload("Hello World");
    JWSObject jwsObject = new JWSObject(header, payload);

    String secret = "123456789012345678901234567890123456789012345678901234567890";

    JWSSigner jwsSigner = new MACSigner(secret);
    jwsObject.sign(jwsSigner);

    JWSVerifier jwsVerifier = new MACVerifier(secret);

    System.out.println(jwsObject.verify(jwsVerifier));
  }

  @Test
  @DisplayName("官网的 HS256 示例")
  void testFull() throws JOSEException, ParseException {
    // Generate random 256-bit (32-byte) shared secret
    SecureRandom random = new SecureRandom();
    byte[] sharedSecret = new byte[32];
    random.nextBytes(sharedSecret);

    // Create HMAC signer
    JWSSigner signer = new MACSigner(sharedSecret);

    // Prepare JWS object with "Hello, world!" payload
    JWSObject jwsObject =
        new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("Hello, world!"));

    // Apply the HMAC
    jwsObject.sign(signer);

    // To serialize to compact form, produces something like
    // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
    String s = jwsObject.serialize();

    // To parse the JWS and verify it, e.g. on client-side
    jwsObject = JWSObject.parse(s);

    JWSVerifier verifier = new MACVerifier(sharedSecret);

    assertTrue(jwsObject.verify(verifier));

    assertEquals("Hello, world!", jwsObject.getPayload().toString());
  }
}
