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

package io.github.code13.libs.okhttp3;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * OkHttpRecipes.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/6 17:39
 */
public class OkHttpRecipes {

  OkHttpClient client = new OkHttpClient();

  @Test
  @DisplayName("Synchronous Get")
  void synchronousGet() throws IOException {

    Request request = new Request.Builder().url("https://publicobject.com/helloworld.txt").build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      Headers responseHeaders = response.headers();
      for (int i = 0; i < responseHeaders.size(); i++) {
        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("Asynchronous Get")
  void asynchronousGet() {
    Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                e.printStackTrace();
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                  if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                  }

                  Headers responseHeaders = response.headers();
                  for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                  }

                  System.out.println(responseBody.string());
                }
              }
            });
  }

  @Test
  @DisplayName("Accessing Headers")
  void accessingHeaders() throws IOException {
    Request request =
        new Request.Builder()
            .url("https://api.github.com/repos/square/okhttp/issues")
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("Accept", "application/json; q=0.5")
            .addHeader("Accept", "application/vnd.github.v3+json")
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println("Server: " + response.header("Server"));
      System.out.println("Date: " + response.header("Date"));
      System.out.println("Vary: " + response.headers("Vary"));
    }
  }

  @Test
  @DisplayName("PostingAString")
  void postingAString() throws IOException {
    MediaType mediaTypeMarkdown = MediaType.parse("text/x-markdown; charset=utf-8");
    String postBody =
        """
        Releases
        --------

         * _1.0_ May 6, 2013
         * _1.1_ June 15, 2013
         * _1.2_ August 11, 2013
        """;

    Request request =
        new Request.Builder()
            .url("https://api.github.com/markdown/raw")
            .post(RequestBody.create(mediaTypeMarkdown, postBody))
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("Post Streaming")
  void postStreaming() throws IOException {
    MediaType mediaTypeMarkdown = MediaType.parse("text/x-markdown; charset=utf-8");

    RequestBody requestBody =
        new RequestBody() {
          @Override
          public MediaType contentType() {
            return mediaTypeMarkdown;
          }

          @Override
          public void writeTo(BufferedSink sink) throws IOException {
            sink.writeUtf8("Numbers\n");
            sink.writeUtf8("-------\n");
            for (int i = 2; i <= 997; i++) {
              sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
            }
          }

          private String factor(int n) {
            for (int i = 2; i < n; i++) {
              int x = n / i;
              if (x * i == n) {
                return factor(x) + " × " + i;
              }
            }
            return Integer.toString(n);
          }
        };

    Request request =
        new Request.Builder().url("https://api.github.com/markdown/raw").post(requestBody).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("Posting a File")
  void postingAFile() throws IOException {
    MediaType mediaTypeMarkdown = MediaType.parse("text/x-markdown; charset=utf-8");

    File file = new File("src/test/resources/README.md");

    Request request =
        new Request.Builder()
            .url("https://api.github.com/markdown/raw")
            .post(RequestBody.create(mediaTypeMarkdown, file))
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("Posting form parameters")
  void postingFormParameters() throws IOException {
    RequestBody formBody = new FormBody.Builder().add("search", "Jurassic Park").build();
    Request request =
        new Request.Builder().url("https://en.wikipedia.org/wiki/Main_Page").post(formBody).build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("Posting a multipart request")
  void postingAMultipartRequest() throws IOException {
    final String IMGUR_CLIENT_ID = "...";
    MediaType mediaType = MediaType.parse("image/png");

    // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
    RequestBody requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("title", "Square Logo")
            .addFormDataPart(
                "image",
                "logo-square.png",
                RequestBody.create(mediaType, new File("src/test/resources/icon-square.png")))
            .build();

    Request request =
        new Request.Builder()
            .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
            .url("https://api.imgur.com/3/image")
            .post(requestBody)
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }

  @Test
  @DisplayName("postWithJson")
  void postWithJson() throws IOException {
    Request request =
        new Request.Builder().url("https://api.github.com/gists/c2a7c39532239ff261be").build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      Gist gist =
          new ObjectMapper()
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
              .readValue(response.body().byteStream(), Gist.class);

      for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
        System.out.println(entry.getKey());
        System.out.println(entry.getValue().content);
      }
    }
  }

  record Gist(Map<String, GistFile> files) {}

  record GistFile(String content) {}

  @Test
  @DisplayName("Canceling a Call")
  void cancelingACall() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Request request =
        new Request.Builder()
            .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
            .build();

    long startNanos = System.nanoTime();
    Call call = client.newCall(request);

    // Schedule a job to cancel the call in 1 second.
    executor.schedule(
        () -> {
          System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
          call.cancel();
          System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
        },
        1,
        TimeUnit.SECONDS);

    System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
    try (Response response = call.execute()) {
      System.out.printf(
          "%.2f Call was expected to fail, but completed: %s%n",
          (System.nanoTime() - startNanos) / 1e9f, response);
    } catch (IOException e) {
      System.out.printf(
          "%.2f Call failed as expected: %s%n", (System.nanoTime() - startNanos) / 1e9f, e);
    }
  }

  @Test
  @DisplayName("Timeouts")
  void timeouts() throws IOException {
    OkHttpClient client =
        new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    Request request =
        new Request.Builder()
            .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("Response completed: " + response);
    }
  }

  @Test
  @DisplayName("HandlingAuthentication")
  void handlingAuthentication() throws IOException {
    OkHttpClient client =
        new OkHttpClient.Builder()
            .authenticator(
                (route, response) -> {
                  if (response.request().header("Authorization") != null) {
                    return null; // Give up, we've already attempted to authenticate.
                  }

                  System.out.println("Authenticating for response: " + response);
                  System.out.println("Challenges: " + response.challenges());
                  String credential = Credentials.basic("jesse", "password1");
                  return response
                      .request()
                      .newBuilder()
                      .header("Authorization", credential)
                      .build();
                })
            .build();

    Request request =
        new Request.Builder().url("http://publicobject.com/secrets/hellosecret.txt").build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      System.out.println(response.body().string());
    }
  }
}
