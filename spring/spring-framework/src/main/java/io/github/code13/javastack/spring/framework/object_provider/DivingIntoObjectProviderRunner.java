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

package io.github.code13.javastack.spring.framework.object_provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DivingIntoObjectProviderRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/23 17:48
 */
public class DivingIntoObjectProviderRunner {

  @Test
  @DisplayName("testExampleOneMissingBean")
  void testExampleOneMissingBean() {

    Assertions.assertThrows(
        UnsatisfiedDependencyException.class,
        () -> {
          try (var context = new AnnotationConfigApplicationContext()) {
            context.register(ExampleOne.class);
            context.refresh();

            ExampleOne bean = context.getBean(ExampleOne.class);
            bean.runApps();
          }
        });
  }

  @Test
  void testExampleTwoWithOneLogger() {
    try (var context = new AnnotationConfigApplicationContext()) {
      context.register(ExampleTwo.class, PlainLogger.class); // Register the dependency
      context.refresh();

      ExampleTwo example = context.getBean(ExampleTwo.class);
      example.runApps();
    }
  }

  @Test
  public void testExampleTwoWithMultipleLoggers() {
    Assertions.assertThrows(
        NoUniqueBeanDefinitionException.class,
        () -> {
          try (var context = new AnnotationConfigApplicationContext()) {
            context.register(ExampleTwo.class, PlainLogger.class, JsonLogger.class);
            context.refresh();

            ExampleTwo example = context.getBean(ExampleTwo.class);
            example.runApps();
          }
        });
  }

  @Test
  public void testExampleThreeWithMultipleLoggers() {
    try (var context = new AnnotationConfigApplicationContext()) {
      context.register(ExampleThree.class, PlainLogger.class, JsonLogger.class);
      context.refresh();

      ExampleThree example = context.getBean(ExampleThree.class);
      example.runApps();
    }
  }

  @Test
  public void testExampleFourWithNoLoggers() {
    try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
      context.register(ExampleFour.class);
      context.refresh();

      ExampleFour example = context.getBean(ExampleFour.class);
      example.runApps();
    }
  }

  @Test
  public void testExampleFiveWithOneLogger() {
    try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
      context.register(ExampleFive.class, JsonLogger.class);
      context.refresh();

      ExampleFive example = context.getBean(ExampleFive.class);
      example.runApps();
    }
  }

  static class ExampleOne {

    private final LogService logService;

    public ExampleOne(LogService logService) {
      this.logService = logService;
    }

    public void runApps() {
      logService.log("some data");
    }
  }

  static class ExampleTwo {

    private final ObjectProvider<LogService> logService;

    public ExampleTwo(ObjectProvider<LogService> logService) {
      this.logService = logService;
    }

    public void runApps() {
      logService.ifAvailable(e -> e.log("some data"));
    }
  }

  static class ExampleThree {

    private final ObjectProvider<LogService> logService;

    public ExampleThree(ObjectProvider<LogService> logService) {
      this.logService = logService;
    }

    public void runApps() {
      logService.stream().forEach(e -> e.log("some app data with " + getClass().getSimpleName()));
    }
  }

  static class ExampleFour {

    private final ObjectProvider<LogService> logService;

    public ExampleFour(ObjectProvider<LogService> logService) {
      this.logService = logService;
    }

    LogService getLogService() {
      // use PlainLogger if not available.
      return logService.getIfAvailable(PlainLogger::new);
    }

    public void runApps() {
      getLogService().log("some app data with " + getClass().getSimpleName());
    }
  }

  static class ExampleFive {

    private final ObjectProvider<LogService> logService;

    public ExampleFive(ObjectProvider<LogService> logService) {
      this.logService = logService;
    }

    public void runApps() {
      logService.ifAvailable(e -> e.log("some app data with " + getClass().getSimpleName()));
    }
  }

  interface LogService {
    void log(String data);
  }

  static class PlainLogger implements LogService {
    @Override
    public void log(String data) {
      System.out.printf("Data [%s] at %d%n", data, System.currentTimeMillis());
    }
  }

  static class JsonLogger implements LogService {
    @Override
    public void log(String data) {
      System.out.printf(
          "{\"log\": { \"message\": \"%s\", \"timestamp\": %d } }",
          data, System.currentTimeMillis());
    }
  }
}
