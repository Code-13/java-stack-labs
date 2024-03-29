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

import static io.github.code13.libs.alibaba.druid.ast.Utils.cast;
import static io.github.code13.libs.alibaba.druid.ast.Utils.print;
import static io.github.code13.libs.alibaba.druid.ast.Utils.startParse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * SqlQueryTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 20:55
 * @see SQLExpr
 */
class SqlQueryTest {

  /** 1. 简单的查询语句 2. 包含子查询的查询语句 */
  @Test
  void sqlQueryTest() {
    String sql =
        "select u.id as userId, u.name as userName, age as userAge from users as u where u.id = 1 and u.name = '孙悟空' limit 2,10";
    // 解析SQL
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
    // 因为我们的sql是一个查询语句,所以根据我们上面的介绍就是一个SQLSelectStatement
    SQLSelectStatement sqlSelectStatement = cast(sqlStatement, SQLSelectStatement.class);
    // 从查询语句顶级抽象中获取查询对象
    SQLSelect select = sqlSelectStatement.getSelect();
    SQLSelectQuery query = select.getQuery();
    // SQLSelectQuery 有两个实现 SQLSelectQueryBlock 和 SQLUnionQuery。这里我们先用SQLSelectQueryBlock举例
    SQLSelectQueryBlock queryBlock = cast(query, SQLSelectQueryBlock.class);
    // 首先我们拿到语句的from对象
    SQLTableSource from = queryBlock.getFrom();
    startParse("解析From", this::parseFrom, from);
    // 首先我们先拿到查询的字段,这里因为用到了别名
    List<SQLSelectItem> selectColumnList = queryBlock.getSelectList();
    startParse("解析SQLSelectItem", this::parseSQLSelectItem, selectColumnList);
    // 拿到查询条件 u.id = 1 and u.name = '孙悟空' // 因为使用到了别名
    SQLExpr where = queryBlock.getWhere();
    startParse("解析Where", this::parseWhere, where);
    // 解析limit
    SQLLimit limit = queryBlock.getLimit();
    startParse("解析SQLLimit", this::parseLimit, limit);
  }

  private void parseFrom(SQLTableSource from) {
    // from 对象同时有 4个实现,可以看上面的介绍.这里因为是一个最简单的查询,所以就是SQLExprTableSource
    SQLExprTableSource fromTableSource = cast(from, SQLExprTableSource.class);
    SQLName name = fromTableSource.getName();
    String alias = fromTableSource.getAlias();
    // 首先我们先拿到要查询的表是哪个,并且判断是否有别名
    print("表名:{},别名:{}", name, alias);
  }

  /**
   * 解析查询字段,注意是否使用了别名.u.id as userId, u.name as userName, u.age as userAge<br>
   * userId（sqlSelectItem.getAlias）<br>
   * 如果有别名: u.id( id = SQLPropertyExpr.getName,u = SQLPropertyExpr.getOwnernName)<br>
   * 如果没别名: id(id = SQLIdentifierExpr.name)
   *
   * @param selectColumnList 查询字段
   */
  private void parseSQLSelectItem(List<SQLSelectItem> selectColumnList) {
    for (SQLSelectItem sqlSelectItem : selectColumnList) {
      // u.id as userId(selectColumnAlias)
      String selectColumnAlias = sqlSelectItem.getAlias();
      // u.id = SQLPropertyExpr
      SQLExpr expr = sqlSelectItem.getExpr();
      if (expr instanceof SQLPropertyExpr) {
        SQLPropertyExpr selectColumnExpr = cast(expr, SQLPropertyExpr.class);
        print(
            "列名:{},别名:{},表别名:{}",
            selectColumnExpr.getName(),
            selectColumnAlias,
            selectColumnExpr.getOwnernName());
      }
      if (expr instanceof SQLIdentifierExpr) {
        SQLIdentifierExpr selectColumnExpr = cast(expr, SQLIdentifierExpr.class);
        print("列名:{},别名:{}", selectColumnExpr.getName(), selectColumnAlias);
      }
    }
  }

  /**
   * 判断where要 1. 注意是SQLBinaryOpExpr(id = 1) or (u.id = 1) 需要注意是否使用了别名<br>
   * 2. 注意如果只有一个查询添加 where本身就是一个SQLBinaryOpExpr，如果是多个就要用 where.getChildren() 如果有别名:
   * SQLPropertyExpr(name = id , ownerName = u)<br>
   * 如果没别名: SQLIdentifierExpr(name = id) <br>
   * </> 值对象: SQLValuableExpr
   *
   * @param where 条件对象
   */
  private void parseWhere(SQLExpr where) {
    List<SQLObject> childrenList = where.getChildren();
    for (SQLObject sqlObject : childrenList) {
      // 包含了 left 和 right
      SQLBinaryOpExpr conditionBinary = cast(sqlObject, SQLBinaryOpExpr.class);
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
  }

  /**
   * 偏移量,只有2个值
   *
   * @param limit 限制
   */
  private void parseLimit(SQLLimit limit) {
    // 偏移量
    SQLExpr offset = limit.getOffset();
    // 便宜数量
    SQLExpr rowCount = limit.getRowCount();
    print("偏移量:{},偏移数量:{}", offset, rowCount);
  }
}
