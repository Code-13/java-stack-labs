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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JDBC_09_Batch.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/22 14:26
 */
public class JDBC_09_Batch extends BaseJdbcRunner {

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
  @DisplayName("test_statementExecuteBatch")
  void test_statementExecuteBatch() throws SQLException {
    try (Statement stmt = connection.createStatement()) {
      stmt.addBatch("INSERT INTO user_balance(name, balance) VALUES('zhangsan', 1000)");
      stmt.addBatch("INSERT INTO user_balance(name, balance) VALUES('lisi', 1001)");

      int[] affectRowsArray = stmt.executeBatch();
      System.out.println("影响的行数：" + Arrays.toString(affectRowsArray));
    }
  }

  @Test
  @DisplayName("test_preparedStatementExecuteBatch")
  void test_preparedStatementExecuteBatch() throws SQLException {
    PreparedStatement pstmt =
        connection.prepareStatement("INSERT INTO user_balance(name, balance) VALUES(?, ?)");

    try (pstmt) {
      pstmt.setString(1, "letian");
      pstmt.setLong(2, 1000L);
      pstmt.addBatch();
      pstmt.setString(1, "xiaosi");
      pstmt.setLong(2, 1001L);
      pstmt.addBatch();

      int[] affectRowsArray = pstmt.executeBatch();
      System.out.println("影响的行数：" + Arrays.toString(affectRowsArray));
    }
  }
}
