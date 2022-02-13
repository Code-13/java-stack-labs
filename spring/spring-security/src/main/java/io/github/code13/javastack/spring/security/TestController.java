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

package io.github.code13.javastack.spring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/9 21:27
 */
@RestController
public class TestController {

  /** 这个接口会被spring security 拦截. */
  @GetMapping(value = "/api/v1/test1")
  public Object get() {
    return "认证成功，直接访问";
  }

  /** 这个接口没有被 Spring Security 拦截.可以直接访问 */
  @GetMapping(value = "/no/api/v1/test1")
  public Object get1() {
    return "这个接口没有被 Spring Security 拦截.可以直接访问";
  }

  /** 需要ROLE_OTHER权限才可以访问 */
  @PreAuthorize("hasAuthority('ROLE_OTHER')")
  @GetMapping(value = "/authorities/api/v1/test1")
  public Object authorities() {
    return "authorities";
  }
}
