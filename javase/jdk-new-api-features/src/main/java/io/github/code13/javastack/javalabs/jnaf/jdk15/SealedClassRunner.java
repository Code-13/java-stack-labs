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

package io.github.code13.javastack.javalabs.jnaf.jdk15;

/**
 * SealedRunner.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/16 23:44
 */
public class SealedClassRunner {

  public static sealed class Person permits Student, Teacher, Worker {}

  public static sealed class Student extends Person permits MiddleSchoolStudent {}

  public static final class Teacher extends Person {}

  public static final class MiddleSchoolStudent extends Student {}

  public static non-sealed class Worker extends Person {}

  public static class RailWayWorker extends Worker {}

}
