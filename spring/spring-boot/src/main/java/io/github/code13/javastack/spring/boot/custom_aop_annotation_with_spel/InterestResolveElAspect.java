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

package io.github.code13.javastack.spring.boot.custom_aop_annotation_with_spel;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * InterestResolveElAspect.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/4/22 09:26
 */
@Aspect
class InterestResolveElAspect {

  private static final Logger logger = LoggerFactory.getLogger(InterestResolveElAspect.class);

  private final ExpressionParser expressionParser = new SpelExpressionParser();
  private final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer =
      new LocalVariableTableParameterNameDiscoverer();

  @Around("@annotation(anno)")
  public Object invoked(ProceedingJoinPoint joinPoint, Interest anno) throws Throwable {
    Object[] args = joinPoint.getArgs();
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

    EvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    String keySpel = anno.key();
    Expression keyExpression = expressionParser.parseExpression(keySpel);
    String key = keyExpression.getValue(context, String.class);

    String unlessSpel = anno.unless();
    Expression unlessExpression = expressionParser.parseExpression(unlessSpel);
    Boolean unless = unlessExpression.getValue(context, Boolean.class);

    logger.info(
        "call InterestResolveElAspect.invoked; keySpel:[{}], key:[{}],unlessSpel:[{}],unless:[{}]",
        keySpel,
        key,
        unlessSpel,
        unless);

    doInvoke(key, unless);

    return joinPoint.proceed();
  }

  private void doInvoke(String key, Boolean unless) {
    // 在此处处理你想要的逻辑
  }
}
