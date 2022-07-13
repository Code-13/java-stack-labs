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

import java.util.concurrent.locks.Lock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 并发王者课-铂金1：探本溯源-为何说Lock接口是Java中锁的基础.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/23 14:28
 */
@DisplayName("并发王者课-铂金1：探本溯源-为何说Lock接口是Java中锁的基础 ")
class Platinum1 {

  /*
   * Lock 接口
   *
   * void lock()：获取锁。如果当前锁不可用，则会被阻塞直至锁释放；
   * void lockInterruptibly()：获取锁并允许被中断。这个方法和lock()类似，不同的是，它允许被中断并抛出中断异常。
   * boolean tryLock()：尝试获取锁。会立即返回结果，而不会被阻塞。
   * boolean tryLock(long timeout, TimeUnit timeUnit)：尝试获取锁并等待一段时间。这个方法和tryLock()，但是它会根据参数等待–会，如果在规定的时间内未能获取到锁就会放弃；
   * void unlock()：释放锁。
   * .
   */

  /*
   * Lock有一些synchronized所不具备的优势:
   * <p>1. synchronized用于方法体或代码块，而Lock可以灵活使用，甚至可以跨越方法；
   * <p>2. synchronized没有公平性，任何线程都可以获取并长期持有，从而可能饿死其他线程。而基于Lock接口，我们可以实现公平锁，从而避免一些线程活跃性问题；
   * <p>3. synchronized被阻塞时只有等待，而Lock则提供了tryLock方法，可以快速试错，并可以设定时间限制，使用时更加灵活；
   * <p>4. synchronized不可以被中断，而Lock提供了lockInterruptibly方法，可以实现中断。
   */

  static class WildMonster {

    private boolean isWildMonsterBeenKilled;

    private final Lock lock = new WildMonsterLock();

    public synchronized void killWildMonster() {
      lock.lock();
      try {
        String playerName = Thread.currentThread().getName();
        if (isWildMonsterBeenKilled) {
          System.out.println(playerName + "未斩杀野怪失败...");
          return;
        }
        isWildMonsterBeenKilled = true;
        System.out.println(playerName + "斩获野怪！");
      } finally {
        lock.unlock();
      }
    }
  }

  @Test
  @DisplayName("自定义Lock")
  void test1() {

    var wildMonster = new WildMonster();

    String[] players = {"哪吒", "兰陵王", "铠", "典韦"};

    var i = 0;
    for (String player : players) {
      i += 2;
      var thread = new Thread(wildMonster::killWildMonster, player);
      thread.setPriority(i);
      thread.start();
    }

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /** {@link WildMonsterReentrantLock}. */
  @Test
  @DisplayName("可重入Lock")
  void test2() {

    var wildMonster = new WildMonster();

    String[] players = {"哪吒", "兰陵王", "铠", "典韦"};

    var i = 0;
    for (String player : players) {
      i += 2;
      var thread = new Thread(wildMonster::killWildMonster, player);
      thread.setPriority(i);
      thread.start();
    }

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
