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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SseEmitterController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/3 21:07
 */
@RestController
@RequestMapping("/spring/boot/sse")
public class SseEmitterController {

  @GetMapping("/")
  public SseEmitter sseEmitter() {
    SseEmitter sseEmitter = new Utf8SseEmitter();
    sseEmitter.onTimeout(() -> System.out.println("TimeOut"));
    sseEmitter.onError(
        throwable -> {
          System.out.println(throwable.getMessage());
        });
    sseEmitter.onCompletion(() -> System.out.println("Completion"));
    SseEmitterHandler handler = new SseEmitterHandler(sseEmitter);
    return handler.handle();
  }

  static class Utf8SseEmitter extends SseEmitter {

    public Utf8SseEmitter() {
      super(0L);
    }

    @Override
    protected void extendResponse(ServerHttpResponse outputMessage) {
      super.extendResponse(outputMessage);

      HttpHeaders headers = outputMessage.getHeaders();
      if (headers.getContentType() == null) {
        headers.setContentType(new MediaType(MediaType.TEXT_EVENT_STREAM, StandardCharsets.UTF_8));
      }
    }
  }

  static class SseEmitterHandler {

    final ObjectMapper objectMapper = new ObjectMapper();

    private final SseEmitter sseEmitter;
    private final Executor executor = Executors.newFixedThreadPool(5);

    SseEmitterHandler(SseEmitter sseEmitter) {
      this.sseEmitter = sseEmitter;
    }

    private SseEmitter handle() {
      executor.execute(
          () -> {
            try {
              for (int i = 0; i < 100; i++) {
                sseEmitter.send(
                    SseEmitter.event()
                        // .id(String.valueOf(System.currentTimeMillis()))
                        .data(Map.of("a", "b", "c", i)));

                TimeUnit.MILLISECONDS.sleep(50);
              }

              //              sseEmitter.send(
              //                  SseEmitter.event()
              //                      // .id(String.valueOf(System.currentTimeMillis()))
              //                      // .name("COMPLETE")
              //                      .data("{COMPLETE}"));

              Map<String, Object> map = Map.of("a", "b", "c", "d", "e", "f", "g", Map.of("k", "m"));

              sseEmitter.send(
                  SseEmitter.event()
                      // .id(String.valueOf(System.currentTimeMillis()))
                      .data(objectMapper.writeValueAsString(map)));

              sseEmitter.complete();
            } catch (Exception e) {
              sseEmitter.completeWithError(e);
            }
          });
      return sseEmitter;
    }

    void sendMessage(String message) {
      try {
        sseEmitter.send(SseEmitter.event().data(message));
      } catch (IOException e) {
        //        sseEmitter.completeWithError(e);
      }
    }

    void complete() {
      sseEmitter.complete();
    }
  }
}
