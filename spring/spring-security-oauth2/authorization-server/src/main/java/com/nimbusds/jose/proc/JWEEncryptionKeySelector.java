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

package com.nimbusds.jose.proc;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyConverter;
import com.nimbusds.jose.jwk.source.JWKSource;
import java.security.Key;
import java.security.PublicKey;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.SecretKey;
import net.jcip.annotations.ThreadSafe;

/**
 * JWEEncryptionKeySelector.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/20 10:28
 */
@ThreadSafe
public class JWEEncryptionKeySelector<C extends SecurityContext>
    extends AbstractJWKSelectorWithSource<C> implements JWEKeySelector<C> {

  /** The expected JWE algorithm. */
  private final JWEAlgorithm jweAlg;

  /** The expected JWE encryption method. */
  private final EncryptionMethod jweEnc;

  /**
   * Creates a new decryption key selector.
   *
   * @param jweAlg The expected JWE algorithm for the objects to be decrypted. Must not be {@code
   *     null}.
   * @param jweEnc The expected JWE encryption method for the objects to be decrypted. Must not be
   *     {@code null}.
   * @param jwkSource The JWK source. Must include the private keys and must not be {@code null}.
   */
  public JWEEncryptionKeySelector(
      JWEAlgorithm jweAlg, EncryptionMethod jweEnc, JWKSource<C> jwkSource) {
    super(jwkSource);
    if (jweAlg == null) {
      throw new IllegalArgumentException("The JWE algorithm must not be null");
    }
    this.jweAlg = jweAlg;
    if (jweEnc == null) {
      throw new IllegalArgumentException("The JWE encryption method must not be null");
    }
    this.jweEnc = jweEnc;
  }

  /**
   * Returns the expected JWE algorithm.
   *
   * @return The expected JWE algorithm.
   */
  public JWEAlgorithm getExpectedJWEAlgorithm() {
    return jweAlg;
  }

  /**
   * The expected JWE encryption method.
   *
   * @return The expected JWE encryption method.
   */
  public EncryptionMethod getExpectedJWEEncryptionMethod() {
    return jweEnc;
  }

  /**
   * Creates a JWK matcher for the expected JWE algorithms and the specified JWE header.
   *
   * @param jweHeader The JWE header. Must not be {@code null}.
   * @return The JWK matcher, {@code null} if none could be created.
   */
  protected JWKMatcher createJWKMatcher(JWEHeader jweHeader) {

    if (!getExpectedJWEAlgorithm().equals(jweHeader.getAlgorithm())) {
      return null;
    }

    if (!getExpectedJWEEncryptionMethod().equals(jweHeader.getEncryptionMethod())) {
      return null;
    }

    return JWKMatcher.forJWEHeader(jweHeader);
  }

  @Override
  public List<? extends Key> selectJWEKeys(JWEHeader jweHeader, C context)
      throws KeySourceException {
    if (!jweAlg.equals(jweHeader.getAlgorithm())
        || !jweEnc.equals(jweHeader.getEncryptionMethod())) {
      // Unexpected JWE alg or enc
      return Collections.emptyList();
    }

    JWKMatcher jwkMatcher = createJWKMatcher(jweHeader);
    List<JWK> jwkMatches = getJWKSource().get(new JWKSelector(jwkMatcher), context);
    List<Key> sanitizedKeyList = new LinkedList<>();

    for (Key key : KeyConverter.toJavaKeys(jwkMatches)) {
      if (key instanceof PublicKey || key instanceof SecretKey) {
        sanitizedKeyList.add(key);
      } // skip public keys
    }

    return sanitizedKeyList;
  }
}
