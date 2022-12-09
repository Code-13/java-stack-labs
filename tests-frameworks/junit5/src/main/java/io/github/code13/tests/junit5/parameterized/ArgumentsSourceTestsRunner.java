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

package io.github.code13.tests.junit5.parameterized;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * {@link ArgumentsSource} can be used to specify a custom, reusable ArgumentsProvider. Note that an
 * implementation of ArgumentsProvider must be declared as either a top-level class or as a static
 * nested class.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 3:49 PM
 */
class ArgumentsSourceTestsRunner {

  @ParameterizedTest
  @ArgumentsSource(MyArgumentsProvider.class)
  void testWithArgumentsSource(String argument) {
    assertNotNull(argument);
  }

  static class MyArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of("apple", "banana").map(Arguments::of);
    }
  }
}
