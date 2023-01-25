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

package org.springframework.security.oauth2.jwt;

/**
 * JweEncoder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/19 15:03
 */
public interface JweEncoder {

  /**
   * Encode the JWT to it's compact claims representation format.
   *
   * @param parameters the parameters containing the JOSE header and JWT Claims Set
   * @return a {@link Jwt}
   * @throws JwtEncodingException if an error occurs while attempting to encode the JWT
   */
  Jwt encode(JweEncoderParameters parameters) throws JwtEncodingException;
}
