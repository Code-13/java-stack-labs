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

import io.github.code13.spring.boot.caaws.Interest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;

/**
 * InternalInterestResolveElAspect.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/11 10:04
 */
@Aspect
public class InternalInterestResolveElAspect implements AopSpelResolveService {

  private static final Logger logger =
      LoggerFactory.getLogger(InternalInterestResolveElAspect.class);

  @Around("@annotation(anno)")
  public Object invoked(ProceedingJoinPoint joinPoint, Interest anno) throws Throwable {
    EvaluationContext evaluationContext = getEvaluationContext(joinPoint);

    String key = resolveSpel(evaluationContext, anno::key, String.class);
    Boolean unless = resolveSpel(evaluationContext, anno::unless, Boolean.class);

    doInvoke(key, unless);

    return joinPoint.proceed();
  }

  private void doInvoke(String key, Boolean unless) {
    logger.info("call InterestResolveElAspect.invoked, key:[{}],unless:[{}]", key, unless);
    // 在此处处理你想要的逻辑
  }
}
