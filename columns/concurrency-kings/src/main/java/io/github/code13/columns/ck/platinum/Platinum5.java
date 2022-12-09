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

package io.github.code13.columns.ck.platinum;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-铂金5：致胜良器-无处不在的“阻塞队列”究竟是何面目.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/26 10:28
 */
@DisplayName("王者并发课-铂金5：致胜良器-无处不在的“阻塞队列”究竟是何面目")
class Platinum5 {

  /*
   * 当线程试着往队列里放数据时，如果它已经满了，那么线程将进入等待；
   * 而当线程试着从队列里取数据时，如果它已经空了，那么线程将进入等待。
   */

  @Test
  @DisplayName("简单的阻塞队列")
  void test1() throws InterruptedException {

    /**
     * 在下面的阻塞队列中，我们设计一个队列queue，并通过limit字段限定它的容量。
     * enqueue()方法用于向队列中放入数据，如果队列已满则等待；而dequeue()方法则用于从数据中取出数据，如果队列为空则等待.
     */
    class BlockingQueue {

      List<Object> queue = new LinkedList<>();
      private int limit;

      public BlockingQueue(int limit) {
        this.limit = limit;
      }

      public void print(Object... args) {
        StringBuilder message = new StringBuilder(getThreadName() + ":");
        for (Object arg : args) {
          message.append(arg);
        }
        System.out.println(message);
      }

      public String getThreadName() {
        return Thread.currentThread().getName();
      }

      public synchronized void enqueue(Object item) throws InterruptedException {
        while (queue.size() == limit) {
          print("队列已满，等待中...");
          wait();
        }
        queue.add(item);
        notifyAll();
        print(item, "已经放入！");
      }

      public synchronized Object dequeue() throws InterruptedException {
        while (queue.size() == 0) {
          print("队列空的，等待中...");
          wait();
        }

        if (queue.size() == limit) {
          notifyAll();
        }

        var o = queue.get(0);
        print(o, "已经拿到！");
        queue.remove(o);
        return o;
      }
    }

    BlockingQueue blockingQueue = new BlockingQueue(1);

    Thread lanLingWang =
        new Thread(
            () -> {
              try {
                String[] items = {"A", "B", "C", "D", "E", "F", "G"};
                for (String item : items) {
                  Thread.sleep(500);
                  blockingQueue.enqueue(item);
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            },
            "兰陵王");

    Thread niumo =
        new Thread(
            () -> {
              try {
                while (true) {
                  blockingQueue.dequeue();
                  Thread.sleep(1000);
                }
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            },
            "牛魔王");

    lanLingWang.start();
    niumo.start();

    Thread.currentThread().join();
  }
}
