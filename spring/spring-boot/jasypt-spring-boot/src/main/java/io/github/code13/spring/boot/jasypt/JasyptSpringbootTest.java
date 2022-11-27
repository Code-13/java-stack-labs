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

package io.github.code13.spring.boot.jasypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import javax.sql.DataSource;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * JasyptSpringbootTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/27 12:09
 */
@SpringBootTest
public class JasyptSpringbootTest {

  @Autowired StringEncryptor encryptor;
  @Autowired Environment environment;

  @Autowired DataSource dataSource;

  @Test
  @DisplayName("testJasypt")
  void testJasypt() {
    System.out.println(encryptor.encrypt("root"));
    System.out.println(
        encryptor.decrypt("0ufwLPLtMC2HRRs62EoSfVIUN+pZAaNmV2hTNH34Wfg+P4uHQwcw8auaxEVXtstu"));
  }

  @Test
  @DisplayName("encryptUrl")
  void encryptUrl() {
    System.out.println(encryptor.encrypt("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"));
  }

  @Test
  @DisplayName("测试链接属性不是加密后")
  void testUrlIsCorrect() {
    String url = environment.getProperty("spring.datasource.url", String.class);
    assertEquals("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", url);
  }

  @Test
  @DisplayName("testDataSourceIsNotWrong")
  void testDataSourceIsNotWrong() {
    Assertions.assertDoesNotThrow(
        () -> {
          Connection connection = dataSource.getConnection();
          System.out.println(connection);
          // HikariProxyConnection@524876402 wrapping conn0: url=jdbc:h2:mem:testdb user=SA
        });
  }
}
