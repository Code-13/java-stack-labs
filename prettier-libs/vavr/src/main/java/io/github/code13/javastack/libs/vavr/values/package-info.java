/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

/**
 * vavr Values
 * <p>
 * In a functional setting we see a value as a kind of normal form, an expression which cannot be further evaluated.
 * In Java we express this by making the state of an object final and call it immutable.
 * Vavr’s functional Value abstracts over immutable objects.
 * Efficient write operations are added by sharing immutable memory between instances. What we get is thread-safety for free!
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 15:48
 */
package io.github.code13.javastack.libs.vavr.values;