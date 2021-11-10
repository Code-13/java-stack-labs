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

--  copy from https://github.com/baomidou/generator/blob/develop/mybatis-plus-generator/src/test/resources/sql/init.sql

drop table if exists `t_simple`;
create table `t_simple`
(
    id          int IDENTITY primary key comment 'id',
    name        varchar(50) comment '姓名',
    age         int comment '年龄',
    delete_flag tinyint(1) comment '删除标识1',
    deleted     tinyint(1) comment '删除标识2',
    version     bigint comment '版本',
    create_time datetime comment '创建时间',
    update_time datetime comment '更新时间',
    primary key (id)
) COMMENT = '测试表';