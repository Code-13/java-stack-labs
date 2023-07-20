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
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import org.junit.jupiter.api.Test;

/**
 * SQLSelectQueryTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/7/20 20:51
 */
class SQLSelectQueryTest {

  /**
   * SQLSelectStatement包含一个SQLSelect，SQLSelect包含一个SQLSelectQuery。
   *
   * <p>SQLSelectQuery有主要的两个派生类， 分别是SQLSelectQueryBlock(单表sql查询)和SQLUnionQuery(联合查询)。
   */
  @Test
  void SQLSelectQuery() {
    // true
    System.out.println(parseSQLSelectQuery("select * from users") instanceof SQLSelectQueryBlock);
    // true
    System.out.println(
        parseSQLSelectQuery("select name from users union select name from school")
            instanceof SQLUnionQuery);
  }

  public SQLSelectQuery parseSQLSelectQuery(String sql) {
    SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
    SQLSelectStatement sqlSelectStatement = Utils.cast(sqlStatement, SQLSelectStatement.class);
    SQLSelect select = sqlSelectStatement.getSelect();
    return select.getQuery();
  }
}
