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

package io.github.code13.books.jcip.ch2_thread_safe;

import io.github.code13.books.jcip.ThreadSafe;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CountingFactorizer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/7/2021 2:40 PM
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

  @Serial private static final long serialVersionUID = -7282908272971876866L;
  private final AtomicLong count = new AtomicLong();

  public long getCount() {
    return count.get();
  }

  @Override
  public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {
    BigInteger i = extractFromRequest(req);
    BigInteger[] factors = factor(i);
    encodeIntoResponse(res, factors);
  }

  void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {}

  BigInteger extractFromRequest(ServletRequest req) {
    return new BigInteger("7");
  }

  BigInteger[] factor(BigInteger i) {
    // Doesn't really factor
    return new BigInteger[] {i};
  }
}
