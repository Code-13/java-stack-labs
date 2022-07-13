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

package io.github.code13.javastack.libs.redisson.collections;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.Comparator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;

/**
 * SortedSetRunner.
 *
 * <p>Redis based distributed SortedSet for Java implements java.util.SortedSet interface. It uses
 * comparator to sort elements and keep uniqueness. For String data type it's recommended to use
 * LexSortedSet object due to performance gain.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/29 11:48
 */
@DisplayName("SortedSet")
class SortedSetRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @AfterAll
  static void setDown() {
    redissonClient.shutdown();
  }

  @Test
  @DisplayName("SortedSet")
  void sortedSet() {
    RSortedSet<Integer> set = redissonClient.getSortedSet("SortedSet");
    set.trySetComparator(new CusComparator()); // set object comparator
    set.add(3);
    set.add(1);
    set.add(2);

    set.removeAsync(0);
    set.addAsync(5);
  }

  static class CusComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
      return Integer.compare(o1, o2);
    }
  }
}
