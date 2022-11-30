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

package io.github.code13.javastack.frameworks.mbplus.utils;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * H2.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 12:38 PM
 */
public final class H2 {

  private H2() {}

  private static H2 instance;

  public static H2 instance() {
    if (instance == null) {
      instance = new H2();
    }
    return instance;
  }

  public H2 startH2Server() {
    try {
      Server.createTcpServer().start();
      return instance;
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("启动 H2 数据库出错");
      throw new RuntimeException(e);
    }
  }

  public H2 initTables(DataSource dataSource) {
    SqlDataSourceScriptDatabaseInitializer initializer =
        new SqlDataSourceScriptDatabaseInitializer(dataSource, creatSettings());
    initializer.setResourceLoader(new DefaultResourceLoader());
    initializer.initializeDatabase();

    return instance;
  }

  public H2 executeSql(String url, String user, String password, String sql) {
    System.out.println(sql);

    try (Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement(); ) {
      statement.execute(sql);
      return instance;
    } catch (SQLException e) {
      //      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public static DataSource createH2DataSource() {
    DataSourceProperties dataSourceProperties = Utils.loadDataSourceProperties();
    return DataSourceBuilder.create(Utils.class.getClassLoader())
        .type(HikariDataSource.class)
        .driverClassName(dataSourceProperties.determineDriverClassName())
        .url(dataSourceProperties.determineUrl())
        .username(dataSourceProperties.determineUsername())
        .password(dataSourceProperties.determinePassword())
        .build();
  }

  private static DatabaseInitializationSettings creatSettings() {
    return SettingsCreator.createFrom(new SqlInitializationProperties());
  }

  /** copy from {@link org.springframework.boot.autoconfigure.sql.init.SettingsCreator} */
  static final class SettingsCreator {

    private SettingsCreator() {}

    static DatabaseInitializationSettings createFrom(SqlInitializationProperties properties) {
      DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
      settings.setSchemaLocations(
          scriptLocations(properties.getSchemaLocations(), "schema", properties.getPlatform()));
      settings.setDataLocations(
          scriptLocations(properties.getDataLocations(), "data", properties.getPlatform()));
      settings.setContinueOnError(properties.isContinueOnError());
      settings.setSeparator(properties.getSeparator());
      settings.setEncoding(properties.getEncoding());
      settings.setMode(properties.getMode());
      return settings;
    }

    private static List<String> scriptLocations(
        List<String> locations, String fallback, String platform) {
      if (locations != null) {
        return locations;
      }
      List<String> fallbackLocations = new ArrayList<>();
      fallbackLocations.add("optional:classpath*:" + fallback + "-" + platform + ".sql");
      fallbackLocations.add("optional:classpath*:" + fallback + ".sql");
      return fallbackLocations;
    }
  }
}
