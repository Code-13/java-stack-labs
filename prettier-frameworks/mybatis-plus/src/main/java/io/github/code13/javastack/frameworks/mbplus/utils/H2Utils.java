/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.frameworks.mbplus.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * H2Utils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 12:38 PM
 */
public final class H2Utils {

  private H2Utils() {}

  private static H2Utils instance;

  public static H2Utils newBuilder() {
    if (instance == null) {
      instance = new H2Utils();
    }
    return instance;
  }

  public H2Utils startH2Server() {
    try {
      Server.createTcpServer().start();
      return instance;
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("启动 H2 数据库出错");
      throw new RuntimeException(e);
    }
  }

  public H2Utils initTables(String url, String user, String password, String initSql) {

    try (Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement(); ) {
      statement.execute(initSql);
      return instance;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public H2Utils initTables(DataSourceProperties properties, String initSql) {

    try (Connection connection =
            DriverManager.getConnection(
                properties.getUrl(), properties.getUsername(), properties.getPassword());
        Statement statement = connection.createStatement(); ) {
      statement.execute(initSql);
      return instance;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
