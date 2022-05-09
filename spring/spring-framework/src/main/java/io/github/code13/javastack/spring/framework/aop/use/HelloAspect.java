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

package io.github.code13.javastack.spring.framework.aop.use;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * HelloAspect.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/9 15:12
 */
@Component
@Aspect
public class HelloAspect {

  // 定义一个切入点：指定哪些方法可以被切入（如果是别的类需要使用 请用该方法的全类名）
  @Pointcut("execution(* io.github.code13.javastack.spring.framework.aop.use..*.*(..))")
  public void pointCut() {
    //
  }

  @Before("pointCut()")
  public void doBefore(JoinPoint joinPoint) {
    System.out.println("AOP Before Advice...");
  }

  @After("pointCut()")
  public void doAfter(JoinPoint joinPoint) {
    System.out.println("AOP After Advice...");
  }

  @AfterReturning(pointcut = "pointCut()", returning = "returnVal")
  public void afterReturn(JoinPoint joinPoint, Object returnVal) {
    System.out.println("AOP AfterReturning Advice:" + returnVal);
  }

  @AfterThrowing(pointcut = "pointCut()", throwing = "error")
  public void afterThrowing(JoinPoint joinPoint, Throwable error) {
    System.out.println("AOP AfterThrowing Advice..." + error);
    System.out.println("AfterThrowing...");
  }
}
