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

package io.github.code13.javastack.spring.boot.crmlc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * MultilevelCacheApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/16 22:15
 */
@SpringBootApplication
@EnableCaching
public class MultilevelCacheApp {

  public static void main(String[] args) {
    SpringApplication.run(MultilevelCacheApp.class, args);
  }
}
