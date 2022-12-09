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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Key.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/4 13:31
 */
public class Key {

  private static final AtomicLong atomicLong = new AtomicLong();

  private final Long id;

  public Key() {
    id = atomicLong.incrementAndGet();
  }

  public Key(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Key key = (Key) o;
    return id.equals(key.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "key:" + id;
  }
}
