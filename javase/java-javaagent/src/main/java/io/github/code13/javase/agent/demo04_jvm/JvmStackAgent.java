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

package io.github.code13.javase.agent.demo04_jvm;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JvmStackAgent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 09:32
 */
public class JvmStackAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("this is jvm agent: " + agentArgs);

    Executors.newScheduledThreadPool(1)
        .scheduleAtFixedRate(
            () -> {
              JvmStack.printMemoryInfo();
              JvmStack.printGCInfo();
              System.out.println(
                  "============================================================================");
            },
            0,
            5000,
            TimeUnit.MILLISECONDS);
  }
}
