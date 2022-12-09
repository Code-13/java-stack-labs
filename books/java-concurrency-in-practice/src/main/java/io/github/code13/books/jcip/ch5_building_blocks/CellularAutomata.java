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

package io.github.code13.books.jcip.ch5_building_blocks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CellularAutomata.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/9/2021 9:43 AM
 */
public class CellularAutomata {

  private final Board mainBoard;
  private final CyclicBarrier barrier;
  private final Worker[] workers;

  public CellularAutomata(Board mainBoard) {
    this.mainBoard = mainBoard;
    int count = Runtime.getRuntime().availableProcessors();
    barrier = new CyclicBarrier(count, mainBoard::commitNewValues);
    workers = new Worker[count];
    for (int i = 0; i < count; i++) {
      workers[i] = new Worker(mainBoard.getSubBoard(count, i));
    }
  }

  public void start() {
    for (int i = 0; i < workers.length; i++) {
      new Thread(workers[i]).start();
    }
    mainBoard.waitForConvergence();
  }

  private class Worker implements Runnable {

    private final Board board;

    private Worker(Board board) {
      this.board = board;
    }

    @Override
    public void run() {
      while (!board.hasConverged()) {
        for (int x = 0; x < board.getMaxX(); x++) {
          for (int y = 0; y < board.getMaxY(); y++) {
            board.setNewValue(x, y, computeValue(x, y));
          }
        }
        try {
          barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
          return;
        }
      }
    }

    private int computeValue(int x, int y) {
      // Compute the new value that goes in (x,y)
      return 0;
    }
  }

  interface Board {
    int getMaxX();

    int getMaxY();

    int getValue(int x, int y);

    int setNewValue(int x, int y, int value);

    void commitNewValues();

    boolean hasConverged();

    void waitForConvergence();

    Board getSubBoard(int numPartitions, int index);
  }
}
