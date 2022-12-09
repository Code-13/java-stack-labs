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

package io.github.code13.spring.boot.caaws.internal;

import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * AopSpelResolveService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022 /7/11 09:29
 */
public interface AopSpelResolveService {

  /** The constant expressionParser. */
  ExpressionParser expressionParser = new SpelExpressionParser();

  /** The constant parameterNameDiscoverer. */
  LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer =
      new LocalVariableTableParameterNameDiscoverer();

  /**
   * Gets evaluation context.
   *
   * @param joinPoint ProceedingJoinPoint
   * @return the evaluation context
   */
  default EvaluationContext getEvaluationContext(ProceedingJoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    String[] parameterNames = getParameterNameDiscoverer().getParameterNames(method);
    if (parameterNames == null) {
      throw new IllegalArgumentException("cant get ParameterNames for method:" + method.getName());
    }

    EvaluationContext context = new StandardEvaluationContext();

    int length = parameterNames.length;
    for (int i = 0; i < length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    return context;
  }

  /**
   * Resolve spel.
   *
   * @param <T> the type parameter
   * @param evaluationContext the evaluation context
   * @param spelSupplier the spel supplier
   * @param desiredResultType the desired result type
   * @return the t
   */
  default <T> T resolveSpel(
      EvaluationContext evaluationContext,
      Supplier<String> spelSupplier,
      Class<T> desiredResultType) {
    Expression expression = getExpressionParser().parseExpression(spelSupplier.get());
    return expression.getValue(evaluationContext, desiredResultType);
  }

  /**
   * Gets expression parser.
   *
   * @return ExpressionParser
   */
  default ExpressionParser getExpressionParser() {
    return expressionParser;
  }

  /**
   * Gets parameter name discoverer.
   *
   * @return ParameterNameDiscoverer
   */
  default ParameterNameDiscoverer getParameterNameDiscoverer() {
    return parameterNameDiscoverer;
  }
}
