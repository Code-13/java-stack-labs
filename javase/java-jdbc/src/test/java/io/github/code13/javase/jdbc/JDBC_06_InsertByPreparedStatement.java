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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JDBC_06_InsertByPreparedStatement.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/22 14:12
 */
public class JDBC_06_InsertByPreparedStatement extends BaseJdbcRunner {

  @BeforeEach
  void setup1() throws SQLException {
    try (Statement statement = connection.createStatement()) {
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
    }
  }

  @Test
  void test_insert() throws SQLException {
    PreparedStatement pstmt =
        connection.prepareStatement("INSERT INTO user_balance(name, balance) VALUES(?, ?)");
    try (pstmt) {
      pstmt.setString(1, "zhangsan");
      pstmt.setLong(2, 1000L);

      int affectRowsNum = pstmt.executeUpdate();
      System.out.println("影响的行数：" + affectRowsNum);
    }
  }

  @Test
  void test_insert_2() throws SQLException {
    PreparedStatement pstmt =
        connection.prepareStatement(
            "INSERT INTO user_balance(name, balance) VALUES(?, ?)",
            Statement.RETURN_GENERATED_KEYS);

    try (pstmt) {
      pstmt.setString(1, "zhangsan");
      pstmt.setLong(2, 1000L);

      int affectRowsNum = pstmt.executeUpdate();
      System.out.println("影响的行数：" + affectRowsNum);

      try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          System.out.println("生成的主键ID是：" + generatedKeys.getLong(1));
        }
      }
    }
  }
}
