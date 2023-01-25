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

package org.springframework.security.oauth2.jose;

/**
 * JWEAlgorithm.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 14:23
 */
public record JWEAlgorithm(String name) implements JwaAlgorithm {

  /**
   * RSAES using Optimal Asymmetric Encryption Padding (OAEP) (RFC 3447), with the SHA-256 hash
   * function and the MGF1 with SHA-256 mask generation function.
   */
  public static final JWEAlgorithm RSA_OAEP_256 = new JWEAlgorithm("RSA-OAEP-256");

  /**
   * RSAES using Optimal Asymmetric Encryption Padding (OAEP) (RFC 3447), with the SHA-512 hash
   * function and the MGF1 with SHA-384 mask generation function.
   */
  public static final JWEAlgorithm RSA_OAEP_384 = new JWEAlgorithm("RSA-OAEP-384");

  /**
   * RSAES using Optimal Asymmetric Encryption Padding (OAEP) (RFC 3447), with the SHA-512 hash
   * function and the MGF1 with SHA-512 mask generation function.
   */
  public static final JWEAlgorithm RSA_OAEP_512 = new JWEAlgorithm("RSA-OAEP-512");

  /** Advanced Encryption Standard (AES) Key Wrap Algorithm (RFC 3394) using 128 bit keys. */
  public static final JWEAlgorithm A128KW = new JWEAlgorithm("A128KW");

  /** Advanced Encryption Standard (AES) Key Wrap Algorithm (RFC 3394) using 192 bit keys. */
  public static final JWEAlgorithm A192KW = new JWEAlgorithm("A192KW");

  /** Advanced Encryption Standard (AES) Key Wrap Algorithm (RFC 3394) using 256 bit keys. */
  public static final JWEAlgorithm A256KW = new JWEAlgorithm("A256KW");

  @Override
  public String getName() {
    return name;
  }
}
