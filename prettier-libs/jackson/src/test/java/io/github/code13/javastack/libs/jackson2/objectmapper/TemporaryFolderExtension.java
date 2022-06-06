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

package io.github.code13.javastack.libs.jackson2.objectmapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * TemporaryFolderExtension.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/6 13:20
 */
public class TemporaryFolderExtension
    implements AfterEachCallback, TestInstancePostProcessor, ParameterResolver {

  private final Collection<TemporaryFolder> tempFolders;

  public TemporaryFolderExtension() {
    tempFolders = new ArrayList<>();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    tempFolders.forEach(TemporaryFolder::cleanUp);
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    Stream.of(testInstance.getClass().getDeclaredFields())
        .filter(field -> field.getType() == TemporaryFolder.class)
        .forEach(field -> injectTemporaryFolder(testInstance, field));
  }

  private void injectTemporaryFolder(Object testInstance, Field field) {
    field.setAccessible(true);
    try {
      field.set(testInstance, createTempFolder());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Object createTempFolder() {
    TemporaryFolder result = new TemporaryFolder();
    result.prepare();
    tempFolders.add(result);
    return result;
  }

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType() == TemporaryFolder.class;
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return createTempFolder();
  }
}
