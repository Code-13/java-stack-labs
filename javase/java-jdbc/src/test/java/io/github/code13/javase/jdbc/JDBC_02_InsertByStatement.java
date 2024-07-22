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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JDBC_02_InsertByStatement.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/22 13:33
 */
public class JDBC_02_InsertByStatement extends BaseJdbcRunner {

  @BeforeEach
  void setup1() throws SQLException {
    Statement statement = connection.createStatement();

    statement.execute(
        """
         CREATE TABLE `user_balance` (
           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
           `name` VARCHAR(45) NOT NULL ,
           `balance` BIGINT NOT NULL ,
           PRIMARY KEY (`id`)
         )
         ENGINE = InnoDB
         DEFAULT CHARACTER SET = utf8mb4
         COLLATE = utf8mb4_general_ci;
         """);

    statement.close();
  }

  @Test
  @DisplayName("test_insert1")
  void test_insert1() throws SQLException {
    try (Statement statement = connection.createStatement()) {
      String sql =
          String.format(
              "INSERT INTO user_balance(name, balance) VALUES('%s', %s)", "zhangsan", 1000L);

      int affectRowsNum = statement.executeUpdate(sql);
      System.out.println("影响的行数：" + affectRowsNum);
    }
  }

  @Test
  @DisplayName("test_insert2")
  void test_insert2() throws SQLException {
    try (Statement statement = connection.createStatement()) {
      String sql =
          String.format("INSERT INTO user_balance(name, balance) VALUES('%s', %s)", "lisi", 1001L);

      // 指定第2个参数为Statement.RETURN_GENERATED_KEYS，可以获取生成的主键id值
      int affectRowsNum = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
      System.out.println("影响的行数：" + affectRowsNum);

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          System.out.println("生成的主键ID是：" + generatedKeys.getLong(1));
        }
      }
    }
  }
}
