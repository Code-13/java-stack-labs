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

package io.github.code13.spring.security.oauth2.client.sso;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * OAuth2SsoCondition.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/12 20:41
 */
final class OAuth2SsoCondition extends SpringBootCondition {

  @Override
  public ConditionOutcome getMatchOutcome(
      ConditionContext context, AnnotatedTypeMetadata metadata) {

    Environment environment = context.getEnvironment();

    boolean enable = environment.getProperty("sso.web.enable", Boolean.class, true);
    return enable
        ? ConditionOutcome.match("SSO is enabled.")
        : ConditionOutcome.noMatch("SSO do not be enabled. To enable, set `sso.web.enable` true");
  }
}
