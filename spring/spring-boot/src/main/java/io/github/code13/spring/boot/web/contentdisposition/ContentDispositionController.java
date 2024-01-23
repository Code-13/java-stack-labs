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

package io.github.code13.spring.boot.web.contentdisposition;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ContentDispositionController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/28 17:06
 */
@RestController
@RequestMapping("/spring/boot")
public class ContentDispositionController {

  @GetMapping("/content-disposition")
  public void contentDisposition(HttpServletResponse response) throws IOException {

    Path file = Files.createTempFile("下载-down test ", ".txt");
    Files.writeString(file, "asdfghjklqwertyuiopzxcvbnm");

    response.setHeader(HttpHeaders.CONTENT_TYPE, getMediaType(file).toString());
    response.setHeader(
        HttpHeaders.CONTENT_DISPOSITION, buildContentDisposition("下载-down test .txt"));

    try (InputStream inputStream = Files.newInputStream(file)) {
      inputStream.transferTo(response.getOutputStream());
    }

    Files.delete(file);
  }

  private String buildContentDisposition(String fileName) {
    return ContentDisposition.attachment()
        .filename(fileName)
        .filename(fileName, StandardCharsets.UTF_8)
        .build()
        .toString();
  }

  private static MediaType getMediaType(Path file) {
    return MediaTypeFactory.getMediaType(file.getFileName().toString())
        .orElse(MediaType.APPLICATION_OCTET_STREAM);
  }
}
