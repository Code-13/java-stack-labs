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

package io.github.code13.javastack.libs.vavr;

import io.vavr.*;
import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * vavr Functions
 * <p>
 * https://docs.vavr.io/#_functions
 *
 * <p>
 * Functional programming is all about values and transformation of values using functions.
 * Java 8 just provides a Function which accepts one parameter and a BiFunction which accepts two parameters.
 * Vavr provides functions up to a limit of 8 parameters.
 * The functional interfaces are of called Function0, Function1, Function2, Function3 and so on.
 * If you need a function which throws a checked exception you can use CheckedFunction1, CheckedFunction2 and so on.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/23 15:04
 */
@DisplayName("Functions")
class FunctionsTest {

  @Test
  @DisplayName("Example for vavr Functions")
  void test1() {
    Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;

    assertEquals(sum.apply(1, 1), 2);
  }

  /**
   * You can compose functions.
   * In mathematics, function composition is the application of one function to the result of another to produce a third function.
   * For instance, the functions f : X → Y and g : Y → Z can be composed to yield a function h: g(f(x)) which maps X → Z.
   */
  @Test
  @DisplayName("Composition-andThen")
  void test2() {
    Function1<Integer, Integer> plusOne = a -> a + 1;
    Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

    Function1<Integer, Integer> add1AndMultiplyBy2 = plusOne.andThen(multiplyByTwo);

    assertEquals(add1AndMultiplyBy2.apply(2), 6);
  }

  @Test
  @DisplayName("Composition-compose")
  void test3() {
    Function1<Integer, Integer> plusOne = a -> a + 1;
    Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

    Function1<Integer, Integer> add1AndMultiplyBy2 = multiplyByTwo.compose(plusOne);

    assertEquals(add1AndMultiplyBy2.apply(2), 6);
  }

  /**
   * You can lift a partial function into a total function that returns an Option result.
   * The term partial function comes from mathematics.
   * A partial function from X to Y is a function f: X′ → Y, for some subset X′ of X.
   * It generalizes the concept of a function f: X → Y by not forcing f to map every element of X to an element of Y.
   * That means a partial function works properly only for some input values.
   * If the function is called with a disallowed input value, it will typically throw an exception.
   */
  @Test
  @DisplayName("Lifting")
  void test4() {
    //The following method divide is a partial function that only accepts non-zero divisors.
    Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;

    // We use lift to turn divide into a total function that is defined for all inputs.
    Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

    // = None
    Option<Integer> i1 = safeDivide.apply(1, 0);

    // = Some(2)
    Option<Integer> i2 = safeDivide.apply(4, 2);

    System.out.println(i1);
    System.out.println(i2);
  }

  /**
   * Partial application allows you to derive a new function from an existing one by fixing some values.
   * You can fix one or more parameters, and the number of fixed parameters defines the arity of the new function such that new arity = (original arity - fixed parameters).
   * The parameters are bound from left to right.
   */
  @Test
  @DisplayName("Partial application")
  void test5() {
    Function2<Integer, Integer, Integer> sum1 = (a, b) -> a + b;
    Function1<Integer, Integer> add2 = sum1.apply(2); // 	The first parameter a is fixed to the value 2.

    assertEquals(add2.apply(4), 6);


    Function5<Integer, Integer, Integer, Integer, Integer, Integer> sum2 = (a, b, c, d, e) -> a + b + c + d + e;
    Function2<Integer, Integer, Integer> add6 = sum2.apply(2, 3, 1);

    assertEquals(add6.apply(4, 3), 13);

    //  Partial application differs from Currying
  }

  /**
   * Currying is a technique to partially apply a function by fixing a value for one of the parameters, resulting in a Function1 function that returns a Function1.
   */
  @Test
  @DisplayName("Currying")
  void test6() {

    //  When a Function2 is curried, the result is indistinguishable from the partial application of a Function2 because both result in a 1-arity function.
    Function2<Integer, Integer, Integer> sum1 = (a, b) -> a + b;

    Function1<Integer, Integer> _add2 = sum1.curried().apply(2);

    assertEquals(_add2.apply(4), 6);

    //  You might notice that, apart from the use of .curried(), this code is identical to the 2-arity given example in Partial application. With higher-arity functions, the difference becomes clear.

    Function3<Integer, Integer, Integer, Integer> sum2 = (a, b, c) -> a + b + c;

    Function1<Integer, Function1<Integer, Integer>> add2 = sum2.curried().apply(2);

    assertEquals(add2.apply(2).apply(2), 6);

  }

  /**
   * Memoization is a form of caching. A memoized function executes only once and then returns the result from a cache.
   * The following example calculates a random number on the first invocation and returns the cached number on the second invocation.
   */
  @Test
  @DisplayName("Memoization")
  void test7() {
    Function0<Double> hashCache = Function0.of(Math::random).memoized();

    double randomValue1 = hashCache.apply();
    double randomValue2 = hashCache.apply();

    assertEquals(randomValue1, randomValue2);
  }

}
