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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * MyWebSocketHandler.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/2 09:48
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

  private final ConcurrentMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String client = getClient(session);
    if (StringUtils.hasText(client)) {
      sessionMap.putIfAbsent(client, session);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String clientMessage = message.getPayload();

    if (clientMessage.startsWith("hello") || clientMessage.startsWith("greet")) {
      session.sendMessage(
          new TextMessage(
              "Hello, "
                  + getClient(session)
                  + "! It is Server "
                  + ". Time: "
                  + LocalDateTime.now()));

    } else {
      session.sendMessage(new TextMessage("Unknown command"));
    }
  }

  private String getClient(WebSocketSession session) {
    return ((String) session.getAttributes().get("client"));
  }

  public void sendMessageToClient(String client, String message) {
    WebSocketSession session = sessionMap.get(client);
    if (session == null) {
      return;
    }

    if (session.isOpen()) {
      try {
        session.sendMessage(new TextMessage(message));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
