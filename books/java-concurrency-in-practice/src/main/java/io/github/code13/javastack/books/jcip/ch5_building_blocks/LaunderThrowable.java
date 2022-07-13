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

package io.github.code13.javastack.books.jcip.ch5_building_blocks;

/**
 * LaunderThrowable.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/8/2021 3:40 PM
 */
public class LaunderThrowable {

  /**
   * Coerce an unchecked Throwable to a RuntimeException
   *
   * <p>If the Throwable is an Error, throw it; if it is a RuntimeException return it, otherwise
   * throw IllegalStateException
   */
  public static RuntimeException launderThrowable(Throwable t) {
    if (t instanceof RuntimeException) {
      return (RuntimeException) t;
    } else if (t instanceof Error) {
      throw (Error) t;
    } else {
      throw new IllegalStateException("Not unchecked", t);
    }
  }
}
