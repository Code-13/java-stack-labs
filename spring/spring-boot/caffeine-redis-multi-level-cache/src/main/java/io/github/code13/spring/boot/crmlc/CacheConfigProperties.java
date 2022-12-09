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

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * CacheConfigProperties.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 22:11
 */
@Data
@ConfigurationProperties(prefix = "spring.cache.multi")
class CacheConfigProperties {

  private Set<String> cacheNames = new HashSet<>();

  /** 是否存储空值，默认true，防止缓存穿透 */
  private boolean cacheNullValues = true;

  /** 是否动态根据cacheName创建Cache的实现，默认true */
  private boolean dynamic = true;

  /** 缓存key的前缀 */
  private String cachePrefix;

  @NestedConfigurationProperty private RedisConfigProp redis = new RedisConfigProp();

  @NestedConfigurationProperty private CaffeineConfigProp caffeine = new CaffeineConfigProp();
}
