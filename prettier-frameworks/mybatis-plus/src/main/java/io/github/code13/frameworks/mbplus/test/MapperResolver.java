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

package io.github.code13.frameworks.mbplus.test;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * MapperResolver. copy from {@link SqlSessionFactoryBean#buildSqlSessionFactory()}
 *
 * @see org.mybatis.spring.SqlSessionFactoryBean
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/27 14:08
 */
interface MapperResolver {

  ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

  default void resolveMappers(Configuration configuration, String[] mapperLocations) {
    try {
      for (Resource mapperLocation : resolveMapperLocations(mapperLocations)) {
        XMLMapperBuilder xmlMapperBuilder =
            new XMLMapperBuilder(
                mapperLocation.getInputStream(),
                configuration,
                mapperLocation.toString(),
                configuration.getSqlFragments());
        xmlMapperBuilder.parse();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Resource[] resolveMapperLocations(String... locations) {
    return Stream.of(Optional.of(locations).orElse(new String[0]))
        .flatMap(location -> Stream.of(getResources(location)))
        .toArray(Resource[]::new);
  }

  private Resource[] getResources(String location) {
    try {
      return resourceResolver.getResources(location);
    } catch (IOException e) {
      return new Resource[0];
    }
  }
}
