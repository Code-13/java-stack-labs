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

package io.github.code13.spring.boot.web.rsakey;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * RsaUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/2/1 19:40
 */
public class RsaUtils {

  public static final String KEY_RSA_TYPE = "RSA";

  public static final String ALGORITHM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

  public static KeyPair generateRsaKey() {
    KeyPair keyPair;
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA_TYPE);
      keyPairGenerator.initialize(2048);
      keyPair = keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
    return keyPair;
  }

  /**
   * RSA 加密
   *
   * @param sourceBase64RSA base64 编码后的密文
   * @param privateKeyBase64Str base64 编码后的私钥
   * @return 明文
   * @throws Exception 抛出异常
   */
  public static String encode(String source, String publicKeyBase64Str) throws Exception {
    byte[] privateBytes = Base64.getDecoder().decode(publicKeyBase64Str);
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
    PublicKey publicKey = keyFactory.generatePublic(pkcs8EncodedKeySpec);

    return encode(source, publicKey);
  }

  /**
   * RSA 加密
   *
   * @param sourceBase64RSA base64 编码后的密文
   * @param privateKeyBase64Str base64 编码后的私钥
   * @return 明文
   * @throws Exception 抛出异常
   */
  public static String encode(String source, PublicKey publicKey) throws Exception {
    Cipher oaepFromAlgo = Cipher.getInstance(ALGORITHM);
    oaepFromAlgo.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] ct = oaepFromAlgo.doFinal(source.getBytes(StandardCharsets.UTF_8));

    return Base64.getEncoder().encodeToString(ct);
  }

  /**
   * RSA 解密
   *
   * @param sourceBase64RSA base64 编码后的密文
   * @param privateKeyBase64Str base64 编码后的私钥
   * @return 明文
   * @throws Exception 抛出异常
   */
  public static String decode(String sourceBase64RSA, String privateKeyBase64Str) throws Exception {
    byte[] privateBytes = Base64.getDecoder().decode(privateKeyBase64Str);
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

    return decode(sourceBase64RSA, privateKey);
  }

  /**
   * RSA 解密
   *
   * @param sourceBase64RSA base64 编码后的密文
   * @param privateKeyBase64Str base64 编码后的私钥
   * @return 明文
   * @throws Exception 抛出异常
   */
  public static String decode(String sourceBase64RSA, PrivateKey privateKey) throws Exception {
    Cipher oaepFromInit = Cipher.getInstance(ALGORITHM);
    OAEPParameterSpec oaepParams =
        new OAEPParameterSpec(
            "SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);

    oaepFromInit.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);

    byte[] ct = Base64.getMimeDecoder().decode(sourceBase64RSA);
    byte[] pt = oaepFromInit.doFinal(ct);
    return new String(pt, StandardCharsets.UTF_8);
  }
}
