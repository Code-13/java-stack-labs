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
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * SqlParseTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 21:23
 */
class SqlParseTest {

  @Test
  void set() {
    String sql = "select * from t where t.id in (select id from users)";
    List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
    SQLStatement sqlStatement = sqlStatements.get(0);
    System.out.println(sqlStatement.getChildren());
  }

  @Test
  void set2() {
    String sql = "select u.id,u.name from user as u where u.id = 1 and u.name = 'xi' and u.age = ?";
    // 新建 MySQL Parser
    SQLStatementParser parser = new MySqlStatementParser(sql);
    // 使用Parser解析生成AST，这里SQLStatement就是AST
    SQLStatement statement = parser.parseStatement();
    // 使用visitor来访问AST
    MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//            statement.accept(visitor);
    // 从visitor中拿出你所关注的信息
    //        System.out.println(visitor.getColumns());
    List<TableStat.Condition> conditions = visitor.getConditions();

    //        System.out.println(conditions);
    if (statement instanceof SQLSelectStatement sqlSelectStatement) {
      SQLSelect select = sqlSelectStatement.getSelect();
      System.out.println(select);
      SQLSelectQueryBlock queryBlock = select.getQueryBlock();
      List<SQLSelectItem> selectList = queryBlock.getSelectList();
      for (SQLSelectItem sqlSelectItem : selectList) {
        String alias = sqlSelectItem.getAlias();
        SQLExpr expr = sqlSelectItem.getExpr();
      }
      SQLTableSource from = queryBlock.getFrom();
      System.out.println(from);
      SQLExpr where = queryBlock.getWhere();
      System.out.println(where);
      List<SQLObject> childrens = where.getChildren();
      for (SQLObject children : childrens) {
        if (children instanceof SQLIdentifierExpr) {
          System.out.println(((SQLIdentifierExpr) children));
        }
        if (children instanceof SQLBinaryOpExpr) {
          SQLBinaryOpExpr children1 = (SQLBinaryOpExpr) children;
          SQLExpr left = children1.getLeft();
          if (left instanceof SQLIdentifierExpr) {
            System.out.println(left);
          }
          if (left instanceof SQLPropertyExpr) {
            SQLPropertyExpr right1 = (SQLPropertyExpr) left;
            System.out.println(right1);
          }
          SQLExpr right = children1.getRight();
          if (right instanceof SQLIntegerExpr) {
            System.out.println(((SQLIntegerExpr) right));
          }
          if (right instanceof SQLCharExpr) {
            System.out.println(((SQLCharExpr) right));
          }
          //
          if (right instanceof SQLVariantRefExpr) {
            System.out.println(right);
          }
        }
      }
    }
  }
}
