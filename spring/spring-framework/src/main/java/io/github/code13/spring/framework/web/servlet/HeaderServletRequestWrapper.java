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

package io.github.code13.spring.framework.web.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * HeaderServletRequestWrapper.
 *
 * <p>解决不能设置请求头的问题
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/27 10:09
 */
public class HeaderServletRequestWrapper extends HttpServletRequestWrapper {

  private final MultiValueMap<String, String> extraHeaders = new LinkedMultiValueMap<>();

  public HeaderServletRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  /**
   * 添加 header, 可以添加多个 header 值
   *
   * @param name name
   * @param value value
   */
  public void addHeader(String name, String value) {
    extraHeaders.add(name, value);
  }

  @Override
  public String getHeader(String name) {
    String value = super.getHeader(name);
    if (value == null) {
      List<String> values = extraHeaders.get(name);
      if (values != null && !values.isEmpty()) {
        value = values.get(0);
      }
    }
    return value;
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    Enumeration<String> names = super.getHeaderNames();
    if (!extraHeaders.isEmpty()) {
      ArrayList<String> headerNames = Collections.list(names);
      headerNames.addAll(extraHeaders.keySet());
      return Collections.enumeration(headerNames);
    }
    return names;
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    Enumeration<String> originHeaders = super.getHeaders(name);
    if (extraHeaders.containsKey(name)) {
      ArrayList<String> headers = Collections.list(originHeaders);
      headers.addAll(extraHeaders.get(name));
      return Collections.enumeration(headers);
    }
    return originHeaders;
  }
}
