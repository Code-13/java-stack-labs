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

package io.github.code13.libs.alibaba.druid.ast;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SqlStatementTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 20:32
 * @see com.alibaba.druid.sql.ast.SQLStatement
 */
class SqlStatementTest {

  /*
   * SqlStatement DQL & DML顶级抽象 DQL 数据查询语言 select DML 数据操纵语言 insert update delete
   *
   * 最常用的Statement当然是SELECT/UPDATE/DELETE/INSERT，他们分别是
   * 核心类                说明
   * SQLSelectStatement 查询语句
   * SQLUpdateStatement 更新语句
   * SQLDeleteStatement 删除语句
   * SQLInsertStatement 新增语句
   */

  @Test
  void statement() {
    // 以下全部 true
    System.out.println(
        SQLUtils.parseSingleMysqlStatement("select * from users") instanceof SQLSelectStatement);

    System.out.println(
        SQLUtils.parseSingleMysqlStatement("insert into users(id,name,age) values (1,'孙悟空',500)")
            instanceof SQLInsertStatement);

    System.out.println(
        SQLUtils.parseSingleMysqlStatement("update users set name = '唐僧' where id = 1 ")
            instanceof SQLUpdateStatement);

    System.out.println(
        SQLUtils.parseSingleMysqlStatement("delete from users where id = 1")
            instanceof SQLDeleteStatement);
  }

  @Test
  @DisplayName("sqlSelectStatement")
  void sqlSelectStatement() {
    SQLStatement sqlStatement =
        SQLUtils.parseSingleMysqlStatement("select * from user where name = '唐僧' and age = 18");

    SQLSelectStatement sqlSelectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    System.out.println(sqlSelectStatement);

    SQLSelect select = sqlSelectStatement.getSelect();
    System.out.println(select);

    SQLSelectQuery query = select.getQuery();
    SQLSelectQueryBlock selectQueryBlock = Utils.cast(query, SQLSelectQueryBlock.class);

    List<SQLSelectItem> selectList = selectQueryBlock.getSelectList();
    SQLTableSource from = selectQueryBlock.getFrom();

    System.out.println(sqlSelectStatement.getChildren());

  }

  @Test
  void SQLDeleteStatement() {
    SQLStatement sqlStatement =
        SQLUtils.parseSingleMysqlStatement("delete from users where id = 1");
    SQLDeleteStatement sqlDeleteStatement = Utils.cast(sqlStatement, SQLDeleteStatement.class);
    sqlDeleteStatement.addCondition(SQLUtils.toSQLExpr("name = '孙悟空'"));

    //        DELETE FROM users
    //        WHERE id = 1
    //        AND name = '孙悟空'
    System.out.println(SQLUtils.toSQLString(sqlDeleteStatement));
  }

  @Test
  void SQLDeleteStatement2() {
    SQLStatement sqlStatement =
        SQLUtils.parseSingleMysqlStatement("delete from users where id = 1");
    SQLDeleteStatement sqlDeleteStatement = Utils.cast(sqlStatement, SQLDeleteStatement.class);
    SQLExpr where = sqlDeleteStatement.getWhere();
    SQLBinaryOpExpr sqlBinaryOpExpr = Utils.cast(where, SQLBinaryOpExpr.class);

    //        DELETE FROM users
    //        WHERE id = 2
    sqlBinaryOpExpr.setRight(SQLUtils.toSQLExpr("2"));
    System.out.println(SQLUtils.toSQLString(sqlDeleteStatement));
  }

  @Test
  void SQLUpdateStatement() {
    SQLStatement sqlStatement =
        SQLUtils.parseSingleMysqlStatement(
            "update users u set u.name = '唐僧',age = 18 where u.id = 1 ");
    SQLUpdateStatement sqlUpdateStatement = Utils.cast(sqlStatement, SQLUpdateStatement.class);
    List<SQLUpdateSetItem> setItems = sqlUpdateStatement.getItems();
    for (SQLUpdateSetItem setItem : setItems) {
      SQLExpr column = setItem.getColumn();
      if (column instanceof SQLPropertyExpr) {
        SQLPropertyExpr sqlPropertyExpr = Utils.cast(column, SQLPropertyExpr.class);
        SQLExpr value = setItem.getValue();
        Utils.print(
            "column:{},列owner:{},value:{}",
            sqlPropertyExpr.getName(),
            sqlPropertyExpr.getOwnernName(),
            value);
      }
      if (column instanceof SQLIdentifierExpr) {
        SQLExpr value = setItem.getValue();
        Utils.print("column:{},value:{}", column, value);
      }
    }
    SQLExpr where = sqlUpdateStatement.getWhere();
    Utils.startParse("解析where", Utils::parseWhere, where);
  }
}
