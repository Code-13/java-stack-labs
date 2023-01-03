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

import java.util.Map;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.util.StringUtils;

/**
 * OAuth2ConfigurerUtils.
 *
 * <p>copy from {@link
 * org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2ConfigurerUtils}
 *
 * @see
 *     org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2ConfigurerUtils
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/1/3 20:44
 */
final class OAuth2ConfigurerUtils {

  static <T> T getBean(HttpSecurity httpSecurity, Class<T> type) {
    return httpSecurity.getSharedObject(ApplicationContext.class).getBean(type);
  }

  @SuppressWarnings("unchecked")
  static <T> T getBean(HttpSecurity httpSecurity, ResolvableType type) {
    ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
    String[] names = context.getBeanNamesForType(type);
    if (names.length == 1) {
      return (T) context.getBean(names[0]);
    }
    if (names.length > 1) {
      throw new NoUniqueBeanDefinitionException(type, names);
    }
    throw new NoSuchBeanDefinitionException(type);
  }

  static <T> T getOptionalBean(HttpSecurity httpSecurity, Class<T> type) {
    Map<String, T> beansMap =
        BeanFactoryUtils.beansOfTypeIncludingAncestors(
            httpSecurity.getSharedObject(ApplicationContext.class), type);
    if (beansMap.size() > 1) {
      throw new NoUniqueBeanDefinitionException(
          type,
          beansMap.size(),
          "Expected single matching bean of type '"
              + type.getName()
              + "' but found "
              + beansMap.size()
              + ": "
              + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
    }
    return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
  }

  @SuppressWarnings("unchecked")
  static <T> T getOptionalBean(HttpSecurity httpSecurity, ResolvableType type) {
    ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
    String[] names = context.getBeanNamesForType(type);
    if (names.length > 1) {
      throw new NoUniqueBeanDefinitionException(type, names);
    }
    return names.length == 1 ? (T) context.getBean(names[0]) : null;
  }

  private OAuth2ConfigurerUtils() {
    // no instance
  }
}
