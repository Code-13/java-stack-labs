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

package io.github.code13.javastack.spring.framework.web.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * RequestWrapper.
 *
 * <p>解决 InputStream 只能读取一次的问题
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/26 17:00
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] servletInputBytes;

  public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    servletInputBytes = doBuffered(request);
  }

  private byte[] doBuffered(HttpServletRequest request) throws IOException {
    try (ServletInputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ) {
      inputStream.transferTo(outputStream);
      return outputStream.toByteArray();
    }
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return new BufferedServletInputStream(servletInputBytes);
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  private static class BufferedServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream inputStream;

    public BufferedServletInputStream(byte[] buffer) {
      inputStream = new ByteArrayInputStream(buffer);
    }

    @Override
    public int available() throws IOException {
      return inputStream.available();
    }

    @Override
    public int read() throws IOException {
      return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      return inputStream.read(b, off, len);
    }

    @Override
    public boolean isFinished() {
      return false;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {}
  }
}
