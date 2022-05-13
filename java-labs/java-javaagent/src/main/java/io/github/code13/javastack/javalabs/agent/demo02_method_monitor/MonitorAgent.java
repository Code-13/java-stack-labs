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

package io.github.code13.javastack.javalabs.agent.demo02_method_monitor;

import java.lang.instrument.Instrumentation;

/**
 * HelloAgent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/13 21:39
 */
public class MonitorAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("method monitor agent:  " + agentArgs);
    inst.addTransformer(new MonitorTransformer());
  }
}
