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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * FileOrDirCreate_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/25 15:45
 */
class FileOrDirCreate_Runner {

  Path basePath;
  Path path;

  @BeforeEach
  void setup() throws IOException {
    basePath = Paths.get(System.getProperty("user.dir"), "forDelete");
    if (!Files.exists(basePath)) {
      Files.createDirectories(basePath);
    }

    Path p1 = basePath.resolve("1.txt");
    if (!Files.exists(p1)) {
      path = Files.createFile(p1);
      Files.writeString(path, "abcdefg");
    }
  }

  @AfterEach
  void tearDown() throws IOException {
    // 删除 basePath 目录下所有文件
    deleteDir();
  }

  private void deleteDir() throws IOException {
    Files.walkFileTree(
        basePath,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.delete(file);
            return super.visitFile(file, attrs);
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return super.postVisitDirectory(dir, exc);
          }
        });
  }

  @Test
  @DisplayName("createFile1")
  void createFile1() throws IOException {

    Path path1 = basePath.resolve("aaa").resolve("1.txt");

    System.out.println(Files.isRegularFile(path1));
    System.out.println(Files.exists(path1));
    System.out.println(path1.getFileName().toString());
    System.out.println(path1.getParent().toString());

    if (!Files.exists(path1)) {
      Files.createDirectories(path1);
    }

    ByteArrayInputStream inputStream =
        new ByteArrayInputStream("1".getBytes(StandardCharsets.UTF_8));

    try (inputStream) {
      Files.copy(inputStream, path1, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  @Test
  @DisplayName("createFile2")
  void createFile2() throws IOException {

    Path file = basePath.resolve("bbb").resolve("2.txt");

    System.out.println(Files.isRegularFile(file));
    System.out.println(Files.exists(file));
    System.out.println(file.getFileName().toString());
    Path parent = file.getParent();
    System.out.println(parent.toString());

    if (!Files.exists(parent)) {
      Files.createDirectories(parent);
    }

    Files.deleteIfExists(file);

    Files.writeString(file, "111");

    System.out.println(Files.isRegularFile(file));
    System.out.println(Files.exists(file));
    System.out.println(file.getFileName().toString());
    System.out.println(parent.toString());
  }

  @Test
  @DisplayName("createFile3")
  void createFile3() throws IOException {
    Path file = basePath.resolve("ccc").resolve("2.txt");

    System.out.println(Files.exists(file));
    System.out.println(Files.isRegularFile(file));
    System.out.println(file.getFileName().toString());
    Path parent = file.getParent();
    System.out.println(parent.toString());

    if (!Files.exists(parent)) {
      Files.createDirectories(parent);
    }

    Files.deleteIfExists(file);

    Files.createFile(file);

    Files.write(file, "11".getBytes(StandardCharsets.UTF_8));

    System.out.println(Files.exists(file));
    System.out.println(Files.isRegularFile(file));
    System.out.println(file.getFileName().toString());
    System.out.println(parent.toString());
  }
}
