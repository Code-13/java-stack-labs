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

package io.github.code13.javastack.frameworks.mbplus.test;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.Environment.Builder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * MapperBuilder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/27 14:09
 */
interface MapperBuilder<T> extends MapperResolver {

  default T buildMapper(Class<T> mapperClazz) {
    return buildMapper(mapperClazz, getAutoCommit());
  }

  default T buildMapper(Class<T> mapperClazz, boolean autoCommit) {
    return buildMapper(mapperClazz, getMapperLocations(), autoCommit);
  }

  default T buildMapper(Class<T> mapperClazz, String[] mapperLocations, boolean autoCommit) {
    MybatisConfiguration configuration = buildConfiguration(mapperLocations);
    SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryBuilder().build(configuration);
    return configuration.getMapper(mapperClazz, sqlSessionFactory.openSession(autoCommit));
  }

  @NonNull
  default MybatisConfiguration buildConfiguration(String[] mapperLocations) {
    MybatisConfiguration configuration = new MybatisConfiguration();
    configuration.setEnvironment(getEnvironment());
    configuration.setLogImpl(getLogImpl());
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.setCacheEnabled(false);
    configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);

    GlobalConfig globalConfig = GlobalConfigUtils.defaults();
    customizeGlobalConfig(configuration, globalConfig);
    GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);

    resolveMappers(configuration, mapperLocations);
    addInterceptors(configuration);
    customizeConfiguration(configuration);
    return configuration;
  }

  default void customizeGlobalConfig(
      MybatisConfiguration configuration, GlobalConfig globalConfig) {}

  default Environment getEnvironment() {
    TransactionFactory transactionFactory = new JdbcTransactionFactory();

    DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
    dataSourceFactory.setProperties(getDataSourceProperties());
    DataSource dataSource = dataSourceFactory.getDataSource();

    return new Builder("default")
        .transactionFactory(transactionFactory)
        .dataSource(dataSource)
        .build();
  }

  default void addInterceptors(Configuration configuration) {
    addMybatisPlusInterceptors(configuration);
  }

  default void addMybatisPlusInterceptors(Configuration configuration) {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
    configuration.addInterceptor(interceptor);
  }

  default void customizeConfiguration(MybatisConfiguration configuration) {}

  @NonNull
  default MybatisSqlSessionFactoryBuilder getSqlSessionFactoryBuilder() {
    return new MybatisSqlSessionFactoryBuilder();
  }

  default boolean getAutoCommit() {
    return true;
  }

  String[] getMapperLocations();

  Class<? extends Log> getLogImpl();

  Properties getDataSourceProperties();
}
