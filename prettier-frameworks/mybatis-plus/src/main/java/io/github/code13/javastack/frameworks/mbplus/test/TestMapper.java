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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.type.TypeException;
import org.junit.jupiter.api.BeforeEach;

/**
 * BaseTestMapper.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/27 14:14
 */
public abstract class TestMapper<T> implements MapperBuilder<T> {

  // --- type ---

  protected final Class<T> mapperType;

  // --- mapper ---
  protected T mapper;

  // --- config ---

  protected String[] mapperLocations = {"classpath*:/sql/**/*.xml"};
  protected Class<? extends Log> logImpl = org.apache.ibatis.logging.slf4j.Slf4jImpl.class;

  protected TestMapper() {
    Type genericSuperclass = getClass().getGenericSuperclass();
    if (genericSuperclass instanceof Class<?>) {
      throw new TypeException(
          "'"
              + getClass()
              + "' extends TypeReference but misses the type parameter. "
              + "Remove the extension or add a type parameter to it.");
    }

    mapperType = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
  }

  @Override
  public String[] getMapperLocations() {
    return mapperLocations;
  }

  @Override
  public Class<? extends Log> getLogImpl() {
    return logImpl;
  }

  public T getMapper() {
    return mapper;
  }

  // --- Test Methods ---

  @BeforeEach
  void setup() {
    mapper = buildMapper(mapperType);
  }
}
