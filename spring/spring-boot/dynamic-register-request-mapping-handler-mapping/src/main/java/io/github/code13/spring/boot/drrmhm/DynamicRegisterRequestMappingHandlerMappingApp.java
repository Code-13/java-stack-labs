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

package io.github.code13.spring.boot.drrmhm;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * DynamicRegisterRequestMappingHandlerMappingApp.
 *
 * <p>动态注册 RequestMappingHandlerMapping
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/6 20:01
 */
@SpringBootApplication
@RestController
class DynamicRegisterRequestMappingHandlerMappingApp {

  public static void main(String[] args) {
    SpringApplication.run(DynamicRegisterRequestMappingHandlerMappingApp.class, args);
  }

  public DynamicRegisterRequestMappingHandlerMappingApp(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  private static final String REQUEST_MAPPING_HANDLER_MAPPING_BEAN_NAME =
      "requestMappingHandlerMapping";

  private final ApplicationContext applicationContext;

  @PostMapping("/spring/boot/request-mapping/register")
  public Object registerMapping(@RequestParam(required = false, defaultValue = "") String path) {

    RequestMappingHandlerMapping handlerMapping =
        getRequestMappingHandlerMapping(applicationContext);

    String registerPath = StringUtils.hasText(path) ? path : genDynamicPath();

    RequestMappingInfo requestMappingInfo = getRequestMappingInfo(registerPath);

    handlerMapping.registerMapping(
        requestMappingInfo,
        this,
        Objects.requireNonNull(
            ReflectionUtils.findMethod(
                DynamicRegisterRequestMappingHandlerMappingApp.class,
                "send",
                HttpServletRequest.class)));

    return registerPath;
  }

  public Object send(HttpServletRequest request) {

    RequestMappingHandlerMapping handlerMapping =
        getRequestMappingHandlerMapping(applicationContext);

    RequestMappingInfo requestMappingInfo = getRequestMappingInfo(request.getRequestURI());

    handlerMapping.unregisterMapping(requestMappingInfo);

    return request.getParameter("code");
  }

  private RequestMappingInfo getRequestMappingInfo(String path) {
    return RequestMappingInfo.paths(path).methods(RequestMethod.POST).params("code").build();
  }

  private String genDynamicPath() {
    String prefix = "/validate/code/";
    return prefix + UUID.randomUUID().toString().replace("-", "");
  }

  private RequestMappingHandlerMapping getRequestMappingHandlerMapping(
      ApplicationContext applicationContext) {

    RequestMappingHandlerMapping requestMappingHandlerMapping;

    try {
      requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
    } catch (NoUniqueBeanDefinitionException e) {
      requestMappingHandlerMapping =
          applicationContext.getBean(
              REQUEST_MAPPING_HANDLER_MAPPING_BEAN_NAME, RequestMappingHandlerMapping.class);
    }

    return requestMappingHandlerMapping;
  }
}
