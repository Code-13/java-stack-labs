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

import io.github.code13.spring.security.oauth2.client.sso.EnableSsoOnClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OAuth2ClientApplication.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/2/15 21:56
 */
@EnableSsoOnClient
@SpringBootApplication
public class OAuth2ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(OAuth2ClientApplication.class, args);
  }
}
