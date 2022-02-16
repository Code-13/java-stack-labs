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

package io.github.code13.javastack.spring.security.oauth2.client;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FooController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 22:19
 */
@RestController
public class OAuth2ClientController {

  /**
   * redirect_uri.
   *
   * @param client ID为{@code code13-auth}的客户端信息，注意ClientRegistration中必须包含该ID
   * @return the map
   */
  @GetMapping("/foo/bar")
  public Map<String, Object> bar(
      @RegisteredOAuth2AuthorizedClient("felord-auth") OAuth2AuthorizedClient client) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Map<String, Object> map = new HashMap<>();
    map.put("authentication", authentication);
    map.put("oAuth2AuthorizedClient", client);
    return map;
  }

  /**
   * 登录成功后默认跳转页.
   *
   * @param authentication 当前认证信息
   * @return the map
   */
  @GetMapping("/")
  public Map<String, Object> index(
      @RegisteredOAuth2AuthorizedClient("felord-oidc") OAuth2AuthorizedClient client,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("authentication", authentication);
    map.put("client", client);
    return map;
  }

  /**
   * 登录成功后默认跳转页.
   *
   * @return the map
   */
  @GetMapping("/code13")
  public Map<String, Object> code13() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("code", 0);
    map.put("msg", "这是一个受保护的接口");
    return map;
  }
}
