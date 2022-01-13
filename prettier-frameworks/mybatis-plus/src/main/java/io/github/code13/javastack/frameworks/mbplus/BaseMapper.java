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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

/**
 * MyBatis基类 BaseMapper.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/1/6 22:29
 */
public interface BaseMapper<T, ID extends Serializable>
    extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

  /*
   * Dao 书写规范
   *
   * CRUD操作            方法名约定
   * 新增                create
   * 添加                add
   * 删除                remove
   * 修改                update
   * 查询（单个结果）     get
   * 查询（多个结果）     list
   * 分页查询            page
   * 统计               count
   */

  /**
   * select one return Optional
   *
   * @param id id
   * @return optional value
   */
  default Optional<T> getById(ID id) {
    return Optional.ofNullable(selectById(id));
  }

  /**
   * select one return Optional.
   *
   * @param queryWrapper wrapper
   * @return optional value
   */
  default Optional<T> get(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
    return Optional.ofNullable(selectOne(queryWrapper));
  }

  /**
   * 高性能的 exist 查询.
   *
   * <p>SELECT 1 FROM table WHERE condition LIMIT 1
   *
   * @param queryWrapper .
   * @return true/false
   * @see ExistHighPerformanceMethod
   */
  Optional<Boolean> exist(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

  int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

  int insertBatchSomeColumn(Collection<T> entityList);

  // -- helper methods

  default boolean existWrapper(Boolean bool) {
    return bool != null && bool;
  }

  // -- easy user for wrappers

  default LambdaQueryChainWrapper<T> lambdaQuery() {
    return new LambdaQueryChainWrapper<>(this);
  }

  default LambdaUpdateChainWrapper<T> lambdaUpdate() {
    return new LambdaUpdateChainWrapper<>(this);
  }

  default QueryChainWrapper<T> query() {
    return new QueryChainWrapper<>(this);
  }
}
