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

package io.github.code13.spring.security.oauth2.authorization.server;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * AuthorizationController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/8 22:39
 */
@Controller
public class AuthorizationController {

  @GetMapping("/login/oauth2")
  public void loginOauth2(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    HttpSessionRequestCache cache = new HttpSessionRequestCache();
    SavedRequest savedRequest = cache.getRequest(request, response);

    String redirectUrl = savedRequest.getRedirectUrl();

    System.out.println("----------");
    System.out.println(redirectUrl);
    System.out.println("----------");

    // 可以在此处做一些其余操作

    response.sendRedirect("/login.html");
  }

  @GetMapping("/login.html")
  public String loginPage() {
    return "login";
  }
}