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
 * ConstraintViolationRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 15:52
 */
class ConstraintViolationRunner {

  /*
   * 已经插值（interpolated）的消息
   *     String getMessage();
   * 未插值的消息模版（里面变量还未替换，若存在的话）
   *     String getMessageTemplate();
   *
   * 从rootBean开始的属性路径。如：parent.fullName
   *     Path getPropertyPath();
   * 告诉是哪个约束没有通过（的详情）
   *     ConstraintDescriptor<?> getConstraintDescriptor();
   */
}
