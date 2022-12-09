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

package io.github.code13.columns.ck.diamond;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diamond1.
 *
 * <p>钻石1 线程池
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/8/15 14:44
 */
@DisplayName("王者并发课-钻石1：明心见性-如何由表及里精通线程池设计与原理")
class Diamond1 {

  @Test
  @DisplayName("一、为什么要使用线程池")
  void test1() {

    /*
     * 频繁地繁创建和启用新的线程不仅代价昂贵，而且无限增加的线程势必也会造成管理成本的急剧上升。因此，为了平衡多线程的收益和成本，线程池诞生了。
     * 1. 线程池的使用场景
     * 生产者与消费者问题是线程池的典型应用场景。当你有源源不断的任务需要处理时，为了提高任务的处理速度，你需要创建多个线程。那么，问题来了，如何管理这些任务和多线程呢？答案是：线程池。
     * 线程池的池化（Pooling）原理的应用并不局限于Java中，在MySQL和诸多的分布式中间件系统中都有着广泛的应用。当我们链接数据库的时候，对链接的管理用的是线程池；当我们使用Tomcat时，对请求链接的管理用的也是线程池。所以，当你有批量的任务需要多线程处理时，那么基本上你就需要使用线程池。
     * 2. 线程池的使用好处 线程池的好处主要体现在三个方面：系统资源、任务处理速度和相关的复杂度管理，主要表现在：
     *
     * 降低系统的资源开销：通过复用线程池中的工作线程，避免频繁创建新的线程，可以有效降低系统资源的开销；
     * 提高任务的执行速度：新任务达到时，无需创建新的线程，直接将任务交由已经存在的线程进行处理，可以有效提高任务的执行速度；
     * 有效管理任务和工作线程：线程池内提供了任务管理和工作线程管理的机制。
     *
     * 3.为什么说创建线程是昂贵的 现在你已经知道，频繁地创建新线程需要付出额外的代价，所以我们使用了线程池。那么，创建一个新的线程的代价究竟是怎样的呢？可以参考以下几点：
     *
     * 创建线程时，JVM必须为线程堆栈分配和初始化一大块内存。每个线程方法的调用栈帧都会存储到这里，包括局部变量、返回值和常量池等； 在创建和注册本机线程时，需要和宿主机发生系统调用；
     * 需要创建、初始化描述符，并将其添加到 JVM 内部数据结构中。
     *
     * <p>另外，从某种意义上说，只要线程还活着，它就会占用资源，这不仅昂贵，而且浪费。 例如 ，线程堆栈、访问堆栈的可达对象、JVM
     * 线程描述符、操作系统本机线程描述符等等，在线程活着的时候，这些资源都会持续占据。 虽然不同的Java平台在创建线程时的代价可能有所差异，但总体来说，都不便宜。
     */
  }

  @Test
  @DisplayName("二、线程池的核心组成")
  void test2() {

    /*
     * 任务提交：提供接口接收任务的提交；
     * 任务管理：选择合适的队列对提交的任务进行管理，包括对拒绝策略的设置；
     * 任务执行：由工作线程来执行提交的任务；
     * 线程池管理：包括基本参数设置、任务监控、工作线程管理等。
     */
  }
}
