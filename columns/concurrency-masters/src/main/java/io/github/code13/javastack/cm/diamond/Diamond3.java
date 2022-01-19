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

package io.github.code13.javastack.cm.diamond;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Diamond3.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/7 10:27
 */
@DisplayName("王者并发课-钻石03：琳琅满目-细数CompletableFuture的那些花式玩法 ")
class Diamond3 {

  @Test
  @DisplayName("CompletableFuture的核心设计")
  void test1() {

    /*
     * 总体而言，CompletableFuture实现了Future和CompletionStage两个接口，并且只有少量的属性。
     * 但是，它有近2400余行的代码，并且关系复杂。所以，在核心设计方面，我们不会展开讨论。
     *
     * 现在，你已经知道，Future接口仅提供了get()和isDone这样的简单方法，仅凭Future无法为CompletableFuture提供丰富的能力。
     * 那么，CompletableFuture又是如何扩展自己的能力的呢？这就不得不说CompletionStage接口了，它是CompletableFuture核心，也是我们要关注的重点。
     *
     * 顾名思义，根据CompletionStage名字中的“Stage”，你可以把它理解为任务编排中的步骤。
     * 所谓步骤，即任务编排的基本单元，它可以是一次纯粹的计算或者是一个特定的动作。
     * 在一次编排中，会包含多个步骤，这些步骤之间会存在依赖、链式和组合等不同的关系，也存在并行和串行的关系。这种关系，类似于Pipeline或者流式计算。
     * 既然是编排，就需要维护任务的创建、建立计算关系。
     * 为此，CompletableFuture提供了多达50多个方法，在数量上确实庞大且令人瞠目结舌，想要全部理解显然不太可能，当然也没有必要。
     * 虽然CompletableFuture的方法数量众多，但是在理解时仍有规律可循，我们可以通过分类的方式简化对方法的理解，理解了类型和变种，基本上我们也就掌握了CompletableFuture的核心能力。
     */
  }

  @Test
  @DisplayName("runAsync")
  void test2() {

    CompletableFuture.runAsync(() -> System.out.println("runAsync"));
  }

  @Test
  @DisplayName("supply与supplyAsync")
  void test3() throws ExecutionException, InterruptedException {

    var future = CompletableFuture.supplyAsync(() -> "supplyAsync");

    var promise = future.thenApply(s -> s + "thenApply");

    System.out.println(promise.get());
  }

  @Test
  @DisplayName("thenApply与thenApplyAsync")
  void test4() {
    // 见上
  }

  @Test
  @DisplayName("thenAccept与thenAcceptAsync")
  void test5() {
    var future = CompletableFuture.supplyAsync(() -> "thenAccept与thenAcceptAsync");

    future.thenAccept(System.out::println);
  }

  @Test
  @DisplayName("thenRun")
  void test6() {

    var future = CompletableFuture.supplyAsync(() -> "thenRun");

    future.thenRun(() -> System.out.println("thenRun"));
  }

  @Test
  @DisplayName("thenCompose与 thenCombine")
  void test7() throws ExecutionException, InterruptedException {

    // 编排两个存在依赖关系的任务

    var future = CompletableFuture.supplyAsync(() -> "thenRun");

    var promise = future.thenCompose(s -> CompletableFuture.supplyAsync(() -> "thenCompose，" + s));

    System.out.println(promise.get());

    //    组合两个相互独立的任务

    var roundsFuture = CompletableFuture.supplyAsync(() -> 500);
    var winRoundsFuture = CompletableFuture.supplyAsync(() -> 365);

    CompletableFuture<? extends Serializable> combine =
        roundsFuture.thenCombine(
            winRoundsFuture,
            (rounds, winRounds) -> {
              if (rounds == 0) {
                return 0.0;
              }
              DecimalFormat df = new DecimalFormat("0.00");
              return df.format((float) winRounds / rounds);
            });

    System.out.println(combine.get());
  }

  /**
   * allOf()与anyOf()也是一对孪生兄弟，当我们需要对多个Future的运行进行组织时，就可以考虑使用它们：
   *
   * <p>allOf()：给定一组任务，等待所有任务执行结束； anyOf()：给定一组任务，等待其中任一任务执行结束。
   */
  @Test
  @DisplayName("allOf与anyOf")
  void test8() throws ExecutionException, InterruptedException {

    CompletableFuture<Integer> roundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(200);
                return 500;
              } catch (InterruptedException e) {
                return null;
              }
            });
    CompletableFuture<Integer> winRoundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(100);
                return 365;
              } catch (InterruptedException e) {
                return null;
              }
            });

    CompletableFuture<Object> completedFuture =
        CompletableFuture.anyOf(winRoundsFuture, roundsFuture);
    System.out.println(completedFuture.get()); // 返回365

    CompletableFuture<Void> completedFutures =
        CompletableFuture.allOf(winRoundsFuture, roundsFuture);
  }

  @Test
  @DisplayName("CompletableFuture中的异常处理: exceptionally() 仅在发生异常时才会调用")
  void test9() throws ExecutionException, InterruptedException {

    CompletableFuture<Integer> roundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(200);
                return 0;
              } catch (InterruptedException e) {
                return null;
              }
            });
    CompletableFuture<Integer> winRoundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(100);
                return 365;
              } catch (InterruptedException e) {
                return null;
              }
            });

    CompletableFuture<? extends Serializable> winRateFuture =
        roundsFuture
            .thenCombine(
                winRoundsFuture,
                (rounds, winRounds) -> {
                  if (rounds == 0) {
                    throw new RuntimeException("总场次错误");
                  }
                  DecimalFormat df = new DecimalFormat("0.00");
                  return df.format((float) winRounds / rounds);
                })
            .exceptionally(
                ex -> {
                  System.out.println("出错：" + ex.getMessage());
                  return "";
                });

    System.out.println(winRateFuture.get());
  }

  @Test
  @DisplayName("CompletableFuture中的异常处理: handle() 无论是否发生异常，都会调用它")
  void test10() throws ExecutionException, InterruptedException {

    CompletableFuture<Integer> roundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(200);
                return 0;
              } catch (InterruptedException e) {
                return null;
              }
            });
    CompletableFuture<Integer> winRoundsFuture =
        CompletableFuture.supplyAsync(
            () -> {
              try {
                Thread.sleep(100);
                return 365;
              } catch (InterruptedException e) {
                return null;
              }
            });

    CompletableFuture<? extends Serializable> winRateFuture =
        roundsFuture
            .thenCombine(
                winRoundsFuture,
                (rounds, winRounds) -> {
                  if (rounds == 0) {
                    throw new RuntimeException("总场次错误");
                  }
                  DecimalFormat df = new DecimalFormat("0.00");
                  return df.format((float) winRounds / rounds);
                })
            .handle(
                (res, ex) -> {
                  if (ex != null) {
                    System.out.println("出错：" + ex.getMessage());
                    return "";
                  }
                  return res;
                });

    System.out.println(winRateFuture.get());
  }

  @Test
  @DisplayName("指定线程池")
  void test11() {

    // CompletableFuture中的任务有同步、异步和指定线程池三个变种，
    // 都可以传递变种

  }
}
