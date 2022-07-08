/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.frameworks.junit5.extensions.buildin;

import org.junit.jupiter.api.io.TempDir;

/**
 * The TempDirectory Extension.
 *
 * <p>{@link TempDir} is an experimental feature You’re invited to give it a try and provide
 * feedback to the JUnit team so they can improve and eventually promote this feature.
 *
 * <p>The built-in TempDirectory extension is used to create and clean up a temporary directory for
 * an individual test or all tests in a test class. It is registered by default. To use it, annotate
 * a field of type java.nio.file.Path or java.io.File with {@link TempDir} or add a parameter of
 * type java.nio.file.Path or java.io.File annotated with {@link TempDir} to a lifecycle method or
 * test method.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/8/2021 9:28 AM
 */
class TempDirectoryExtension {

  //  @Test
  //  void writeItemsToFile(@TempDir Path tempDir) throws IOException {
  //    Path file = tempDir.resolve("test.txt");
  //    new ListWriter(file).write();
  //    assertEquals(singletonList("a,b,c"), Files.readAllLines(file));
  //  }
  //
  //  @Test
  //  void copyFileFromSourceToTarget(@TempDir Path source, @TempDir Path target) throws IOException
  // {
  //    Path sourceFile = source.resolve("test.txt");
  //    new ListWriter(sourceFile).write("a", "b", "c");
  //
  //    Path targetFile = Files.copy(sourceFile, target.resolve("test.txt"));
  //
  //    assertNotEquals(sourceFile, targetFile);
  //    assertEquals(singletonList("a,b,c"), Files.readAllLines(targetFile));
  //  }
}
