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

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

/**
 * BatchOperationsRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 10:53
 */
public class BatchOperationsRunner {
  EmbeddedDatabase dataSource;
  JdbcTemplate jdbcTemplate;
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @BeforeEach
  void setup() {
    dataSource = JdbcInternalUtils.getEmbeddedH2Database();
    jdbcTemplate = new JdbcTemplate(dataSource);
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  @AfterEach
  void tearDown() {
    dataSource.shutdown();
  }

  @Test
  @DisplayName("basicBatchOperationsUsingJdbcTemplate")
  void basicBatchOperationsUsingJdbcTemplate() {
    List<Employee> employees = mock();
    int[] batchUpdate =
        jdbcTemplate.batchUpdate(
            "INSERT INTO EMPLOYEE VALUES (?,?,?,?)",
            new BatchPreparedStatementSetter() {
              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, employees.get(i).getId());
                ps.setString(2, employees.get(i).getFirstName());
                ps.setString(3, employees.get(i).getLastName());
                ps.setString(4, employees.get(i).getAddress());
              }

              @Override
              public int getBatchSize() {
                return employees.size();
              }
            });

    assertEquals(employees.size(), batchUpdate.length);
  }

  @Test
  @DisplayName("basicBatchOperationsUsingNamedParameterJdbcTemplate")
  void basicBatchOperationsUsingNamedParameterJdbcTemplate() {
    List<Employee> employees = mock();

    SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(employees.toArray());

    int[] updatedCounts =
        namedParameterJdbcTemplate.batchUpdate(
            "INSERT INTO EMPLOYEE VALUES (:id, :firstName, :lastName, :address)", batch);

    assertEquals(employees.size(), updatedCounts.length);
  }

  static List<Employee> mock() {
    return Stream.iterate(1, integer -> integer + 1)
        .limit(5)
        .map(
            i -> {
              Employee employee = new Employee();
              employee.setId(5 + i);
              employee.setFirstName("FirstName" + i);
              employee.setLastName("LastName" + i);
              employee.setAddress("Address" + i);
              return employee;
            })
        .collect(Collectors.toList());
  }
}
