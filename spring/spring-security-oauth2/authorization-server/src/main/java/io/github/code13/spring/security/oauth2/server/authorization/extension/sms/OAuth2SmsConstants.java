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

package io.github.code13.spring.security.oauth2.server.authorization.extension.sms;

/**
 * OAuth2SmsConstants.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/1/19 18:36
 */
public final class OAuth2SmsConstants {

  /** 自定义 grant type —— 短信验证码 */
  public static final String GRANT_TYPE_SMS_CODE = "urn:ietf:params:oauth:grant-type:sms_code";

  /** 自定义 grant type —— 短信验证码 —— 手机号的key */
  public static final String SMS_PHONE_PARAMETER_NAME = "phone";

  /** 自定义 grant type —— 短信验证码 —— 短信验证码的key */
  public static final String SMS_CODE_PARAMETER_NAME = "code";

  private OAuth2SmsConstants() {
    // no instance
  }
}
