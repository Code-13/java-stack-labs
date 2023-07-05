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

package io.github.code13.spring.boot.web.sse;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * SseConfiguration.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/4 12:20
 */
@Configuration
public class SseConfiguration {

  @Bean
  public FilterRegistrationBean<Filter> sseAuthFilter() {
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(
        (request, response, chain) -> {
          HttpServletRequest servletRequest = (HttpServletRequest) request;
          String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
          if (!StringUtils.hasText(authorization)) {
            response.getWriter().write("缺少授权");
            return;
          }

          chain.doFilter(servletRequest, response);
        });
    return bean;
  }
}
