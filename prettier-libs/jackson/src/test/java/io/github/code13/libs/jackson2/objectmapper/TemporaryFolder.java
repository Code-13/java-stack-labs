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

package io.github.code13.libs.jackson2.objectmapper;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * TemporaryFolder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/6/6 13:36
 */
public class TemporaryFolder {

  private File rootFolder;

  public File newFile(String name) throws IOException {
    File result = new File(rootFolder, name);
    result.createNewFile();
    return result;
  }

  void prepare() {
    try {
      rootFolder = File.createTempFile("junit5-", ".tmp");
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    rootFolder.delete();
    rootFolder.mkdir();
  }

  void cleanUp() {
    try {
      Files.walkFileTree(rootFolder.toPath(), new DeleteAllVisitor());
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static class DeleteAllVisitor extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
      Files.delete(file);
      return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path directory, IOException exception)
        throws IOException {
      Files.delete(directory);
      return CONTINUE;
    }
  }
}
