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

package io.github.code13.spring.security.oauth2.server.authorization.extension;

import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ErrorCodesExtension.SealedOAuth2ErrorCodesExtension;

/**
 * OAuth2ErrorCodesExtension.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 17:33
 */
public sealed interface OAuth2ErrorCodesExtension permits SealedOAuth2ErrorCodesExtension {

  String SCOPE_IS_EMPTY = "scope_is_empty";

  final class SealedOAuth2ErrorCodesExtension implements OAuth2ErrorCodesExtension {
    private SealedOAuth2ErrorCodesExtension() {
      // no instance
    }
  }
}
