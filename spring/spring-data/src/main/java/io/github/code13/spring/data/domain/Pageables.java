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

package io.github.code13.spring.data.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Pageables.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/5/11 11:39
 * @see Pageable
 */
public final class Pageables {

  public static Pageable ofSize(int pageSize) {
    return of(0, pageSize);
  }

  public static Pageable of(int pageNumber, int pageSize) {
    return of(pageNumber, pageSize, Sort.unsorted());
  }

  public static PageRequest of(int pageNumber, int pageSize, Sort sort) {
    return PageRequest.of(pageNumber, pageSize, sort);
  }

  private Pageables() {
    // no instance
  }
}
