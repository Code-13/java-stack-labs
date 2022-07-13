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

package io.github.code13.javastack.spring.security.captcha;

/**
 * CaptchaService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/10/2022 2:26 PM
 */
public interface CaptchaService {

  /**
   * verify captcha.
   *
   * @param phone 手机号 用来获取缓存验证码
   * @param rawCode 收到的验证码 用来和缓存验证码作比对
   * @return isVerified 比对的结果
   */
  boolean verifyCaptcha(String phone, String rawCode);
}
