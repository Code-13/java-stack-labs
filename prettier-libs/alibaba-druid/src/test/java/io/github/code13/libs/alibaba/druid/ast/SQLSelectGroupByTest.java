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
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * SQLSelectGroupByTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 21:31
 */
class SQLSelectGroupByTest {

  @Test
  void groupBy() {
    SQLStatement sqlStatement =
        SQLUtils.parseSingleMysqlStatement(
            "select name,count(1) as count from users group by name,age having count > 2");
    SQLSelectStatement selectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    SQLSelect select = selectStatement.getSelect();
    SQLSelectQueryBlock query = Utils.cast(select.getQuery(), SQLSelectQueryBlock.class);
    SQLSelectGroupByClause groupBy = query.getGroupBy();
    List<SQLExpr> items = groupBy.getItems();
    for (SQLExpr item : items) {
      // group by name
      // group by age
      SQLIdentifierExpr groupByColumn = Utils.cast(item, SQLIdentifierExpr.class);
      Utils.print("group by {}", groupByColumn);
    }
  }

  @Test
  public void having() {
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement("select name,count(1) as count from users group by name,age having count > 2");
    SQLSelectStatement selectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    SQLSelect select = selectStatement.getSelect();
    SQLSelectQueryBlock query = Utils.cast(select.getQuery(), SQLSelectQueryBlock.class);
    SQLSelectGroupByClause groupBy = query.getGroupBy();
    SQLExpr having = groupBy.getHaving();
    // 因为只有一个条件,所以having就是SQLBinaryOpExpr
    SQLBinaryOpExpr havingExpr = Utils.cast(having, SQLBinaryOpExpr.class);
    // 没有使用别名,所以就是SQLIdentifierExpr
    SQLExpr left = havingExpr.getLeft();
    SQLIdentifierExpr leftExpr = Utils.cast(left, SQLIdentifierExpr.class);
    // 数字类型就是
    SQLExpr right = havingExpr.getRight();
    SQLValuableExpr rightValue = Utils.cast(right, SQLValuableExpr.class);
    SQLBinaryOperator operator = havingExpr.getOperator();
    // left:count, operator:>,right:2
    Utils.print("left:{}, operator:{},right:{}", leftExpr.getName(), operator.name, rightValue.getValue());
  }
}
