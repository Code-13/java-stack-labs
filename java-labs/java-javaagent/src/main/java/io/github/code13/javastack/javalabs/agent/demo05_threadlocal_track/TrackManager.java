/*
 *
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

package io.github.code13.javastack.javalabs.agent.demo05_threadlocal_track;

import java.util.Stack;

/**
 * TrackManager.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 09:52
 */
public class TrackManager {

  private static final ThreadLocal<Stack<String>> track = new ThreadLocal<>();

  private static String createSpan() {
    Stack<String> stack = track.get();
    if (stack == null) {
      stack = new Stack<>();
      track.set(stack);
    }
    String linkId;
    if (stack.isEmpty()) {
      linkId = TrackContext.getLinkId();
      if (linkId == null) {
        linkId = "nvl";
        TrackContext.setLinkId(linkId);
      }
    } else {
      linkId = stack.peek();
      TrackContext.setLinkId(linkId);
    }
    return linkId;
  }

  public static String createEntrySpan() {
    String span = createSpan();
    Stack<String> stack = track.get();
    stack.push(span);
    return span;
  }

  public static String getExitSpan() {
    Stack<String> stack = track.get();
    if (stack == null || stack.isEmpty()) {
      TrackContext.clear();
      return null;
    }
    return stack.pop();
  }

  public static String getCurrentSpan() {
    Stack<String> stack = track.get();
    if (stack == null || stack.isEmpty()) {
      return null;
    }
    return stack.peek();
  }
}
