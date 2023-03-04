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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * User.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/3/4 15:59
 */
@Getter
@Setter
public class User implements Serializable {

  @Serial private static final long serialVersionUID = 6498051515552405133L;

  @TableId(type = IdType.ASSIGN_ID)
  private String id;

  private String username;

  private String password;

  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private String phone;

  /** 账户是否过期,过期无法验证 */
  private boolean accountNonExpired = true;

  /** 指定用户是否被锁定或者解锁,锁定的用户无法进行身份验证 */
  private boolean accountNonLocked = true;

  /** 指示是否已过期的用户的凭据(密码),过期的凭据防止认证 */
  private boolean credentialsNonExpired = true;

  /** 是否被禁用,禁用的用户不能身份验证 */
  private boolean enabled = true;

  @Version private Integer version;

  @TableField(fill = FieldFill.INSERT)
  private String createdUser;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createdTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updatedUser;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;
}
