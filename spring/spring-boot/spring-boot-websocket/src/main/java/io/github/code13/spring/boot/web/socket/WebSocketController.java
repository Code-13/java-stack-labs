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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * WebSocketController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/12/1 22:14
 */
@Controller
public class WebSocketController {

  private final MyWebSocketHandler myWebSocketHandler;

  public WebSocketController(MyWebSocketHandler myWebSocketHandler) {
    this.myWebSocketHandler = myWebSocketHandler;
  }

  @GetMapping("/index.html")
  public String index() {
    return "index";
  }

  @GetMapping("/error/{id}")
  @ResponseBody
  public String sendError(@PathVariable String id) {
    myWebSocketHandler.sendMessageToClient("Monitor", id);
    return "success";
  }
}
