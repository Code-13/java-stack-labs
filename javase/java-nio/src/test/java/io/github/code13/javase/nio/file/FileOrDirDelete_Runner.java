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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 文件和文件夹删除方式汇总.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/12 12:34
 */
class FileOrDirDelete_Runner {

  // 一、删除文件或文件夹的四种基础方法
  // File类的delete()
  // File类的deleteOnExit()
  // Files.delete(Path path)
  // Files.deleteIfExists(Path path);

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
    //    deleteDir();
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
  @DisplayName("testFileDelete")
  void testFileDelete() {

    boolean delete = path.toFile().delete();
    assertTrue(delete);

    // 只能告诉你失败了 ，但是没有给出任何失败的原因
    delete = basePath.resolve("2.txt").toFile().delete();
    assertFalse(delete);
  }

  @Test
  @DisplayName("testFileDeleteOnExit")
  void testFileDeleteOnExit() throws IOException {
    // //void ,删除失败没有任何提示，应避免使用这个方法，就是个坑
    path.toFile().deleteOnExit();
  }

  @Test
  @DisplayName("testFilesDelete")
  void testFilesDelete() {
    // 如果文件不存在，抛出NoSuchFileException
    assertThrows(
        NoSuchFileException.class,
        () -> {
          Path path = basePath.resolve("2.txt");
          Files.delete(path);
        });

    // 如果文件夹里面包含文件，抛出DirectoryNotEmptyException
    assertThrows(
        DirectoryNotEmptyException.class,
        () -> {
          Files.delete(basePath);
        });
  }

  @Test
  @DisplayName("testFilesDeleteUseFilesWalk")
  void testFilesDeleteUseFilesWalk() throws IOException {
    // 使用walkFileTree方法遍历整个文件目录树，使用FileVisitor处理遍历出来的每一项文件或文件夹
    // FileVisitor的visitFile方法用来处理遍历结果中的“文件”，所以我们可以在这个方法里面删除文件
    // FileVisitor的postVisitDirectory方法，注意方法中的“post”表示“后去做……”的意思，所以用来文件都处理完成之后再去处理文件夹，所以使用这个方法删除文件夹就可以有效避免文件夹内容不为空的异常，因为在去删除文件夹之前，该文件夹里面的文件已经被删除了。

    Files.walkFileTree(
        basePath,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }
        });
  }

  @Test
  @DisplayName("testFileDeleteUseFilesWalk")
  void testFileDeleteUseFilesWalk() throws IOException {
    // 使用Files.walk遍历文件夹（包含子文件夹及子其文件），遍历结果是一个Stream<Path>
    // 对每一个遍历出来的结果进行处理，调用Files.delete就可以了。

    // 问题：怎么能做到先去删除文件，再去删除文件夹？ 。
    // 利用的是字符串的排序规则，从字符串排序规则上讲，“D:\data\test1\test2”一定排在“D:\data\test1\test2\test2.log”的前面。所以我们使用“sorted(Comparator.reverseOrder())”把Stream顺序颠倒一下，就达到了先删除文件，再删除文件夹的目的

    try (Stream<Path> stream = Files.walk(basePath)) {
      stream
          .sorted(Comparator.reverseOrder())
          .forEachOrdered(
              p -> {
                try {
                  Files.delete(p);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    }
  }

  @Test
  @DisplayName("testFileDeleteUseLegacyIO")
  void testFileDeleteUseLegacyIO() {
    // 传统IO递归删除
    // listFiles()方法只能列出文件夹下面的一层文件或文件夹，不能列出子文件夹及其子文件。
    // 先去递归删除子文件夹，再去删除文件夹自己本身
    deleteDirectoryLegacyIO(basePath.toFile());
  }

  private void deleteDirectoryLegacyIO(File file) {

    File[] list = file.listFiles(); // 无法做到list多层文件夹数据
    if (list != null) {
      for (File temp : list) { // 先去递归删除子文件夹及子文件
        deleteDirectoryLegacyIO(temp); // 注意这里是递归调用
      }
    }

    if (file.delete()) { // 再删除自己本身的文件夹
      System.out.printf("删除成功 : %s%n", file);
    } else {
      System.err.printf("删除失败 : %s%n", file);
    }
  }
}
