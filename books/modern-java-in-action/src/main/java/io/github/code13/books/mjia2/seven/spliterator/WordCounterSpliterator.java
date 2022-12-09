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

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * WordCounterSpliterator.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 14:01
 */
public class WordCounterSpliterator implements Spliterator<Character> {

  private final String string;

  private int currentChar = 0;

  public WordCounterSpliterator(String string) {
    this.string = string;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Character> action) {
    action.accept(string.charAt(currentChar++));
    return currentChar < string.length();
  }

  @Override
  public Spliterator<Character> trySplit() {
    int currentSize = string.length() - currentChar;
    if (currentSize < 10) {
      return null;
    }
    for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
      if (Character.isWhitespace(string.charAt(splitPos))) {
        Spliterator<Character> spliterator =
            new WordCounterSpliterator(string.substring(currentChar, splitPos));
        currentChar = splitPos;
        return spliterator;
      }
    }
    return null;
  }

  @Override
  public long estimateSize() {
    return (long) string.length() - currentChar;
  }

  @Override
  public int characteristics() {
    return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
  }
}
