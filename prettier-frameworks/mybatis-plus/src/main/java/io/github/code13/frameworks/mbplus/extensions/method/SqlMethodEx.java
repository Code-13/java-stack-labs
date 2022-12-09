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

package io.github.code13.frameworks.mbplus.extensions.method;

/**
 * SqlMethodEx.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 1/11/2022 10:15 AM
 */
public enum SqlMethodEx {

  //  --count
  EXIST_HIGH_PERFORMANCE(
      "existHighPerformance", "高性能查询是否存在", "<script>SELECT 1 FROM %s %s LIMIT 1</script>"),
  ;

  private final String method;
  private final String desc;
  private final String sql;

  SqlMethodEx(String method, String desc, String sql) {
    this.method = method;
    this.desc = desc;
    this.sql = sql;
  }

  public String getMethod() {
    return method;
  }

  public String getDesc() {
    return desc;
  }

  public String getSql() {
    return sql;
  }
}
