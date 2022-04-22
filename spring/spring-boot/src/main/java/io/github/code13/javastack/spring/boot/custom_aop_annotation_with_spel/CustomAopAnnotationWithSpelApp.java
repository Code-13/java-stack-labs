/*
 *
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

package io.github.code13.javastack.spring.boot.custom_aop_annotation_with_spel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CustomAopAnnotationWithSpelApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/22 10:01
 */
@SpringBootApplication
@Import(InterestResolveElAspect.class)
class CustomAopAnnotationWithSpelApp {

  public static void main(String[] args) {
    SpringApplication.run(CustomAopAnnotationWithSpelApp.class, args);
  }

  @RestController
  static class TestController {

    /**
     * <a href="http://127.0.0.1:8080/?name=foo&age=66">访问此链接观察结果</a>
     *
     * @param user .
     * @return .
     */
    @GetMapping("/")
    @Interest(key = "#user.name", unless = "#user.age > 18")
    public Object interest(User user) {
      return user;
    }
  }

  record User(String name, Integer age) {}
}
