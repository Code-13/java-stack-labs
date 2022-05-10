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

package io.github.code13.javastack.spring.framework.aop.proxy_factory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DecoratingProxy;

/**
 * ProxyFactoryRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/9 22:31
 */
public class ProxyFactoryRunner {

  @Test
  @DisplayName("test_proxyFactory_With_Interface")
  void test_proxyFactory_With_Interface() {
    ProxyFactory proxyFactory = new ProxyFactory(new HelloServiceImpl());
    proxyFactory.addAdvice(
        new MethodBeforeAdvice() {
          @Override
          public void before(Method method, Object[] args, Object target) throws Throwable {
            System.out.println("你被拦截了：方法名为：" + method.getName() + " 参数为--" + Arrays.asList(args));
          }
        });

    HelloService proxy = (HelloService) proxyFactory.getProxy();
    proxy.hello();

    System.out.println(proxyFactory.getTargetClass());
    System.out.println(proxyFactory.getTargetSource());
    System.out.println(Arrays.toString(proxyFactory.getProxiedInterfaces()));
    System.out.println(Arrays.toString(proxyFactory.getAdvisors()));

    // 获取类型，看看是JDK代理还是cglib的
    System.out.println(proxy instanceof Proxy); // true  所有的JDK代理都是继承自Proxy的
    System.out.println(proxy instanceof SpringProxy); // true
    System.out.println(proxy.getClass()); // class
    // io.github.code13.javastack.spring.framework.aop.proxy_factory.$Proxy10
    System.out.println(Proxy.isProxyClass(proxy.getClass())); // true
    System.out.println(AopUtils.isCglibProxy(proxy)); // false

    // 测试Advised接口、DecoratingProxy的内容
    Advised advised = (Advised) proxy;
    System.out.println(
        Arrays.asList(
            advised.getProxiedInterfaces())); // [interface com.fsx.maintest.DemoInterface]
    System.out.println(
        Arrays.asList(
            advised.getAdvisors())); // [org.springframework.aop.support.DefaultPointcutAdvisor:
    // pointcut [Pointcut.TRUE]; advice
    // [com.fsx.maintest.Main$$Lambda$2/1349414238@2ef5e5e3]]
    System.out.println(advised.isExposeProxy()); // false
    System.out.println(advised.isFrozen()); // false

    // System.out.println(advised.removeAdvice(null));
    DecoratingProxy decoratingProxy = (DecoratingProxy) proxy;
    System.out.println(decoratingProxy.getDecoratedClass());

    System.out.println("-----------------------------------------------------");

    // Object的方法 ==== 所有的Object方法都不会被AOP代理 这点需要注意
    System.out.println(proxy.equals(new Object()));
    System.out.println(proxy.hashCode());
    System.out.println(proxy.getClass());

    // 其余方法都没被拦截  只有toString()被拦截了  咋回事呢？它也不符合切点表达式的要求啊  看下面的解释吧
    // 你被拦截了：方法名为：hello 参数为--[]
    System.out.println(proxy.toString());
  }

  @Test
  @DisplayName("test_proxyFactory_no_with_interface")
  void test_proxyFactory_no_with_interface() {
    ProxyFactory proxyFactory = new ProxyFactory(new HelloServiceNoInterface());
    proxyFactory.addAdvice(
        new MethodBeforeAdvice() {
          @Override
          public void before(Method method, Object[] args, Object target) throws Throwable {
            System.out.println("你被拦截了：方法名为：" + method.getName() + " 参数为--" + Arrays.asList(args));
          }
        });

    HelloServiceNoInterface proxy = (HelloServiceNoInterface) proxyFactory.getProxy();
    proxy.hello();
  }

  interface HelloService {
    void hello();
  }

  static class HelloServiceImpl implements HelloService {

    @Override
    public void hello() {
      System.out.println("say hello");
    }
  }

  static class HelloServiceNoInterface {
    public void hello() {
      System.out.println("say hello");
    }
  }
}
