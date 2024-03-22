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

package io.github.code13.frameworks.easyquery;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import io.github.code13.frameworks.easyquery.proxy.TopicProxy;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Topic.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/3/22 15:39
 */
@Data
@Table("t_topic")
@EntityProxy
public class Topic implements ProxyEntityAvailable<Topic, TopicProxy> {

  @Column(primaryKey = true)
  private String id;

  private Integer stars;

  private String title;

  private LocalDateTime createTime;

  @Override
  public Class<TopicProxy> proxyTableClass() {
    return TopicProxy.class;
  }
}
