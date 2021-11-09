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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

/**
 * YamlUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 1:08 PM
 */
public final class Utils {

  private Utils() {}

  static YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
  static ClassPathResource resource = new ClassPathResource("application.yaml");

  public static String getYamlProperty(String name) {
    try {
      List<PropertySource<?>> propertySources = loader.load("application", resource);

      PropertySource<?> propertySource = propertySources.get(0);

      return (String) propertySource.getProperty(name);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static DataSourceProperties loadDataSourceProperties() {
    try {

      String url = getYamlProperty("spring.datasource.url");
      String user = getYamlProperty("spring.datasource.username");
      String password = getYamlProperty("spring.datasource.password");
      String driver = getYamlProperty("spring.datasource.driver-class-name");

      DataSourceProperties properties = new DataSourceProperties();
      properties.setUrl(url);
      properties.setUsername(user);
      properties.setPassword(password);
      properties.setType((Class<? extends DataSource>) Class.forName(driver));

      return properties;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static String loadClassPathResource(String path) {
    ClassPathResource classPathResource = new ClassPathResource(path);

    try (InputStream inputStream = classPathResource.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ) {

      inputStream.transferTo(outputStream);
      return outputStream.toString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
