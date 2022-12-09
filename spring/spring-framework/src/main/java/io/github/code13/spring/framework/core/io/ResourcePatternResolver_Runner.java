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

package io.github.code13.spring.framework.core.io;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Examples for {@link org.springframework.core.io.support.ResourcePatternResolver}.
 *
 * <p>ResourceLoader
 *
 * @see org.springframework.core.io.support.ResourcePatternResolver
 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/21 21:17
 */
public class ResourcePatternResolver_Runner {

  @Test
  @DisplayName("test1")
  void test1() throws IOException {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    Resource[] resources =
        resourcePatternResolver.getResources(
            "classpath*:io/github/code13/spring/framework/*/**.class");

    assertTrue(resources.length > 0);

    Arrays.stream(resources).forEach(System.out::println);
  }
}
