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

package io.github.code13.javastack.spring.framework.jdbc;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

/**
 * JdbcOperationsUsingSimpleClassesRunner.
 *
 * <p>SimpleJdbc classes provide an easy way to configure and execute SQL statements. These classes
 * use database metadata to build basic queries. {@link SimpleJdbcInsert} and {@link SimpleJdbcCall}
 * classes provide an easier way to execute insert and stored procedure calls.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 10:15
 */
public class JdbcOperationsUsingSimpleClassesRunner {

  EmbeddedDatabase dataSource;

  @BeforeEach
  void setup() {
    dataSource = JdbcInternalUtils.getEmbeddedH2Database();
  }

  @AfterEach
  void tearDown() {
    dataSource.shutdown();
  }

  /**
   * The INSERT statement is generated based on the configuration of SimpleJdbcInsert and all we
   * need is to provide the Table name, Column names and values.
   */
  @Test
  @DisplayName("simpleJdbcInsert")
  void simpleJdbcInsert() {
    //    First, let’s create a SimpleJdbcInsert:
    SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("EMPLOYEE");

    //    Next, let’s provide the Column names and values, and execute the operation

    Map<String, Object> parameter = new HashMap<>();
    parameter.put("ID", 5);
    parameter.put("FIRST_NAME", "Bill");
    parameter.put("LAST_NAME", "Gates");
    parameter.put("ADDRESS", "USA");

    int execute = simpleJdbcInsert.execute(parameter);
    // 1 ; insert success
    System.out.println(execute);
  }

  @Test
  @DisplayName("simpleJdbcInsertUsingGeneratedKeyColumns")
  void simpleJdbcInsertUsingGeneratedKeyColumns() {
    //    First, let’s create a SimpleJdbcInsert:
    SimpleJdbcInsert simpleJdbcInsert =
        new SimpleJdbcInsert(JdbcInternalUtils.getEmbeddedH2Database())
            .withTableName("EMPLOYEE")
            // configure the actual column that is auto-generated:
            .usingGeneratedKeyColumns("ID");

    //    Next, let’s provide the Column names and values, and execute the operation

    Map<String, Object> parameter = new HashMap<>();
    parameter.put("FIRST_NAME", "Bill");
    parameter.put("LAST_NAME", "Gates");
    parameter.put("ADDRESS", "USA");

    // to allow the database to generate the primary key, we can make use of the
    // executeAndReturnKey() API
    Number id = simpleJdbcInsert.executeAndReturnKey(parameter);
    System.out.println("Generated id - " + id.longValue());
  }

  @Test
  @DisplayName("storedProceduresWithSimpleJdbcCall")
  void storedProceduresWithSimpleJdbcCall() {
    SimpleJdbcCall simpleJdbcCall =
        new SimpleJdbcCall(JdbcInternalUtils.getEmbeddedH2Database())
            .withProcedureName("READ_EMPLOYEE");

    MapSqlParameterSource parameterSource = new MapSqlParameterSource().addValue("in_id", 1);
    Map<String, Object> out = simpleJdbcCall.execute(parameterSource);

    Employee employee = new Employee();
    employee.setFirstName((String) out.get("FIRST_NAME"));
    employee.setLastName((String) out.get("LAST_NAME"));

    System.out.println(employee);
  }
}
