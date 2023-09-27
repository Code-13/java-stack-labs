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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZipService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/19 10:37
 */
public final class ZipService {

  List<String> filesListInDir = new ArrayList<String>();

  /**
   * 将 dir 目录下的文件打包成 zip, 将 zip 包的 path 返回.
   *
   * @param dir the dir
   * @return the path
   */
  public static Path compress(Path dir) {
    return null;
  }

  /**
   * This method zips the directory
   *
   * @param dir
   * @param zipDirName
   */
  private void zipDirectory(File dir, String zipDirName) {
    try {
      populateFilesList(dir);
      // now zip files one by one
      // create ZipOutputStream to write to the zip file
      FileOutputStream fos = new FileOutputStream(zipDirName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (String filePath : filesListInDir) {
        System.out.println("Zipping " + filePath);
        // for ZipEntry we need to keep only relative file path, so we used substring on absolute
        // path
        ZipEntry ze =
            new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
        zos.putNextEntry(ze);
        // read the file and write to ZipOutputStream
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, len);
        }
        zos.closeEntry();
        fis.close();
      }
      zos.close();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method populates all the files in a directory to a List
   *
   * @param dir
   * @throws IOException
   */
  private void populateFilesList(File dir) throws IOException {
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) filesListInDir.add(file.getAbsolutePath());
      else populateFilesList(file);
    }
  }

  /**
   * This method compresses the single file to zip format
   *
   * @param file
   * @param zipFileName
   */
  private static void zipSingleFile(File file, String zipFileName) {
    try {
      // create ZipOutputStream to write to the zip file
      FileOutputStream fos = new FileOutputStream(zipFileName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      // add a new Zip Entry to the ZipOutputStream
      ZipEntry ze = new ZipEntry(file.getName());
      zos.putNextEntry(ze);
      // read the file and write to ZipOutputStream
      FileInputStream fis = new FileInputStream(file);
      byte[] buffer = new byte[1024];
      int len;
      while ((len = fis.read(buffer)) > 0) {
        zos.write(buffer, 0, len);
      }

      // Close the zip entry to write to zip file
      zos.closeEntry();
      // Close resources
      zos.close();
      fis.close();
      fos.close();
      System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将 zip 文件全部解压出来，并返回所有 zip 包的根路径.
   *
   * @param zip the zip
   * @return the path
   */
  public static Path unCompress(Path zip) {
    return null;
  }
}
