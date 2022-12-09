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

package io.github.code13.frameworks.mbplus.extensions.method.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.code13.frameworks.mbplus.extensions.method.SqlMethodEx;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * ExistHighPerformanceMethod.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 1/11/2022 10:27 AM
 */
public class ExistHighPerformanceMethod extends AbstractMethod {

  private static final long serialVersionUID = 4918764992664655400L;

  public ExistHighPerformanceMethod() {
    super(SqlMethodEx.EXIST_HIGH_PERFORMANCE.getMethod());
  }

  @Override
  public MappedStatement injectMappedStatement(
      Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

    SqlMethodEx sqlMethod = SqlMethodEx.EXIST_HIGH_PERFORMANCE;
    String tableName = tableInfo.getTableName();
    String sqlWhere = sqlWhereEntityWrapper(true, tableInfo);
    String sql = String.format(sqlMethod.getSql(), tableName, sqlWhere, sqlComment());
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
    return addSelectMappedStatementForOther(
        mapperClass, getMethod(sqlMethod), sqlSource, Boolean.class);
  }

  private String getMethod(SqlMethodEx sqlMethod) {
    return sqlMethod.getMethod();
  }
}
