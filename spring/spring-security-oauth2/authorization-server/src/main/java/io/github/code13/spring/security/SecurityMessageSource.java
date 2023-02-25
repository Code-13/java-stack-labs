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

package io.github.code13.spring.security;

import java.nio.charset.StandardCharsets;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * SecurityMessageSource.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/2/9 17:42
 */
public final class SecurityMessageSource extends ResourceBundleMessageSource {

  private static final MessageSourceAccessor messageSourceAccessor =
      new MessageSourceAccessor(new SecurityMessageSource());

  public SecurityMessageSource() {
    setBasename("io.github.code13.spring.security.messages");
    setDefaultEncoding(StandardCharsets.UTF_8.displayName());
  }

  public static MessageSourceAccessor getAccessor() {
    return messageSourceAccessor;
  }
}
