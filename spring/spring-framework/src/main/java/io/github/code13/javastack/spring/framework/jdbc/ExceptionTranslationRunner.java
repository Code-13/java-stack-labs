/*
 *
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

package io.github.code13.javastack.spring.framework.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/**
 * ExceptionTranslationRunner.
 *
 * <p>Spring comes with its own data exception hierarchy out of the box – with DataAccessException
 * as the root exception – and it translates all underlying raw exceptions to it. And so we keep our
 * sanity by not having to handle low-level persistence exceptions and benefit from the fact that
 * Spring wraps the low-level exceptions in DataAccessException or one of its sub-classes. Also,
 * this keeps the exception handling mechanism independent of the underlying database we are using.
 * Besides, the default {@link SQLErrorCodeSQLExceptionTranslator}, we can also provide our own
 * implementation of {@link SQLExceptionTranslator}.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 10:05
 */
public class ExceptionTranslationRunner {

  void setup() {
    DataSource dataSource = JdbcInternalUtils.getEmbeddedH2Database();

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    // To use this custom exception translator, we need to pass it to the JdbcTemplate by calling
    // setExceptionTranslator() method:
    jdbcTemplate.setExceptionTranslator(new CustomSQLErrorCodeTranslator());
  }

  /**
   * Here’s a quick example of a custom implementation, customizing the error message when there is
   * a duplicate key violation, which results in error code 23505 when using H2:
   */
  static class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator {

    @Override
    protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx) {
      if (sqlEx.getErrorCode() == 23505) {
        return new DuplicateKeyException(
            "Custom Exception translator - Integrity constraint violation.", sqlEx);
      }
      return null;
    }
  }
}
