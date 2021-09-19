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

package io.github.code13.javastack.libs.zxing;

import java.io.Serial;

/**
 * QrcodeException.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 15:21
 */
public class QrcodeException extends RuntimeException {

  @Serial private static final long serialVersionUID = -3827798381929804161L;

  public QrcodeException() {
    this("创建而二维码时失败");
  }

  public QrcodeException(String message) {
    super(message);
  }

  public QrcodeException(String message, Throwable cause) {
    super(message, cause);
  }

  public QrcodeException(Throwable cause) {
    this("创建而二维码时失败", cause);
  }
}
