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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JDBC_10_Transaction.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/22 14:33
 */
public class JDBC_10_Transaction extends BaseJdbcRunner {

  /*
   * connection.setAutoCommit(false)：开启事务。
   *
   * connection.commit();：提交事务。
   *
   * connection.rollback(); 回滚事务。
   *
   * 默认情况下 connection 的 autoCommit 属性为 true，可以理解为任何一个SQL的执行，都会马上生效，都是在一个事务里，这种是隐式的事务，不需要写rollback、commit的代码。
   *
   * 当通过connection.setAutoCommit(false)开启事务后，必须要显式的进行 commit 或者 rollback。rollback 一般在出现异常时进行。
   */

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

  public void insert(String name, Long balance) throws SQLException {
    try (PreparedStatement pstmt =
        connection.prepareStatement("INSERT INTO user_balance(name, balance) VALUES(?, ?)")) {
      pstmt.setString(1, name);
      pstmt.setLong(2, balance);
      int affectRowsNum = pstmt.executeUpdate();
      System.out.println("影响的行数：" + affectRowsNum);
    }
  }

  public void raiseException(boolean raise) {
    if (raise) {
      throw new RuntimeException("异常");
    }
  }

  @Test
  public void test01() throws SQLException, ClassNotFoundException {
    connection.setAutoCommit(false);
    insert("zhangsan", 10000L);
  }

  @Test
  public void test02() throws SQLException, ClassNotFoundException {
    connection.setAutoCommit(false);
    try {
      insert("letian", 10000L);
      insert("xiaosi", 10000L);
      raiseException(true); // 产生一个异常
      System.out.println("commit");
      connection.commit();
    } catch (Exception ex) {
      System.out.println("rollback");
      connection.rollback();
    }
  }

  @Test
  public void test03() throws SQLException, ClassNotFoundException {
    connection.setAutoCommit(false);
    try {
      insert("zhangsan", 10000L);
      insert("lisi", 10000L);
      raiseException(false); // 不产生异常
      System.out.println("commit");
      connection.commit();
    } catch (Exception ex) {
      System.out.println("rollback");
      connection.rollback();
    }
  }
}
