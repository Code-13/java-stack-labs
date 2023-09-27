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

package io.github.code13.javase.nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * File_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/16 13:07
 */
public class File_Runner {

  @Test
  @DisplayName("testCreatTmpFile")
  void testCreatTmpFile() throws IOException {
    File file = File.createTempFile("123", "txt");
    file.deleteOnExit();
  }

  @Test
  @DisplayName("testCreatTmpFile1")
  void testCreatTmpFile1() throws IOException {
    Path path = Files.createTempFile("1", "txt");
    Files.deleteIfExists(path);
  }
}
