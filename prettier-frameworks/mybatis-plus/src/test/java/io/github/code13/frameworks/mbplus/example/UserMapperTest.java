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

package io.github.code13.frameworks.mbplus.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.github.code13.frameworks.mbplus.H2TestUtils;
import io.github.code13.frameworks.mbplus.extensions.method.injector.CustomerSqlInjector;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * UserMapperTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 11:49 AM
 */
class UserMapperTest {

  private static UserMapper mapper;

  @BeforeAll
  static void setUpMybatisDatabase() throws Exception {
    DataSource dataSource = H2TestUtils.start();

    MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setMapperLocations(
        new MybatisPlusProperties()
            .setMapperLocations(
                new String[] {"classpath*:io/github/code13/frameworks/mbplus/example/*.xml"})
            .resolveMapperLocations());

    // config for SqlInjector
    GlobalConfig globalConfig = GlobalConfigUtils.defaults();
    globalConfig.setSqlInjector(new CustomerSqlInjector());
    factoryBean.setGlobalConfig(globalConfig);
    SqlSessionFactory factory = factoryBean.getObject();

    // you can use builder.openSession(false) to not commit to database
    mapper = factory.getConfiguration().getMapper(UserMapper.class, factory.openSession(true));
  }

  @Test
  void testGetByEmail() {
    Optional<User> userOptional = mapper.getByEmail("test1@baomidou.com");
    assertTrue(userOptional.isPresent());
  }
}
