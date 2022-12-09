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

package io.github.code13.spring.boot.crmlc;

import java.time.Duration;
import lombok.Data;

/**
 * CaffeineConfigProp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 22:04
 */
@Data
class CaffeineConfigProp {

  /** 访问后过期时间 */
  private Duration expireAfterAccess;

  /** 写入后过期时间 */
  private Duration expireAfterWrite;

  /** 写入后刷新时间 */
  private Duration refreshAfterWrite;

  /** 初始化大小 */
  private int initialCapacity;

  /** 最大缓存对象个数，超过此数量时之前放入的缓存将失效 */
  private long maximumSize;

  /** key 强度 */
  private CaffeineStrength keyStrength;

  /** value 强度 */
  private CaffeineStrength valueStrength;
}
