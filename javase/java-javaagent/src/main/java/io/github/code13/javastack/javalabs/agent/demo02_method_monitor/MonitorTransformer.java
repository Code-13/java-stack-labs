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

package io.github.code13.javastack.javalabs.agent.demo02_method_monitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * MonitorTransformer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/13 22:22
 */
public class MonitorTransformer implements ClassFileTransformer {

  private static final Set<String> classNameSet = new HashSet<>();

  static {
    classNameSet.add(
        "io.github.code13.javastack.javalabs.agent.demo02_method_monitor.MonitorAgentTest");
  }

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {

    try {
      String currentClassName = className.replace("/", ".");
      if (!classNameSet.contains(currentClassName)) { // 提升classNameSet中含有的类
        return null;
      }
      System.out.println("transform: [" + currentClassName + "]");

      CtClass ctClass = ClassPool.getDefault().get(currentClassName);

      if (ctClass.isFrozen()){
        return null;
      }

      CtBehavior[] methods = ctClass.getDeclaredBehaviors();
      for (CtBehavior method : methods) {
        enhanceMethod(method);
      }

      return ctClass.toBytecode();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private void enhanceMethod(CtBehavior method) throws CannotCompileException {
    if (method.isEmpty()) {
      return;
    }
    String name = method.getName();
    if ("main".equalsIgnoreCase(name)) {
      return;
    }

    String source = """
        {
          long start = System.nanoTime();
          $_ = $proceed($$);
          System.out.println("method:[{1}]");
          System.out.println("cost: " + (System.nanoTime() - start));
        }
        """.replace("{1}",name);

    method.instrument(new ExprEditor(){
      @Override
      public void edit(MethodCall m) throws CannotCompileException {
        m.replace(source);
      }
    });
  }
}
