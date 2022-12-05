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
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HttpClient5Fluent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/5 14:39
 */
class HttpClient5Fluent {

  @Test
  @DisplayName("get")
  void get() throws IOException {
    System.out.println(Request.get("http://httpbin.org/get").execute().returnContent().asString());
  }

  @Test
  @DisplayName("post")
  void postWithForm() throws IOException {
    String res =
        Request.post("http://httpbin.org/post")
            .bodyForm(
                new BasicNameValuePair("username", "wdbyte.com"),
                new BasicNameValuePair("password", "secret"))
            .execute()
            .returnContent()
            .asString();

    System.out.println(res);
  }

  @Test
  @DisplayName("postWithJson")
  void postWithJson() throws IOException {
    String res =
        Request.post("http://httpbin.org/post")
            .bodyString("", ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString();

    System.out.println(res);
  }
}
