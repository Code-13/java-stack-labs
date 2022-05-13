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

package io.github.code13.javastack.javalabs.agent.demo03_bytebuddy_monitor;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Listener;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

/**
 * ByteBuddyMonitor.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/13 23:10
 */
public class ByteBuddyMonitorAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("byte buddy agent：" + agentArgs);

    AgentBuilder.Transformer transformer =
        (builder, typeDescription, classLoader, module) ->
            builder
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(MethodCostTime.class));

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
              DynamicType dynamicType) {}

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

    new AgentBuilder.Default()
        .type(
            ElementMatchers.nameStartsWith(
                "io.github.code13.javastack.javalabs.agent.demo03_bytebuddy_monitor"))
        .transform(transformer)
        .with(listener)
        .installOn(inst);
  }
}
