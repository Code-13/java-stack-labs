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

package io.github.code13.javastack.ck.platinum;

import org.junit.jupiter.api.DisplayName;

/**
 * 王者并发课-铂金10：能工巧匠-ThreadLocal如何为线程打造私有数据空间.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/27 11:42
 */
@DisplayName("王者并发课-铂金10：能工巧匠-ThreadLocal如何为线程打造私有数据空间")
class Platinum10 {

  /*
   * 在多线程并发场景中，如果你需要为每个线程设置可以跨越类和方法层面的私有变量，那么你就需要考虑使用ThreadLocal了
   */

  /*
   * ThreadLocal造成内存泄漏的原因就是,不再被使用的Entry没有从线程私有Map中清除,而清除不再被使用的Entry有两种方式:
   *
   * 调用ThreadLocal#remove(),手动把Entry从Map中移除
   * 将外界对ThreadLocal实例的引用置为null,等到GC之后,线程下一次访问ThreadLocal时,利用ThreadLocalMap的自动清除机制去清除过期Entry.
   *
   * 第2种方式是不可靠的,因为get()只在线性探测时"顺带地"清除过期Entry,set()也只是遍历Log2(N)个Entry的方式去"随缘"检测过期Entry并清楚,如果依赖ThreadLocalMap的清除机制,就很有可能发生内存泄漏.
   */
}
