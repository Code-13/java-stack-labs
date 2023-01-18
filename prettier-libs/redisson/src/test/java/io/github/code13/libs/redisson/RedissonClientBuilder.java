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

package io.github.code13.libs.redisson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * RedissonClientBuilder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/26 21:44
 */
public final class RedissonClientBuilder {

  public static RedissonClient build(String host, int port, String password, int dataBase) {
    return doBuild(host, port, password, dataBase);
  }

  public static RedissonClient build() {
    return build("127.0.0.1", 6379, "", 0);
  }

  private static RedissonClient doBuild(String host, int port, String password, int dataBase) {
    Config config = new Config();
    SingleServerConfig singleServerConfig =
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setDatabase(dataBase);

    if (StringUtils.isNotBlank(password)) {
      singleServerConfig.setPassword(password);
    }

    // ObjectMapper objectMapper = buildObjectMapper();
    // config.setCodec(new JsonJacksonCodec(objectMapper));

    return Redisson.create(config);
  }

  private static ObjectMapper buildObjectMapper() {
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    javaTimeModule.addSerializer(
        LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
    javaTimeModule.addDeserializer(
        LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

    // 处理LocalDate
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

    // 处理LocalTime
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
    javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

    var objectMapper = new ObjectMapper();
    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }

  private RedissonClientBuilder() {
    throw new UnsupportedOperationException();
  }
}
