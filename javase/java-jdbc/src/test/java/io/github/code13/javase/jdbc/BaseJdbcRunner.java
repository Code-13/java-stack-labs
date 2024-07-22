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

package io.github.code13.javase.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * BaseJdbcRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/18 14:06
 */
public class BaseJdbcRunner {

  private static final String jdbcURL = "jdbc:h2:mem:jdbc_test;MODE=MySQL";
  private static final String jdbcUsername = "sa";
  private static final String jdbcPassword = "";

  private static final String JDBC_DRIVER = "org.h2.Driver";

  protected Connection connection;

  @BeforeEach
  void setup() throws SQLException, ClassNotFoundException {
    connection = getConnection();
  }

  @AfterEach
  void tearDown() throws SQLException {
    connection.close();
  }

  /** 获取数据库连接 */
  protected Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(JDBC_DRIVER);
    return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
  }
}
