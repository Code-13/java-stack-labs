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

package io.github.code13.javase.agent.demo05_threadlocal_track;

import java.util.UUID;
import net.bytebuddy.asm.Advice;

/**
 * TrackAdvice.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 09:54
 */
public class TrackAdvice {

  @Advice.OnMethodEnter()
  public static void enter(
      @Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
    String linkId = TrackManager.getCurrentSpan();
    if (linkId == null) {
      linkId = UUID.randomUUID().toString();
      TrackContext.setLinkId(linkId);
    }

    String entrySpan = TrackManager.createEntrySpan();
    System.out.println("链路追踪：" + entrySpan + " " + className + "." + methodName);
  }

  @Advice.OnMethodEnter()
  public static void exit(
      @Advice.Origin("#t") String className, @Advice.Origin("#m") String methodName) {
    TrackManager.getExitSpan();
  }
}
