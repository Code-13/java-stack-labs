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

package io.github.code13.javastack.spring.security;

import org.springframework.http.HttpStatus;

/**
 * ResponseData.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/11/2022 3:51 PM
 */
public final class ResponseData {

  private int code;

  private String msg;

  private String path;

  public ResponseData() {}

  public ResponseData(HttpStatus status, String path) {
    this(status.value(), status.getReasonPhrase(), path);
  }

  public ResponseData(int code, String msg, String path) {
    this.code = code;
    this.msg = msg;
    this.path = path;
  }

  public static ResponseData of(HttpStatus status, String path) {
    return new ResponseData(status, path);
  }

  // --- getters、setters、hashcode、equals、toString

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public String getPath() {
    return path;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
