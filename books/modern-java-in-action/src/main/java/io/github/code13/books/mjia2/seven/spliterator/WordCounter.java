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

package io.github.code13.books.mjia2.seven.spliterator;

/**
 * WordCounter.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 13:59
 */
public class WordCounter {

  private final int counter;

  private final boolean lastSpace;

  public WordCounter(int counter, boolean lastSpace) {
    this.counter = counter;
    this.lastSpace = lastSpace;
  }

  public WordCounter accumulate(Character c) {
    if (Character.isWhitespace(c)) {
      return lastSpace ? this : new WordCounter(counter, true);
    } else {
      return lastSpace ? new WordCounter(counter + 1, false) : this;
    }
  }

  public WordCounter combine(WordCounter wordCounter) {
    return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
  }

  public int getCounter() {
    return counter;
  }
}
