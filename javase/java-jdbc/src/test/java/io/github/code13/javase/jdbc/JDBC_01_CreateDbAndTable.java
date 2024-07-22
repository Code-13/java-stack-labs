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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JDBC_01_CreateDbAndTable.
 *
 * <p>CreateDbAndTableExample
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/7/18 14:00
 */
public class JDBC_01_CreateDbAndTable extends BaseJdbcRunner {

  @Test
  @DisplayName("test")
  void test() throws SQLException {
    System.out.println("当前数据库：" + connection.getCatalog());

    Statement stmt = connection.createStatement();

    // stmt.execute("DROP DATABASE IF EXISTS `bank`");
    // stmt.execute("CREATE DATABASE `bank`");
    // connection.setCatalog("bank");

    stmt.close();

    stmt = connection.createStatement();

    stmt.execute(
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

    // 查看表
    ResultSet tablesResultSet = stmt.executeQuery("SHOW TABLES");

    // 获取查询结果的元信息，例如获取列的数量，列名
    int columnCount = tablesResultSet.getMetaData().getColumnCount();
    System.out.println("查询结果中列的数量: " + columnCount);
    for (int i = 1; i <= columnCount; ++i) { // 注意，index是从1开始
      String columnLabel = tablesResultSet.getMetaData().getColumnLabel(i);
      String columnClassName = tablesResultSet.getMetaData().getColumnClassName(i);
      System.out.printf("第%s列，列名是 %s，类型是 %s%n", i, columnLabel, columnClassName);
    }
    // 从查询结果中获取所有表名。从上面的结果中能看出只有1列，列名是Tables_in_bank，类型是String
    while (tablesResultSet.next()) {
      System.out.println("第1种方式获取表名：" + tablesResultSet.getString("TABLE_NAME"));
      System.out.println("第2种方式获取表名：" + tablesResultSet.getString(1));
    }

    // 查询表的创建语句
    // ResultSet createSqlResultSet = stmt.executeQuery("SHOW CREATE TABLE `user_balance`");
    // while (createSqlResultSet.next()) {
    //   System.out.println("创建语句：" + createSqlResultSet.getString(2)); // 创建语句在查询结果的第2列
    // }

    tablesResultSet.close();
    // createSqlResultSet.close();
    stmt.close();
  }
}
