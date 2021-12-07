/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch4_composing_objects;

import io.github.code13.javastack.books.jcip.Immutable;

/**
 * Point.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/7/2021 2:55 PM
 */
@Immutable
public class Point {

  final int x;
  final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
