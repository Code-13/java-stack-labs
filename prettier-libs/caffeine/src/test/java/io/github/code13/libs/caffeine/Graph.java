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

package io.github.code13.libs.caffeine;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Graph.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/4 13:32
 */
public class Graph {

  private static final AtomicLong atomicLong = new AtomicLong();

  private final Long id;
  private final LocalDateTime creationDate = LocalDateTime.now();

  public Graph() {
    id = atomicLong.incrementAndGet();
  }

  public Graph(Long id) {
    this.id = id;
  }

  public List<String> vertices() {
    return Collections.emptyList();
  }

  public LocalDateTime creationDate() {
    return creationDate;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Graph:" + id;
  }
}
