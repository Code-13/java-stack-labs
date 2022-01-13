/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.frameworks.mbplus;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.SqlRunnerInjector;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * CustomerMybatisPlusSqlSessionFactoryBuilder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/1/13 22:37
 */
public class TestMybatisPlusSqlSessionFactoryBuilder extends MybatisSqlSessionFactoryBuilder {

  private List<TestGlobalConfigCustomer> customers = Collections.emptyList();

  @Override
  public SqlSessionFactory build(Configuration configuration) {
    GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);

    // todos
    customer(globalConfig);

    IdentifierGenerator identifierGenerator;
    if (null == globalConfig.getIdentifierGenerator()) {
      identifierGenerator = new DefaultIdentifierGenerator();
      globalConfig.setIdentifierGenerator(identifierGenerator);
    } else {
      identifierGenerator = globalConfig.getIdentifierGenerator();
    }
    IdWorker.setIdentifierGenerator(identifierGenerator);

    if (globalConfig.isEnableSqlRunner()) {
      new SqlRunnerInjector().inject(configuration);
    }

    SqlSessionFactory sqlSessionFactory = super.build(configuration);

    // 缓存 sqlSessionFactory
    globalConfig.setSqlSessionFactory(sqlSessionFactory);

    return sqlSessionFactory;
  }

  private void customer(GlobalConfig globalConfig) {
    for (TestGlobalConfigCustomer configCustomer : customers) {
      configCustomer.customer(globalConfig);
    }
  }

  public TestMybatisPlusSqlSessionFactoryBuilder setCustomers(
      List<TestGlobalConfigCustomer> customers) {
    this.customers = customers;
    return this;
  }
}
