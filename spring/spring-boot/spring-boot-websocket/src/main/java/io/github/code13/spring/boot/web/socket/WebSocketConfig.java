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

package io.github.code13.spring.boot.web.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocketConfig.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/1 21:44
 */
@Configuration(proxyBeanMethods = true)
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final MyWebSocketHandler myWebSocketHandler;

  public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
    this.myWebSocketHandler = myWebSocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(myWebSocketHandler, "/socketHandler")
        .setAllowedOrigins("*")
        .addInterceptors(new MyHandshakeInterceptor());
  }

  @Bean
  static MyWebSocketHandler myWebSocketHandler() {
    return new MyWebSocketHandler();
  }
}
