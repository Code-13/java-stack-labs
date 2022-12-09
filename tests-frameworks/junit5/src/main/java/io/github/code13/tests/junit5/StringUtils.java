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

package io.github.code13.tests.junit5;

import java.util.stream.IntStream;

/**
 * StringUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/5/2021 12:28 PM
 */
public final class StringUtils {

  public static boolean isPalindrome(String originalString) {

    String tempString = originalString.replaceAll("\\s+", "").toLowerCase();

    return IntStream.range(0, tempString.length() / 2)
        .noneMatch(i -> tempString.charAt(i) != tempString.charAt(tempString.length() - i - 1));
  }
}
