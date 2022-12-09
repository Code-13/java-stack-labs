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

package io.github.code13.javase.concurrent.locks;

import java.util.concurrent.locks.StampedLock;

/**
 * Point.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/26 09:30
 */
class StampedLockRunner {

  /*
   * StampedLock的状态由版本和模式组成。
   * 锁获取方法返回一个戳，该戳表示并控制对锁状态的访问。
   *
   * StampedLock提供了3种模式控制访问锁：
   */

  /*
  *  写模式：
     获取写锁，它是独占的，当锁处于写模式时，无法获得读锁，所有乐观读验证都将失败。

     writeLock()： 阻塞等待独占获取锁，返回一个戳， 如果是0表示获取失败。
     tryWriteLock()：尝试获取一个写锁，返回一个戳， 如果是0表示获取失败。
     long tryWriteLock(long time, TimeUnit unit): 尝试获取一个独占写锁，可以等待一段事件，返回一个戳， 如果是0表示获取失败。
     long writeLockInterruptibly(): 试获取一个独占写锁，可以被中断，返回一个戳， 如果是0表示获取失败。
     unlockWrite(long stamp)：释放独占写锁，传入之前获取的戳。
     tryUnlockWrite()：如果持有写锁，则释放该锁，而不需要戳值。这种方法可能对错误后的恢复很有用。
  */

  /*
  *  读模式
     悲观的方式后去非独占读锁。

     readLock()： 阻塞等待获取非独占的读锁，返回一个戳， 如果是0表示获取失败。
     tryReadLock()：尝试获取一个读锁，返回一个戳， 如果是0表示获取失败。
     long tryReadLock(long time, TimeUnit unit): 尝试获取一个读锁，可以等待一段事件，返回一个戳， 如果是0表示获取失败。
     long readLockInterruptibly(): 阻塞等待获取非独占的读锁，可以被中断，返回一个戳， 如果是0表示获取失败。
     unlockRead(long stamp)：释放非独占的读锁，传入之前获取的戳。
     tryUnlockRead()：如果读锁被持有，则释放一次持有，而不需要戳值。这种方法可能对错误后的恢复很有用。
  */

  /*
  * 乐观读模式

     乐观读也就是若读的操作很多，写的操作很少的情况下，你可以乐观地认为，写入与读取同时发生几率很少，因此不悲观地使用完全的读取锁定，程序可以查看读取资料之后，是否遭到写入执行的变更，再采取后续的措施(重新读取变更信息，或者抛出异常) ，这一个小小改进，可大幅度提高程序的吞吐量。
     StampedLock 支持 tryOptimisticRead() 方法，读取完毕后做一次戳校验，如果校验通过，表示这期间没有其他线程的写操作，数据可以安全使用，如果校验没通过，需要重新获取读锁，保证数据一致性。

     tryOptimisticRead(): 返回稍后可以验证的戳记，如果独占锁定则返回零。
     boolean validate(long stamp): 如果自给定戳记发行以来锁还没有被独占获取，则返回true。
  */

  /*
  * 此外，StampedLock 提供了api实现上面3种方式进行转换：

   long tryConvertToWriteLock(long stamp)
   如果锁状态与给定的戳记匹配，则执行以下操作之一。如果戳记表示持有写锁，则返回它。或者，如果是读锁，如果写锁可用，则释放读锁并返回写戳记。或者，如果是乐观读，则仅在立即可用时返回写戳记。该方法在所有其他情况下返回零

   long tryConvertToReadLock(long stamp)
   如果锁状态与给定的戳记匹配，则执行以下操作之一。如果戳记表示持有写锁，则释放它并获得读锁。或者，如果是读锁，返回它。或者，如果是乐观读，则仅在立即可用时才获得读锁并返回读戳记。该方法在所有其他情况下返回零。

   long tryConvertToOptimisticRead(long stamp)
   如果锁状态与给定的戳记匹配，那么如果戳记表示持有锁，则释放它并返回一个观察戳记。或者，如果是乐观读，则在验证后返回它。该方法在所有其他情况下返回0，因此作为“tryUnlock”的形式可能很有用。
  */

  static class Point {
    private double x;
    private double y;
    StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) throws InterruptedException {
      // 涉及对共享资源的修改，使用写锁-独占操作
      long stamp = sl.writeLock();
      Thread.sleep(500);
      try {
        x += deltaX;
        y += deltaY;
      } finally {
        sl.unlockWrite(stamp);
      }
    }

    /**
     * 使用乐观读锁访问共享资源 注意：乐观读锁在保证数据一致性上需要拷贝一份要操作的变量到方法栈，并且在操作数据时候可能其他写线程已经修改了数据，
     * 而我们操作的是方法栈里面的数据，也就是一个快照，所以最多返回的不是最新的数据，但是一致性还是得到保障的。
     */
    double distanceFromOrigin() throws InterruptedException {
      long stamp = sl.tryOptimisticRead(); // 使用乐观读锁
      Thread.sleep(500);
      double currentX = x;
      double currentY = y; // 拷贝共享资源到本地方法栈中
      if (!sl.validate(stamp)) { // 如果有写锁被占用，可能造成数据不一致，所以要切换到普通读锁模式
        stamp = sl.readLock();
        try {
          currentX = x;
          currentY = y;
        } finally {
          sl.unlockRead(stamp);
        }
      }
      return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) {
      long stamp = sl.readLock(); // Could instead start with optimistic, not read mode
      try {
        while (x == 0.0 && y == 0.0) { // 循环，检查当前状态是否符合
          long ws = sl.tryConvertToWriteLock(stamp); // 将读锁转为写锁
          if (ws != 0L) { // 这是确认转为写锁是否成功
            stamp = ws; // 如果成功 替换票据
            x = newX;
            y = newY;
            break;
          } else { // 如果不能成功转换为写锁
            sl.unlockRead(stamp); // 我们显式释放读锁
            stamp = sl.writeLock(); // 显式直接进行写锁 然后再通过循环再试
          }
        }

      } finally {
        sl.unlock(stamp); // 释放读锁或写锁
      }
    }
  }
}
