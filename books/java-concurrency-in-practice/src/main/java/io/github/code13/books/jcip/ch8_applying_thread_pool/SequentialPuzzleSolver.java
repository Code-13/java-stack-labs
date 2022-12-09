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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SequentialPuzzleSolver.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/16/2021 10:35 AM
 */
public class SequentialPuzzleSolver<P, M> {

  private final Puzzle<P, M> puzzle;
  private final Set<P> seen = new HashSet<>();

  public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
    this.puzzle = puzzle;
  }

  public List<M> solve() {
    P position = puzzle.initPosition();
    return search(new PuzzleNode<>(position, null, null));
  }

  private List<M> search(PuzzleNode<P, M> node) {
    if (!seen.contains(node.position)) {
      seen.add(node.position);
      if (puzzle.isGoal(node.position)) {
        return node.asMoveList();
      }
      for (M move : puzzle.legalMoves(node.position)) {
        P pos = puzzle.move(node.position, move);
        PuzzleNode<P, M> child = new PuzzleNode<>(pos, move, node);
        List<M> result = search(child);
        if (result != null) {
          return result;
        }
      }
    }
    return Collections.emptyList();
  }
}
