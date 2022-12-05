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

package io.github.code13.libs.apache.httpclient5;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HttpClient5Asynchronous.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/5 14:46
 */
class HttpClient5Asynchronous {

  @Test
  @DisplayName("Asynchronous requests")
  void async1() throws IOException, ExecutionException, InterruptedException {
    try (CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault()) {
      // start http client
      asyncClient.start();
      // Execution of requests
      SimpleHttpRequest request = SimpleRequestBuilder.get("http://httpbin.org/get").build();
      Future<SimpleHttpResponse> future = asyncClient.execute(request, null);
      // Wait until the return is complete
      SimpleHttpResponse response = future.get();
      System.out.println("getAsync1:" + request.getRequestUri() + "->" + response.getCode());
    }
  }

  @Test
  @DisplayName("Asynchronous requests, callbacks based on response")
  void async2() throws IOException, InterruptedException {
    try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
      // start http client
      httpclient.start();
      CountDownLatch latch = new CountDownLatch(1);
      SimpleHttpRequest request = SimpleRequestBuilder.get("http://httpbin.org/get").build();

      httpclient.execute(
          request,
          new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse response) {
              latch.countDown();
              System.out.println(
                  "getAsync2:" + request.getRequestUri() + "->" + response.getCode());
            }

            @Override
            public void failed(Exception ex) {
              latch.countDown();
              System.out.println("getAsync2:" + request.getRequestUri() + "->" + ex);
            }

            @Override
            public void cancelled() {
              latch.countDown();
              System.out.println("getAsync2:" + request.getRequestUri() + " cancelled");
            }
          });

      latch.await();
    }
  }

  @Test
  @DisplayName("async3")
  void async3() {
    try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
      // start http clinet
      httpclient.start();

      SimpleHttpRequest request = SimpleRequestBuilder.get("http://httpbin.org/get").build();

      CountDownLatch latch = new CountDownLatch(1);
      AsyncRequestProducer producer = AsyncRequestBuilder.get("http://httpbin.org/get").build();
      AbstractCharResponseConsumer<HttpResponse> consumer3 =
          new AbstractCharResponseConsumer<HttpResponse>() {

            HttpResponse response;

            @Override
            protected void start(HttpResponse response, ContentType contentType)
                throws HttpException, IOException {
              System.out.println("getAsync3: Start response....");
              this.response = response;
            }

            @Override
            protected int capacityIncrement() {
              return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer data, boolean endOfStream) throws IOException {
              System.out.println("getAsync3: Data received....");
              // Do something useful
            }

            @Override
            protected HttpResponse buildResult() throws IOException {
              System.out.println("getAsync3: Receiving completed...");
              return response;
            }

            @Override
            public void releaseResources() {}
          };
      httpclient.execute(
          producer,
          consumer3,
          new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
              latch.countDown();
              System.out.println(
                  "getAsync3: " + request.getRequestUri() + "->" + response.getCode());
            }

            @Override
            public void failed(Exception ex) {
              latch.countDown();
              System.out.println("getAsync3: " + request.getRequestUri() + "->" + ex);
            }

            @Override
            public void cancelled() {
              latch.countDown();
              System.out.println("getAsync3: " + request.getRequestUri() + " cancelled");
            }
          });
      latch.await();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
