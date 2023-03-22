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

package io.github.code13.spring.framework.core.type;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.ReflectionUtils;

/**
 * MethodMetadata_Runner.
 *
 * @see org.springframework.core.type.MethodMetadata
 * @see org.springframework.core.type.StandardMethodMetadata
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 21:25
 */
public class MethodMetadata_Runner {

  public void testForMethodMetadata() {}

  @Test
  @DisplayName("methodMetadata")
  void methodMetadata() {
    Method method =
        ReflectionUtils.findMethod(MethodMetadata_Runner.class, "testForMethodMetadata");

    StandardMethodMetadata metadata = new StandardMethodMetadata(method);
    assertFalse(metadata.isAbstract());
    assertFalse(metadata.isFinal());
    assertFalse(metadata.isStatic());
    assertTrue(metadata.isOverridable());
  }
}
