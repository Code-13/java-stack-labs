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

package io.github.code13.spring.framework.context.event;

import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Event_Runner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/1/23 20:59
 */
public class Event_Runner {

  /** 数据库数据对象 */
  @Data
  static class User {
    private String id;
    private String name;
  }

  @Data
  static class Car {
    private String id;
    private String name;
  }

  /** 数据库数据存储对象事件 */
  @ToString
  static class DataObjectEvent<T> implements ResolvableTypeProvider {

    private final T data;
    private final String type;

    public DataObjectEvent(T data, String type) {
      this.data = data;
      this.type = type;
    }

    @Override
    public ResolvableType getResolvableType() {
      // 可以监听泛型事件
      return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }

    public String getType() {
      return type;
    }

    public T getData() {
      return data;
    }
  }

  @Component
  static class EventListenerDemo {

    /** 监听泛型 <User> 事件 */
    @EventListener
    public void user(DataObjectEvent<User> userChangeEvent) {
      //  只能监听到 User
      System.out.println(Thread.currentThread().getName());
      System.out.println(userChangeEvent.toString());
    }

    /** 监听泛型 <User> 事件, 异步执行 */
    @Async
    @EventListener
    public void userAsync(DataObjectEvent<User> userChangeEvent) {
      //  只能监听到 User
      System.out.println(Thread.currentThread().getName());
      System.out.println(userChangeEvent.toString());
    }

    /** 监听泛型 <Car> 事件 */
    @EventListener
    public void car(DataObjectEvent<Car> userChangeEvent) {
      //  只能监听到 Car
      System.out.println(userChangeEvent.toString());
    }
  }

  @Configuration
  @EnableAsync
  static class EventRunnerConfig {}

  @Test
  @DisplayName("只有泛型 User 可以被监听到")
  void testUserEvent() {
    try (var applicationContext =
        new AnnotationConfigApplicationContext(getClass().getPackage().getName())) {
      User user = new User();
      user.setId("1");
      user.setName("Test");

      DataObjectEvent<User> event = new DataObjectEvent<>(user, "add");

      // 只有 User 的能监听到
      applicationContext.publishEvent(event);
    }
  }

  @Test
  @DisplayName("只有泛型 User 可以被监听到")
  void testUserEventAsync() {
    try (var applicationContext =
        new AnnotationConfigApplicationContext(getClass().getPackage().getName())) {

      applicationContext.registerBean(
          AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME,
          ThreadPoolTaskExecutor.class,
          bd -> bd.getPropertyValues().add("threadNamePrefix", "Event-Thread-"));

      ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
      constructorArgumentValues.addIndexedArgumentValue(0, applicationContext.getBeanFactory());

      MutablePropertyValues propertyValues = new MutablePropertyValues();
      propertyValues.add("taskExecutor", applicationContext.getBean(TaskExecutor.class));

      RootBeanDefinition beanDefinition =
          new RootBeanDefinition(
              SimpleApplicationEventMulticaster.class, constructorArgumentValues, propertyValues);

      applicationContext.registerBeanDefinition(
          AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, beanDefinition);

      User user = new User();
      user.setId("1");
      user.setName("Test");

      DataObjectEvent<User> event = new DataObjectEvent<>(user, "add");

      // 只有 User 的能监听到
      applicationContext.publishEvent(event);
    }
  }

  @Test
  @DisplayName("只有泛型 Car 可以被监听到")
  void testCarEvent() {
    try (var applicationContext =
        new AnnotationConfigApplicationContext(getClass().getPackage().getName())) {

      Car car = new Car();
      car.setId("2");
      car.setName("BMW3");

      DataObjectEvent<Car> event = new DataObjectEvent<>(car, "add");

      // 只有 Car 的能监听到
      applicationContext.publishEvent(event);
    }
  }
}
