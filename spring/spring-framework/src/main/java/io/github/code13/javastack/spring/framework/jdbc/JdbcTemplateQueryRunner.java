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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

/**
 * JdbcTemplateQuery example.
 *
 * <p>
 *
 * <pre>
 *   The JDBC template is the main API through which we’ll access most of the functionality that we’re interested in:
 *    • creation and closing of connections
 *    • executing statements and stored procedure calls
 *    • iterating over the ResultSet and returning results
 * </pre>
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 09:08
 */
public class JdbcTemplateQueryRunner {

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
  @DisplayName("basicQuery")
  void basicQuery() {
    Integer integer = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
    System.out.println(integer);
    assertNotNull(integer);
  }

  /** Notice the standard syntax of providing parameters – using the `?` character. */
  @Test
  @DisplayName("basicInsert")
  void basicInsert() {
    int update =
        jdbcTemplate.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)", 5, "Bill", "Gates", "USA");
    System.out.println(update);
  }

  /**
   * To get support for named parameters, we’ll use the other JDBC template provided by the
   * framework – the NamedParameterJdbcTemplate. Additionally, this wraps the JdbcTemplate and
   * provides an alternative to the traditional syntax using “?” to specify parameters. Under the
   * hood, it substitutes the named parameters to JDBC “?” placeholder and delegates to the wrapped
   * JDCTemplate to execute the queries:
   */
  @Test
  @DisplayName("queriesWithNamedParameters")
  void queriesWithNamedParameters() {
    //    Notice how we are using the MapSqlParameterSource to provide the values for the named
    // parameters.
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", 1);
    String firstName =
        namedParameterJdbcTemplate.queryForObject(
            "SELECT FIRST_NAME FROM EMPLOYEE WHERE ID = :id", sqlParameterSource, String.class);
    System.out.println(firstName);
    assertNotNull(firstName);
  }

  /** example that uses properties from a bean to determine the named parameters: */
  @Test
  @DisplayName("queriesWithBeanPropertySqlParameterSource")
  void queriesWithBeanPropertySqlParameterSource() {
    Employee employee = new Employee();
    employee.setFirstName("James");

    String selectByIdSql = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = :firstName";

    //    Note how we’re now making use of the BeanPropertySqlParameterSource implementations
    // instead of specifying the named parameters manually like before.
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(employee);

    Integer integer =
        namedParameterJdbcTemplate.queryForObject(selectByIdSql, parameterSource, Integer.class);

    System.out.println(integer);
    assertNotNull(integer);
  }

  @Test
  @DisplayName("mappingQueryResultsToJavaObject")
  void mappingQueryResultsToJavaObject() {
    Employee employee =
        jdbcTemplate.queryForObject(
            "SELECT * FROM EMPLOYEE WHERE ID = ?", new EmployeeRowMapper(), 1);

    System.out.println(employee);
  }

  static class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
      Employee employee = new Employee();
      employee.setId(rs.getInt("ID"));
      employee.setFirstName(rs.getString("FIRST_NAME"));
      employee.setLastName(rs.getString("LAST_NAME"));
      employee.setAddress(rs.getString("ADDRESS"));
      return employee;
    }
  }
}
