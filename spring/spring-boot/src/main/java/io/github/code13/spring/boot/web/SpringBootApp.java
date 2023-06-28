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

package io.github.code13.spring.boot.web;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * SpringBootApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/6/28 16:40
 */
@SpringBootApplication
public class SpringBootApp {

  public static void main(String[] args) {
    new SpringApplicationBuilder(SpringBootApp.class).web(WebApplicationType.SERVLET).run(args);
  }
}
