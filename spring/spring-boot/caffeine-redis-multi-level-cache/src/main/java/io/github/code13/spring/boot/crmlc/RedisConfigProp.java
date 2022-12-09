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
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * RedisConfigProp.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 16:08
 */
@Data
class RedisConfigProp {

  /** 全局过期时间，默认不过期 */
  private Duration defaultExpiration = Duration.ZERO;

  /** 每个cacheName的过期时间，优先级比defaultExpiration高 */
  private Map<String, Duration> expires = new HashMap<>();

  /** 缓存更新时通知其他节点的topic名称 */
  private String topic = "cache:redis:caffeine:topic";
}
