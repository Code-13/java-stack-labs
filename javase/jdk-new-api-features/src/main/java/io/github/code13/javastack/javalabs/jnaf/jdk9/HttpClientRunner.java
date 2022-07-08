/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.javalabs.jnaf.jdk9;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HttpClientRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/12 16:40
 */
@DisplayName("""
    定义一个新的 HTTP 客户端 API 来实现 HTTP/2 和 WebSocket，并且可以替换旧的HttpURLConnectionAPI。
    """)
class HttpClientRunner {

  @Test
  @DisplayName("同步代码示例")
  void synchronousExample() throws IOException, InterruptedException {

    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
        .version(HttpClient.Version.HTTP_2)
        .build();

    HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
        .header("Content-Type", "*/*")
        .GET()
        .build();

    HttpResponse<String> response = httpClient.send(httpRequest, BodyHandlers.ofString());

    System.out.println(response.body());
  }

  @Test
  @DisplayName("异步代码示例")
  void asynchronousExample() throws InterruptedException {
    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.of(10, ChronoUnit.SECONDS))
        .version(HttpClient.Version.HTTP_2)
        .build();

    HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
        .header("Content-Type", "*/*")
        .GET()
        .build();

    httpClient.sendAsync(httpRequest, BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenAccept(System.out::println);

    Thread.currentThread().join();
  }

}
