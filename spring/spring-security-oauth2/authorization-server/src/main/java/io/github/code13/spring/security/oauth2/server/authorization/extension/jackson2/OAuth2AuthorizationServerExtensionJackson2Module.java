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

package io.github.code13.spring.security.oauth2.server.authorization.extension.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.code13.spring.security.oauth2.server.authorization.user.OAuth2User;
import java.io.Serial;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * OAuth2AuthorizationServerExtensionJackson2Module.
 *
 * <p>{@link
 * org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module}
 * {@link org.springframework.security.jackson2.SecurityJackson2Modules}
 *
 * @see
 *     org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
 * @see org.springframework.security.jackson2.SecurityJackson2Modules
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/3 16:11
 */
public class OAuth2AuthorizationServerExtensionJackson2Module extends SimpleModule {

  @Serial private static final long serialVersionUID = -6158869036683981674L;

  public OAuth2AuthorizationServerExtensionJackson2Module() {
    super(
        OAuth2AuthorizationServerExtensionJackson2Module.class.getName(),
        new Version(1, 0, 0, null, null, null));
  }

  @Override
  public void setupModule(SetupContext context) {
    SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
    context.setMixInAnnotations(OAuth2User.class, OAuth2UserMixin.class);
  }
}
