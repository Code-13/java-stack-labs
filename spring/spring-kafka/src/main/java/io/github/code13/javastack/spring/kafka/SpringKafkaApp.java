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

package io.github.code13.javastack.spring.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringKafkaApp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/4 14:21
 */
@SpringBootApplication
@RestController
public class SpringKafkaApp {

  private static final Logger logger = LoggerFactory.getLogger(SpringKafkaApp.class);

  public static void main(String[] args) {
    SpringApplication.run(SpringKafkaApp.class, args);
  }

  @Autowired private KafkaTemplate<Object, Object> template;

  @GetMapping("/send/{input}")
  public void sendFoo(@PathVariable String input) {
    template.send("topic_input", input);
  }

  @KafkaListener(id = "webGroup", topics = "topic_input")
  public void listen(String input) {
    logger.info("input value: {}", input);
  }
}
