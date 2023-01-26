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

import io.github.code13.spring.security.oauth2.server.authorization.extension.OAuth2ParameterNamesExtension.SealedOAuth2ParameterNamesExtension;

/**
 * OAuth2ParameterNamesExtension.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/2 18:19
 */
public sealed interface OAuth2ParameterNamesExtension permits SealedOAuth2ParameterNamesExtension {

  String SMS_PHONE_PARAMETER_NAME = "phone";

  String SMS_CODE_PARAMETER_NAME = "code";

  final class SealedOAuth2ParameterNamesExtension implements OAuth2ParameterNamesExtension {
    private SealedOAuth2ParameterNamesExtension() {
      // no instance
    }
  }
}
