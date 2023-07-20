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
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * AliasVisitorTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 21:50
 */
class AliasVisitorTest {

  public static class CustomerMySqlASTVisitorAdapter extends MySqlASTVisitorAdapter {
    private final Map<String, SQLTableSource> ALIAS_MAP = new HashMap<String, SQLTableSource>();

    private final Map<String, SQLExpr> ALIAS_COLUMN_MAP = new HashMap<String, SQLExpr>();

    public boolean visit(SQLExprTableSource x) {
      String alias = x.getAlias();
      ALIAS_MAP.put(alias, x);
      return true;
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
      List<SQLSelectItem> selectList = x.getSelectList();
      for (SQLSelectItem sqlSelectItem : selectList) {
        String alias = sqlSelectItem.getAlias();
        SQLExpr expr = sqlSelectItem.getExpr();
        ALIAS_COLUMN_MAP.put(alias, expr);
      }
      return true;
    }

    public Map<String, SQLTableSource> getAliasMap() {
      return ALIAS_MAP;
    }

    public Map<String, SQLExpr> getAliasColumnMap() {
      return ALIAS_COLUMN_MAP;
    }
  }

  @Test
  void aliasVisitor() {
    String sql =
        "select u.id as userId, u.name as userName, age as userAge from users as u where u.id = 1 and u.name = '孙悟空' limit 2,10";
    // 解析SQL
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
    CustomerMySqlASTVisitorAdapter customerMySqlASTVisitorAdapter =
        new CustomerMySqlASTVisitorAdapter();
    sqlStatement.accept(customerMySqlASTVisitorAdapter);
    // 表别名:{u=users}
    System.out.println("表别名:" + customerMySqlASTVisitorAdapter.getAliasMap());
    // 列别名{userName=u.name, userId=u.id, userAge=age}
    System.out.println("列别名" + customerMySqlASTVisitorAdapter.getAliasColumnMap());
  }
}
