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
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.helpers.MessageFormatter;

/**
 * Utils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 20:35
 */
final class Utils {

  public static <T> T cast(Object o, Class<T> type) {
    return type.cast(o);
  }

  public static <T> void startParse(String taskName, Consumer<T> consumer, T args) {
    System.out.println("---------------------" + taskName + "--------------------");
    consumer.accept(args);
  }

  public static void print(String format, Object... args) {
    System.err.println(MessageFormatter.arrayFormat(format, args).getMessage());
  }

  public static SQLSelectQuery parseSQLSelectQuery(String sql) {
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
    SQLSelectStatement sqlSelectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    SQLSelect select = sqlSelectStatement.getSelect();
    return select.getQuery();
  }

  public static void parseSQLBinaryOpExpr(SQLBinaryOpExpr conditionBinary) {
    SQLExpr conditionExpr = conditionBinary.getLeft();
    SQLExpr conditionValueExpr = conditionBinary.getRight();
    // 左边有别名所以是SQLPropertyExpr
    if (conditionExpr instanceof SQLPropertyExpr) {
      SQLPropertyExpr conditionColumnExpr = cast(conditionExpr, SQLPropertyExpr.class);
      // 右边根据类型进行转换 id是SQLIntegerExpr name是SQLCharExpr
      SQLValuableExpr conditionColumnValue = cast(conditionValueExpr, SQLValuableExpr.class);
      print(
          "条件列名:{},条件别名:{},条件值:{}",
          conditionColumnExpr.getName(),
          conditionColumnExpr.getOwnernName(),
          conditionColumnValue);
    }
    // 如果没有别名
    if (conditionExpr instanceof SQLIdentifierExpr) {
      SQLIdentifierExpr conditionColumnExpr = cast(conditionExpr, SQLIdentifierExpr.class);
      SQLValuableExpr conditionColumnValue = cast(conditionValueExpr, SQLValuableExpr.class);
      print("条件列名:{},条件值:{}", conditionColumnExpr.getName(), conditionColumnValue);
    }
  }

  /**
   * 判断where要 1. 注意是SQLBinaryOpExpr(id = 1) or (u.id = 1) 需要注意是否使用了别名<br>
   * 2. 注意如果只有一个查询添加 where本身就是一个SQLBinaryOpExpr，如果是多个就要用 where.getChildren()<br>
   * </> 如果有别名: SQLPropertyExpr(name = id , ownerName = u)<br>
   * 如果没别名: SQLIdentifierExpr(name = id) <br>
   * </> 值对象: SQLValuableExpr
   *
   * @param where 条件对象
   */
  public static void parseWhere(SQLExpr where) {
    if (where instanceof SQLBinaryOpExpr) {
      parseSQLBinaryOpExpr(cast(where, SQLBinaryOpExpr.class));
    } else {
      List<SQLObject> childrenList = where.getChildren();
      for (SQLObject sqlObject : childrenList) {
        // 包含了 left 和 right
        SQLBinaryOpExpr conditionBinary = cast(sqlObject, SQLBinaryOpExpr.class);
        parseSQLBinaryOpExpr(conditionBinary);
      }
    }
  }
}
