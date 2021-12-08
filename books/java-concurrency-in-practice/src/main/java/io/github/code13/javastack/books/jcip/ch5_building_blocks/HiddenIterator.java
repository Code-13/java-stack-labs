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

package io.github.code13.javastack.books.jcip.ch5_building_blocks;

import io.github.code13.javastack.books.jcip.Terrible;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * HiddenIterator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/8/2021 11:50 AM
 */
@Terrible
public class HiddenIterator {

  private final Set<Integer> set = new HashSet<>();

  public synchronized void add(Integer i) {
    set.add(i);
  }

  public synchronized void remove(Integer i) {
    set.remove(i);
  }

  public void addTenThings() {
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      add(random.nextInt());
      System.out.println("DEBUG：added ten elements to" + set);
    }
  }
}
