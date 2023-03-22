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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.hc.client5.http.auth.AuthExchange;
import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.ChainElement;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.DigestScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HttpClient5Examples.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/4 13:34
 */
class HttpClient5Examples {

  static final String GET = "http://httpbin.org/get";
  static final String POST = "http://httpbin.org/post";

  @Test
  @DisplayName("get")
  void get() throws IOException, ParseException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet(GET);
      try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
        System.out.println(response.getVersion());
        System.out.println(response.getCode());
        System.out.println(response.getReasonPhrase());

        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        System.out.println(res);

        String url = JsonPath.parse(res).read("$.url", String.class);
        assertEquals(GET, url);
      }
    }
  }

  @Test
  @DisplayName("getWithQueryParameters")
  void getWithQueryParameters() throws IOException, URISyntaxException, ParseException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet(GET);
      List<NameValuePair> nvps =
          List.of(
              new BasicNameValuePair("username", "wdbyte.com"),
              new BasicNameValuePair("password", "secret"));

      URI uri = new URIBuilder(new URI(GET)).addParameters(nvps).build();
      httpGet.setUri(uri);

      try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
        System.out.println(response.getVersion());
        System.out.println(response.getCode());
        System.out.println(response.getReasonPhrase());

        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        System.out.println(res);

        String url = JsonPath.parse(res).read("$.url", String.class);
        assertEquals("http://httpbin.org/get?username=wdbyte.com&password=secret", url);
      }
    }
  }

  @Test
  @DisplayName("postWithUrlEncodedForm")
  void postWithUrlEncodedForm() throws IOException, ParseException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      List<NameValuePair> nvps =
          List.of(
              new BasicNameValuePair("username", "wdbyte.com"),
              new BasicNameValuePair("password", "secret"));

      HttpPost httpPost = new HttpPost(POST);
      httpPost.setEntity(new UrlEncodedFormEntity(nvps));

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        System.out.println(response.getVersion()); // HTTP/1.1
        System.out.println(response.getCode()); // 200
        System.out.println(response.getReasonPhrase()); // OK

        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        System.out.println(res);

        String url = JsonPath.parse(res).read("$.url", String.class);
        assertEquals(GET, url);
      }
    }
  }

  @Test
  @DisplayName("postWithJson")
  void postWithJson() throws IOException, ParseException {
    String json =
        """
        {
          "password": "secret",
          "username": "wdbyte.com"
        }""";

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httpPost = new HttpPost(POST);
      httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);

        assertNotNull(JsonPath.parse(res).read("$.data", String.class));
      }
    }
  }

  @Test
  @DisplayName("GetWithTimeout")
  void getWithTimeout() throws IOException, ParseException {
    RequestConfig config =
        RequestConfig.custom()
            .setConnectTimeout(Timeout.ofMilliseconds(5000L))
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000L))
            .setResponseTimeout(Timeout.ofMilliseconds(5000L))
            .build();

    HttpGet httpGet = new HttpGet(GET);

    try (CloseableHttpClient httpClient =
            HttpClients.custom().setDefaultRequestConfig(config).build();
        CloseableHttpResponse response = httpClient.execute(httpGet)) {
      System.out.println(response.getVersion()); // HTTP/1.1
      System.out.println(response.getCode()); // 200
      System.out.println(response.getReasonPhrase()); // OK
      HttpEntity entity = response.getEntity();

      String res = EntityUtils.toString(entity);

      System.out.println(res);
    }
  }

  @Test
  @DisplayName("getWithCookie")
  void getWithCookie() throws IOException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      //// Create a local cookie store
      BasicCookieStore cookieStore = new BasicCookieStore();
      // BasicClientCookie clientCookie = new BasicClientCookie("name", "www.wdbyte.com");
      // clientCookie.setDomain("http://httpbin.org/cookies");
      // Expiration time
      // clientCookie.setExpiryDate(new Date());
      // addCookie
      // cookieStore.addCookie(clientCookie);

      HttpClientContext localContext = HttpClientContext.create();
      localContext.setCookieStore(cookieStore);

      HttpGet httpget = new HttpGet("http://httpbin.org/cookies/set/cookieName/www.wdbyte.com");

      try (CloseableHttpResponse response = httpClient.execute(httpget, localContext)) {
        System.out.println("----------------------------------------");
        System.out.println(response.getCode() + " " + response.getReasonPhrase());
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
          System.out.println("Local cookie: " + cookie);
        }
        EntityUtils.consume(response.getEntity());
      }
    }
  }

  @Test
  @DisplayName("ChunkEncodedPost")
  void chunkEncodedPost() throws IOException, ParseException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost httppost = new HttpPost("http://httpbin.org/post");
      InputStreamEntity entity =
          new InputStreamEntity(
              Objects.requireNonNull(getClass().getResourceAsStream("/param.json")),
              -1,
              ContentType.APPLICATION_JSON);
      httppost.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httppost)) {
        System.out.println("----------------------------------------");
        System.out.println(response.getCode() + " " + response.getReasonPhrase());
        System.out.println(EntityUtils.toString(response.getEntity()));
      }
    }
  }

  @Test
  @DisplayName("FormLogin")
  void formLogin() throws IOException, ParseException {
    BasicCookieStore cookieStore = new BasicCookieStore();
    try (CloseableHttpClient httpclient =
        HttpClients.custom().setDefaultCookieStore(cookieStore).build()) {
      // I should have used a POST request to send the form parameters, but there is no
      // corresponding interface in httpbin.org for testing, so I switched to a GET request here
      // HttpPost httpPost = new HttpPost("http://httpbin.org/cookies/set/username/wdbyte.com");
      HttpGet httpPost = new HttpGet("http://httpbin.org/cookies/set/username/wdbyte.com");

      // POST Form
      List<NameValuePair> nvps = new ArrayList<>();
      nvps.add(new BasicNameValuePair("username", "wdbyte.com"));
      nvps.add(new BasicNameValuePair("password", "secret"));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps));

      try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
        HttpEntity entity = response.getEntity();

        System.out.println(
            "Login form get: " + response.getCode() + " " + response.getReasonPhrase());
        System.out.println("Current response information " + EntityUtils.toString(entity));

        System.out.println("Post Login Cookie:");
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies.isEmpty()) {
          System.out.println("None");
        } else {
          for (Cookie cookie : cookies) {
            System.out.println("- " + cookie);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("Basic Authorization")
  void basicAuthorization() throws IOException, URISyntaxException, ParseException {
    BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(
        new AuthScope("httpbin.org", 80),
        new UsernamePasswordCredentials("admin", "123456".toCharArray()));

    try (CloseableHttpClient httpClient =
        HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {
      HttpGet httpget = new HttpGet("http://httpbin.org/basic-auth/admin/123456");

      System.out.println("Execute request " + httpget.getMethod() + " " + httpget.getUri());
      try (CloseableHttpResponse response = httpClient.execute(httpget)) {
        System.out.println("----------------------------------------");
        System.out.println(response.getCode() + " " + response.getReasonPhrase());
        System.out.println(EntityUtils.toString(response.getEntity()));
      }
    }
  }

  @Test
  @DisplayName("PreemptiveDigestAuthentication")
  void preemptiveDigestAuthentication() throws IOException, URISyntaxException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

      HttpHost target = new HttpHost("http", "httpbin.org", 80);

      HttpClientContext localContext = HttpClientContext.create();
      BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(
          new AuthScope(target), new UsernamePasswordCredentials("admin", "123456".toCharArray()));
      localContext.setCredentialsProvider(credentialsProvider);

      HttpGet httpget = new HttpGet("http://httpbin.org/digest-auth/auth/admin/123456");

      System.out.println("Execute request " + httpget.getMethod() + " " + httpget.getUri());
      for (int i = 0; i < 2; i++) {
        try (CloseableHttpResponse response = httpclient.execute(target, httpget, localContext)) {
          System.out.println("----------------------------------------");
          System.out.println(response.getCode() + " " + response.getReasonPhrase());
          EntityUtils.consume(response.getEntity());

          AuthExchange authExchange = localContext.getAuthExchange(target);
          if (authExchange != null) {
            AuthScheme authScheme = authExchange.getAuthScheme();
            if (authScheme instanceof DigestScheme digestScheme) {
              System.out.println(
                  "Nonce: "
                      + digestScheme.getNonce()
                      + "; count: "
                      + digestScheme.getNounceCount());
            }
          }
        }
      }
    }
  }

  @Test
  @DisplayName("interceptors")
  void interceptors() throws IOException, ParseException, URISyntaxException {
    // For request id 2, simulate a 404 response and customize the content of the
    // response.
    try (CloseableHttpClient httpclient =
        HttpClients.custom()
            //  Add a request id to the request header
            .addRequestInterceptorFirst(
                new HttpRequestInterceptor() {
                  private final AtomicLong count = new AtomicLong(0);

                  @Override
                  public void process(
                      HttpRequest request, EntityDetails entity, HttpContext context)
                      throws HttpException, IOException {
                    request.setHeader("request-id", Long.toString(count.incrementAndGet()));
                  }
                })
            .addExecInterceptorAfter(
                ChainElement.PROTOCOL.name(),
                "custom",
                (request, scope, chain) -> {
                  Header idHeader = request.getFirstHeader("request-id");
                  if (idHeader != null && "2".equalsIgnoreCase(idHeader.getValue())) {
                    ClassicHttpResponse response =
                        new BasicClassicHttpResponse(HttpStatus.SC_NOT_FOUND, "Oppsie");
                    response.setEntity(new StringEntity("bad luck", ContentType.TEXT_PLAIN));
                    return response;
                  } else {
                    return chain.proceed(request, scope);
                  }
                })
            .build()) {

      for (int i = 0; i < 3; i++) {
        HttpGet httpget = new HttpGet("http://httpbin.org/get");

        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
          System.out.println("----------------------------------------");
          System.out.println("Execute request " + httpget.getMethod() + " " + httpget.getUri());
          System.out.println(response.getCode() + " " + response.getReasonPhrase());
          System.out.println(EntityUtils.toString(response.getEntity()));
        }
      }
    }
  }
}
