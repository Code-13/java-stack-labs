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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 王者并发课-钻石1：明心见性-如何由表及里精通线程池设计与原理.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/7/27 13:42
 */
@DisplayName("王者并发课-钻石1：明心见性-如何由表及里精通线程池设计与原理")
class Platinum11 {

  /*
   * 线程池的使用场景：
   * 1.生产者与消费者问题是线程池的典型应用场景。
   * 2.当你有批量的任务需要多线程处理时，那么基本上你就需要使用线程池
   */

  /*
   * 线程池的使用好处:
   * 1. 降低系统的资源开销
   * 2. 提高任务的执行速度
   * 3. 有效管理任务和工作线程
   */

  /*
   * 线程池的核心组成：
   * 1.任务提交
   * 2.任务管理
   * 3.任务执行
   * 4.线程池管理
   */

  @Test
  @DisplayName("自定义一个线程池")
  void test1() {
    var executor = new CustomerTheadPoolExecutor(5, new ArrayBlockingQueue<>(10));

    String[] wildMonsters = {"棕熊", "野鸡", "灰狼", "野兔", "狐狸", "小鹿", "小花豹", "野猪"};

    for (String wildMonster : wildMonsters) {

      executor.execute(
          new Task() {
            @Override
            public String getTaskDesc() {
              return wildMonster;
            }

            @Override
            public void run() {
              System.out.println(Thread.currentThread().getName() + ":" + wildMonster + "已经烤好");
            }
          });

      executor.waitUntilAllTasksFinished();
      executor.stop();
    }
  }

  private static class Worker implements Runnable {

    private final String name;
    private final BlockingQueue<Task> taskQueue;
    private final AtomicInteger counter = new AtomicInteger();

    private boolean isStopped;
    private Thread thread;

    private Worker(String name, BlockingQueue<Task> taskQueue) {
      this.name = name;
      this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
      thread = Thread.currentThread();
      while (!isStopped) {
        try {
          var task = taskQueue.poll(5L, TimeUnit.SECONDS);
          if (task != null) {
            task.run();
            counter.getAndIncrement();
          }
        } catch (Exception ignored) {
          //
        }
      }
    }

    public void doStop() {
      isStopped = true;
      if (thread != null) {
        thread.interrupt();
      }
    }

    public boolean isStopped() {
      return false;
    }

    public String getName() {
      return name;
    }
  }

  private static class CustomerTheadPoolExecutor {
    private final BlockingQueue<Task> taskQueue;
    private final List<Worker> workers = new LinkedList<>();

    private ThreadPoolStatus status;

    private CustomerTheadPoolExecutor(int workerNums, BlockingQueue<Task> taskQueue) {
      this.taskQueue = taskQueue;
      for (int i = 0; i < workerNums; i++) {
        workers.add(new Worker("Worker" + i, taskQueue));
      }

      for (Worker worker : workers) {
        Thread thread = new Thread(worker, worker.getName());
        thread.start();
      }
    }

    public void execute(Task task) {
      taskQueue.offer(task);
    }

    public void waitUntilAllTasksFinished() {
      while (taskQueue.size() > 0) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void shutDown() {
      status = ThreadPoolStatus.SHUTDOWN;
    }

    public void stop() {
      status = ThreadPoolStatus.STOP;
    }

    enum ThreadPoolStatus {
      RUNNING(),
      SHUTDOWN(),
      STOP(),
      TIDYING(),
      TERMINATED();

      ThreadPoolStatus() {}

      public boolean isRunning() {
        return ThreadPoolStatus.RUNNING.equals(this);
      }
    }
  }

  private interface Task extends Runnable {
    String getTaskDesc();
  }
}
