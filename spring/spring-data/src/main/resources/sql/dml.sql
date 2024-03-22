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

INSERT INTO spring_data.sys_user (user_id, username, encode_password,
                                  expired, locked, enabled)
VALUES ('1', 'spring-data-test',
        '{bcrypt}$2a$10$iNfPvjiZitM.DzuiPgpjYuLGI16pz0MTK8RIoGOLuPpsXZxccOwu2', 1, 1, 1);

-- # 初始化两个角色
INSERT INTO spring_data.role (role_id, role_name, role_comment, enabled)
VALUES ('1', 'ROLE_USER', '用户角色', 1);
INSERT INTO spring_data.role (role_id, role_name, role_comment, enabled)
VALUES ('2', 'ROLE_ADMIN', '管理员角色', 1);
-- #给用户赋予角色
INSERT INTO spring_data.user_role (user_role_id, username, role_id,
                                   role_name, enabled)
VALUES ('1', 'spring-data-test1', '1', 'ROLE_USER', 1);
INSERT INTO spring_data.user_role (user_role_id, username, role_id,
                                   role_name, enabled)
VALUES ('2', 'spring-data-test2', '2', 'ROLE_ADMIN', 1);
