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

package io.github.code13.javastack.javalabs.agent.demo05_threadlocal_track;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Default;
import net.bytebuddy.agent.builder.AgentBuilder.Listener;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * TrackAgent.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 10:00
 */
public class TrackAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("基于javaagent链路追踪");

    AgentBuilder agentBuilder = new Default();

    AgentBuilder.Transformer transformer =
        (builder, typeDescription, classLoader, module, protectionDomain) ->
            builder.visit(
                Advice.to(TrackAdvice.class)
                    .on(
                        ElementMatchers.isMethod()
                            .and(ElementMatchers.any())
                            .and(ElementMatchers.not(ElementMatchers.nameStartsWith("main")))));

    agentBuilder =
        agentBuilder
            .type(
                ElementMatchers.nameStartsWith(
                    "io.github.code13.javastack.javalabs.agent.demo05_threadlocal_track"))
            .transform(transformer)
            .asTerminalTransformation();

    AgentBuilder.Listener listener =
        new Listener() {
          @Override
          public void onDiscovery(
              String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {}

          @Override
          public void onTransformation(
              TypeDescription typeDescription,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded,
              DynamicType dynamicType) {
            System.out.println("onTransformation：" + typeDescription);
          }

          @Override
          public void onIgnored(
              TypeDescription typeDescription,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded) {}

          @Override
          public void onError(
              String typeName,
              ClassLoader classLoader,
              JavaModule module,
              boolean loaded,
              Throwable throwable) {}

          @Override
          public void onComplete(
              String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {}
        };

    agentBuilder.with(listener).installOn(inst);
  }
}
