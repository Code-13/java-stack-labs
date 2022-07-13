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

package io.github.code13.javastack.books.jcip.ch11_performance_and_scalability;

import io.github.code13.javastack.books.jcip.GuardedBy;
import io.github.code13.javastack.books.jcip.Terrible;
import io.github.code13.javastack.books.jcip.ThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * AttributeStore.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/18 23:34
 */
@ThreadSafe
public class AttributeStore {

  @GuardedBy("this")
  private final Map<String, String> attributes = new HashMap<>();

  @Terrible
  public synchronized boolean userLocationMatches(String name, String regexp) {
    String key = "users." + name + ".location";
    String location = attributes.get(key);
    if (location == null) {
      return false;
    }
    return Pattern.matches(regexp, location);
  }
}
