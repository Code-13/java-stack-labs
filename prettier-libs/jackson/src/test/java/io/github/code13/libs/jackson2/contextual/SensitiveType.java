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

package io.github.code13.libs.jackson2.contextual;

/**
 * SensitiveType.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/22 15:58
 */
enum SensitiveType {
  /** 中文名 */
  CHINESE_NAME,
  /** 身份证号 */
  ID_CARD,
  /** 座机号 */
  FIXED_PHONE,
  /** 手机号 */
  MOBILE_PHONE,
  /** 地址 */
  ADDRESS,
  /** 电子邮件 */
  EMAIL,
  /** 银行卡 */
  BANK_CARD,
  /** 公司开户银行联号 */
  CNAPS_CODE,
  ;
}
