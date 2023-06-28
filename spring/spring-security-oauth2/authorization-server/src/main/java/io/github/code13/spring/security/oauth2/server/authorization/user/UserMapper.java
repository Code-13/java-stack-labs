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

package io.github.code13.spring.security.oauth2.server.authorization.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/3/4 16:29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


  // --- Query Wrappers start ---

  /** Lambda 非链式查询 */
  default LambdaQueryWrapper<User> query() {
    return new LambdaQueryWrapper<>();
  }

  /** lambda 链式查询 */
  default LambdaQueryChainWrapper<User> queryChain() {
    return new LambdaQueryChainWrapper<>(this);
  }

  /** lambda 非链式更改 */
  default LambdaUpdateWrapper<User> update() {
    return new LambdaUpdateWrapper<>();
  }

  /** lambda 链式更改 */
  default LambdaUpdateChainWrapper<User> updateChain() {
    return new LambdaUpdateChainWrapper<>(this);
  }

  // --- Query Wrappers end ---
}