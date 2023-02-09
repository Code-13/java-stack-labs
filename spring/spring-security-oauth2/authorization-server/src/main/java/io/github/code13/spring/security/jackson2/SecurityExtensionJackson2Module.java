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

package io.github.code13.spring.security.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.code13.spring.security.sms.SmsAuthenticationToken;
import java.io.Serial;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * SecurityExtensionJackson2Module.
 *
 * <p>{@link org.springframework.security.jackson2.SecurityJackson2Modules}
 *
 * @see org.springframework.security.jackson2.SecurityJackson2Modules
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/2/7 12:14
 */
public class SecurityExtensionJackson2Module extends SimpleModule {

  @Serial private static final long serialVersionUID = -2580880647131380081L;

  public SecurityExtensionJackson2Module() {
    super(SecurityExtensionJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
  }

  @Override
  public void setupModule(SetupContext context) {
    SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
    context.setMixInAnnotations(SmsAuthenticationToken.class, SmsAuthenticationTokenMixin.class);
  }
}
