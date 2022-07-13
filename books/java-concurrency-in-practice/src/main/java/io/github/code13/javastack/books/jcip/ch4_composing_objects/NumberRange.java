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

package io.github.code13.javastack.books.jcip.ch4_composing_objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * NumberRange.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/7/2021 3:20 PM
 */
public class NumberRange {

  // 不变性条件：lower＜=upper
  private final AtomicInteger lower = new AtomicInteger(0);
  private final AtomicInteger upper = new AtomicInteger(0);

  public void setLower(int i) {
    // 注意--不安全的 “先检查后执行”
    if (i > upper.get()) {
      throw new IllegalArgumentException("can't set lower to" + i + " > upper");
    }
    lower.set(i);
  }

  public void setUpper(int i) {
    if (i < lower.get()) {
      throw new IllegalArgumentException("can't set upper to " + i + " < lower");
    }
  }

  public boolean isInRange(int i) {
    return i >= lower.get() && i <= upper.get();
  }
}
