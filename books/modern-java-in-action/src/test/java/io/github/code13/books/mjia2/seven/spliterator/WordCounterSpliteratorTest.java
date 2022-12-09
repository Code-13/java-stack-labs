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

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WordCounterSpliteratorTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 14:11
 */
class WordCounterSpliteratorTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(WordCounterSpliteratorTest.class);

  static final String SENTENCE =
      " Nel mezzo del cammin di nostra vita "
          + "mi ritrovai in una selva oscura"
          + " ché la dritta via era smarrita ";

  @Test
  public void test() {
    WordCounterSpliterator wordCounterSpliterator = new WordCounterSpliterator(SENTENCE);
    Stream<Character> stream = StreamSupport.stream(wordCounterSpliterator, true);
    WordCounter wordCounter =
        stream.reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);

    LOGGER.info("字符计算: {}", wordCounter.getCounter());
  }
}
