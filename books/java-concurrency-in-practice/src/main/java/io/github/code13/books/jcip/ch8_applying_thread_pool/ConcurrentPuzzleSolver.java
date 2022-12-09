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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ConcurrentPuzzleSolver.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/16/2021 10:46 AM
 */
public class ConcurrentPuzzleSolver<P, M> {

  private final Puzzle<P, M> puzzle;
  private final ExecutorService exec;
  private final ConcurrentMap<P, Boolean> seen;
  protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

  public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
    this.puzzle = puzzle;
    exec = initThreadPool();
    seen = new ConcurrentHashMap<P, Boolean>();
    if (exec instanceof ThreadPoolExecutor) {
      ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
      tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    }
  }

  private ExecutorService initThreadPool() {
    return Executors.newCachedThreadPool();
  }

  public List<M> solve() throws InterruptedException {
    try {
      P p = puzzle.initPosition();
      exec.execute(newTask(p, null, null));
      // block until solution found
      PuzzleNode<P, M> solnPuzzleNode = solution.getValue();
      return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
    } finally {
      exec.shutdown();
    }
  }

  protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
    return new SolverTask(p, m, n);
  }

  protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
    SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
      super(pos, move, prev);
    }

    @Override
    public void run() {
      if (solution.isSet() || seen.putIfAbsent(position, true) != null) {
        return; // already solved or seen this position
      }
      if (puzzle.isGoal(position)) {
        solution.setValue(this);
      } else {
        for (M m : puzzle.legalMoves(position)) {
          exec.execute(newTask(puzzle.move(position, m), m, this));
        }
      }
    }
  }
}
