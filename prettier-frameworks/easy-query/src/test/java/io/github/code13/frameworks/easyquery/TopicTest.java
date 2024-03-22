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

package io.github.code13.frameworks.easyquery;

import static org.junit.jupiter.api.Assertions.*;

import com.easy.query.api.proxy.client.DefaultEasyEntityQuery;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.client.EasyQueryClient;
import com.easy.query.core.bootstrapper.EasyQueryBootstrapper;
import com.easy.query.core.logging.LogFactory;
import com.easy.query.mysql.config.MySQLDatabaseConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TopicTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/3/22 15:47
 */
class TopicTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  @DisplayName("test1")
  void test1() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(
        "jdbc:h2:mem:spring-data-embedded;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setMaximumPoolSize(20);

    // 采用控制台输出打印sql
    LogFactory.useStdOutLogging();

    // property的api
    EasyQueryClient easyQueryClient =
        EasyQueryBootstrapper.defaultBuilderConfiguration()
            .setDefaultDataSource(dataSource)
            .optionConfigure(
                op -> {
                  op.setPrintSql(true);
                  op.setKeepNativeStyle(true);
                })
            .useDatabaseConfigure(new MySQLDatabaseConfiguration())
            .build();

    EasyEntityQuery easyEntityQuery = new DefaultEasyEntityQuery(easyQueryClient);

    easyEntityQuery.sqlExecute(
        """
            create table t_topic
            (
                id varchar(32) not null comment '主键ID'primary key,
                stars int not null comment '点赞数',
                title varchar(50) null comment '标题',
                create_time datetime not null comment '创建时间'
            )comment '主题表';
            """);

    Topic topic = easyEntityQuery.queryable(Topic.class).whereById(1).firstOrNull();
    System.out.println(topic);

    System.out.println(11111);
  }
}
