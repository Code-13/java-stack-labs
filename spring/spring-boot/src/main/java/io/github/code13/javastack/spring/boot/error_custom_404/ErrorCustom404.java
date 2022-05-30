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

package io.github.code13.javastack.spring.boot.error_custom_404;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 自定义 404 错误处理.
 *
 * <p>配置文件添加如下配置：
 *
 * <pre>
 *   spring.mvc.throw-exception-if-no-handler-found=true
 *   spring.web.resources.add-mappings=false
 * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/30 16:29
 */
@SpringBootApplication
public class ErrorCustom404 {

  public static void main(String[] args) {
    SpringApplication.run(ErrorCustom404.class, args);
  }

  @ControllerAdvice
  static class ExceptionHandlerClass {

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object noHandlerFoundException(NoHandlerFoundException e) {
      Map<Object, Object> error = Map.of("url", e.getRequestURL(), "method", e.getHttpMethod());
      return Map.of(
          "code",
          404,
          "msg",
          "handler not found",
          "error",
          error,
          "timestamp",
          System.currentTimeMillis());
    }
  }
}
