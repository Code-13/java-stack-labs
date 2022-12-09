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
 * MessageInterpolatorRunner.
 *
 * @see javax.validation.MessageInterpolator
 * @see org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 16:29
 */
class MessageInterpolatorRunner {

  /*
   * 直译为：消息插值器。按字面不太好理解：
   * 简单的说就是对message内容进行格式化，若有占位符{}或者el表达式${}就执行替换和计算。对于语法错误应该尽量的宽容。
   *
   * 校验失败的消息模版交给它处理就成为了人能看得懂的消息格式，
   * 因此它能够处理消息的国际化：消息的key是同一个，但根据不同的Locale展示不同的消息模版。最后在替换/技术模版里面的占位符即可~
   */

}
