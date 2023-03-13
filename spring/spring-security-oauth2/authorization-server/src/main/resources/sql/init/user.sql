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

CREATE TABLE "sys_user"
(
    "id"                      int8 NOT NULL primary key,
    "username"                varchar(255),
    "password"                varchar(255),
    "phone"                   varchar(255),
    "account_non_expired"     bool NOT NULL DEFAULT true,
    "account_non_locked"      bool NOT NULL DEFAULT true,
    "credentials_non_expired" bool NOT NULL DEFAULT true,
    "enabled"                 bool NOT NULL DEFAULT true,
    "version"                 int8,
    "created_user"            varchar(255),
    "created_time"            timestamp(6),
    "updated_user"            varchar(255),
    "updated_time"            timestamp(6)
);

INSERT INTO "sys_user" ("id", "username", "password", "phone", "account_non_expired",
                        "account_non_locked", "credentials_non_expired", "enabled", "version",
                        "created_user", "created_time", "updated_user", "updated_time")
VALUES (1, 'test', '{bcrypt}$2a$10$JwwZfaTk1iNcDffEiRMu3enJi593Mb1exdi.eZ7c83iawNKU1h6aG',
        '18866668888', 't', 't', 't', 't', NULL, NULL, NULL, NULL, NULL);