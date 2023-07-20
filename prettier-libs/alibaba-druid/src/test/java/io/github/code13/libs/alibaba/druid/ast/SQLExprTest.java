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

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
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
 * SQLExprTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 21:37
 */
class SQLExprTest {

  /**
   * select id,name,age from users where id = 1 and name = '孙悟空' - id,name,age 这里SQLExpr - id = 1 和
   * name = 孙悟空 也是SQLExpr
   */
  @Test
  void SQLExpr() {
    String sql = "select id,u.name,age from users as u where id = 1 and name = '孙悟空'"; // 解析SQL
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
    // 因为我们的sql是一个查询语句,所以根据我们上面的介绍就是一个SQLSelectStatement
    SQLSelectStatement sqlSelectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    // 从查询语句顶级抽象中获取查询对象
    SQLSelect select = sqlSelectStatement.getSelect();
    SQLSelectQuery query = select.getQuery();
    // SQLSelectQuery 有两个实现 SQLSelectQueryBlock 和 SQLUnionQuery。这里我们先用SQLSelectQueryBlock举例
    SQLSelectQueryBlock queryBlock = Utils.cast(query, SQLSelectQueryBlock.class);

    // 首先我们拿到语句的from对象
    SQLTableSource from = queryBlock.getFrom();
    // from 对象同时有 4个实现,可以看上面的介绍.这里因为是一个最简单的查询,所以就是SQLExprTableSource
    SQLExprTableSource fromTableSource = cast(from, SQLExprTableSource.class);
    SQLName name = fromTableSource.getName();
    String alias = fromTableSource.getAlias();
    // 首先我们先拿到要查询的表是哪个,并且判断是否有别名
    print("表名:{},别名:{}", name, alias);

    // 我们先拿到查询的字段,这里因为用到了别名
    List<SQLSelectItem> selectColumnList = queryBlock.getSelectList();
    for (SQLSelectItem sqlSelectItem : selectColumnList) {
      // id
      SQLExpr expr = sqlSelectItem.getExpr();
      System.out.println(expr);
    }

    // 拿到查询条件 u.id = 1 and u.name = '孙悟空' // 因为使用到了别名
    SQLExpr where = queryBlock.getWhere();
    List<SQLObject> childrenList = where.getChildren();
    for (SQLObject sqlObject : childrenList) {
      System.out.println(sqlObject);
    }
  }
}
