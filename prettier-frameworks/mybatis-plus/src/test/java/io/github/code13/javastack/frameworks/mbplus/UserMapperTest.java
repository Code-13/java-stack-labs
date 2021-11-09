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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
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
  static void setUpMybatisDatabase() {
    H2TestUtils.start();

    SqlSessionFactory builder =
        // https://gejun123456.github.io/MyBatisCodeHelper-Pro/#/quicktestsql?id=%e7%94%9f%e6%88%90testcase%e6%94%af%e6%8c%81mybatisplus
        new MybatisSqlSessionFactoryBuilder()
            .build(
                UserMapperTest.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "mybatisTestConfiguration/UserMapperTestConfiguration.xml"));
    // you can use builder.openSession(false) to not commit to database
    mapper = builder.getConfiguration().getMapper(UserMapper.class, builder.openSession(true));
  }

  @Test
  void testGetByEmail() {
    User user = mapper.getByEmail("test1@baomidou.com");
    assertNotNull(user);
  }
}
