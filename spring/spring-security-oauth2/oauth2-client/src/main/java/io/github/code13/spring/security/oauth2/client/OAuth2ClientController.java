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

package io.github.code13.spring.security.oauth2.client;

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
   * client idm.
   *
   * @return the map
   */
  @GetMapping("/iam")
  public Map<String, Object> bar(
      @RegisteredOAuth2AuthorizedClient("iam") OAuth2AuthorizedClient client) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Map<String, Object> map = new HashMap<>();
    map.put("authentication", authentication);
    map.put("oAuth2AuthorizedClient", client);
    return map;
  }

  /**
   * client iam-oidc.
   *
   * @param authentication 当前认证信息
   * @return the map
   */
  @GetMapping("/iam-oidc")
  public Map<String, Object> index(
      @RegisteredOAuth2AuthorizedClient("iam-oidc") OAuth2AuthorizedClient client,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("authentication", authentication);
    map.put("client", client);
    return map;
  }

  /**
   * 受保护接口.
   *
   * @return the map
   */
  @GetMapping("/")
  public Map<String, Object> code13() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    HashMap<String, Object> map = new HashMap<>();
    map.put("code", 0);
    map.put("msg", "Spring Security OAuth2 Client 与 Spring Security Authorization Server 集成成功");
    map.put("principal", authentication.getPrincipal());
    return map;
  }
}
