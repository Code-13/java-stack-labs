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

drop schema if exists `spring_data`;
create schema `spring_data`;

use
`spring_data`;
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`         varchar(64)   NOT NULL COMMENT '主键ID',
    `username`        varchar(128)  NOT NULL unique COMMENT '用户名',
    `encode_password` varchar(1024) NOT NULL COMMENT '编码后的密码',
    `expired`         tinyint(1)    NOT NULL DEFAULT 1 COMMENT '账户是否过期',
    `locked`          tinyint(1)    NOT NULL DEFAULT 1 COMMENT '账户是否锁定',
    `enabled`         tinyint(1)    NOT NULL DEFAULT 1 COMMENT '账户是否可用',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB COMMENT '用户账户信息'
  DEFAULT CHARSET = utf8mb4;
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `role_id`      varchar(64) NOT NULL COMMENT '主键ID',
    `role_name`    varchar(32) NOT NULL UNIQUE COMMENT '角色标识',
    `role_comment` varchar(500) DEFAULT NULL COMMENT '说明',
    `enabled`      tinyint(1)   DEFAULT '1' COMMENT ' 是否可用',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='角色表';
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_role_id` varchar(64) NOT NULL COMMENT '主键ID',
    `username`     varchar(64) NOT NULL COMMENT '用户名',
    `role_id`      varchar(64) NOT NULL COMMENT '角色ID',
    `role_name`    varchar(32) NOT NULL COMMENT '角色标识',
    `enabled`      tinyint(1) DEFAULT '1' COMMENT ' 是否可用',
    PRIMARY KEY (`user_role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='用户角色表';
