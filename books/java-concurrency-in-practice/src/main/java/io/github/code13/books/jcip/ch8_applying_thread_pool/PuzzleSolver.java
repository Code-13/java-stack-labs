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

package io.github.code13.books.jcip.ch8_applying_thread_pool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PuzzleSolver.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/16/2021 10:52 AM
 */
public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M> {

  PuzzleSolver(Puzzle<P, M> puzzle) {
    super(puzzle);
  }

  private final AtomicInteger taskCount = new AtomicInteger(0);

  @Override
  protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
    return new CountingSolverTask(p, m, n);
  }

  class CountingSolverTask extends SolverTask {
    CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
      super(pos, move, prev);
      taskCount.incrementAndGet();
    }

    @Override
    public void run() {
      try {
        super.run();
      } finally {
        if (taskCount.decrementAndGet() == 0) {
          solution.setValue(null);
        }
      }
    }
  }
}
