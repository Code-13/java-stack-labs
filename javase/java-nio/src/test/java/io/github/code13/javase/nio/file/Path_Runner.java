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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Files_Runner.
 *
 * <p>{@link java.nio.file.Path}
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/8/4 14:06
 */
public class Path_Runner {

  @Test
  @DisplayName("创建 Path")
  void createPath() {
    // 使用绝对路径
    String userHome = System.getProperty("user.home");
    Path path = Paths.get(userHome, "Downloads", "1.txt");
    // 等价于
    path = Path.of(userHome, "Downloads", "1.txt");
    // 等价于
    path = FileSystems.getDefault().getPath(userHome, "Downloads", "1.txt");

    System.out.println(path);

    // 使用相对路径
    Path path1 = Paths.get("./1.txt");
    System.out.println(path1);
  }

  @Test
  @DisplayName("toFileAndToPath")
  void toFileAndToPath() {}

  @Test
  @DisplayName("files_createTempDirectory")
  void files_createTempDirectory() throws IOException {
    Path directory = Files.createTempDirectory("tmp");
    System.out.println(directory.toString());
    Files.deleteIfExists(directory);
  }

  @Test
  @DisplayName("testRelative")
  void testRelative() {
    Path root = Paths.get("/a/b/");
    Path path = Paths.get("/a/b/c/d");

    Path relativize = root.relativize(path);
    System.out.println(relativize);
    System.out.println(relativize.isAbsolute());

    Path path2 = Paths.get("a/b/c/d");
    Path name = path2.getName(0);
    System.out.println(name);
  }

  @Test
  @DisplayName("files_probe")
  void files_probe() throws IOException {
    String mineType = Files.probeContentType(Paths.get("1.png"));
    System.out.println(mineType);
    mineType = Files.probeContentType(Paths.get("1.pdf"));
    System.out.println(mineType);

    Path path = Paths.get(System.getProperty("user.home"), ".mime.types");
    System.out.println(path);
    System.out.println(Files.exists(path));
  }

  @Test
  @DisplayName("testWatchable")
  void testWatchable() throws IOException {
    Path file = Files.createTempFile("1", ".txt");
    WatchKey watchKey =
        file.register(
            FileSystems.getDefault().newWatchService(), StandardWatchEventKinds.ENTRY_MODIFY);
  }
}
