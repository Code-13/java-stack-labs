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

package io.github.code13.jakartaee.beanvalidation.coreapi;

/**
 * TraversableResolverRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 16:36
 */
class TraversableResolverRunner {

  /*
   * 能跨越的处理器。从字面是非常不好理解，
   * 用粗暴的语言解释为：确定某个属性是否能被ValidationProvider访问，当没访问一个属性时都会通过它来判断一下子，提供两个判断方法
   * 该接口主要根据配置项来进行判断，并不负责。内部使用，调用者基本无需关心，也不见更改其默认机制，暂且略过。
   */

}
