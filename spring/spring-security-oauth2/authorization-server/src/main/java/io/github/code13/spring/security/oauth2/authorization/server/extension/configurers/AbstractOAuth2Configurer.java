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

package io.github.code13.spring.security.oauth2.authorization.server.extension.configurers;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * AbstractOAuth2Configurer.
 *
 * <p>copy from {@link
 * org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.AbstractOAuth2Configurer}
 *
 * @see
 *     org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.AbstractOAuth2Configurer
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/3 15:19
 */
abstract class AbstractOAuth2Configurer {

  private final ObjectPostProcessor<Object> objectPostProcessor;

  protected AbstractOAuth2Configurer(ObjectPostProcessor<Object> objectPostProcessor) {
    this.objectPostProcessor = objectPostProcessor;
  }

  abstract void init(HttpSecurity httpSecurity);

  abstract void configure(HttpSecurity httpSecurity);

  RequestMatcher getRequestMatcher() {
    return null;
  }

  protected final <T> T postProcess(T object) {
    return objectPostProcessor.postProcess(object);
  }

  protected final ObjectPostProcessor<Object> getObjectPostProcessor() {
    return objectPostProcessor;
  }
}
