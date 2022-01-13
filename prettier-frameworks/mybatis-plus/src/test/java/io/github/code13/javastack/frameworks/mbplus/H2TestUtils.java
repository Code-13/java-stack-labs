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

package io.github.code13.javastack.frameworks.mbplus;

import static io.github.code13.javastack.frameworks.mbplus.utils.H2Utils.createH2DataSource;
import static io.github.code13.javastack.frameworks.mbplus.utils.Utils.loadClassPathResource;
import static io.github.code13.javastack.frameworks.mbplus.utils.Utils.loadDataSourceProperties;

import io.github.code13.javastack.frameworks.mbplus.utils.H2Utils;
import javax.sql.DataSource;

/**
 * H2TestUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 1:39 PM
 */
public final class H2TestUtils {

  private H2TestUtils() {}

  public static final String SCHEMA_PATH = "schema.sql";

  public static DataSource start() {
    H2Utils.newBuilder()
        .startH2Server()
        .initTables(loadDataSourceProperties(), loadClassPathResource(SCHEMA_PATH));

    return createH2DataSource();
  }
}
