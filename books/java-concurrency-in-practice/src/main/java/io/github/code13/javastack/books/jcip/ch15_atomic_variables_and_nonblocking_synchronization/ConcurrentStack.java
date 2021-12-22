/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch15_atomic_variables_and_nonblocking_synchronization;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ConcurrentStack.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/22/2021 2:09 PM
 */
public class ConcurrentStack<E> {

  private final AtomicReference<Node<E>> top = new AtomicReference<>();

  public void push(E item) {
    Node<E> newNode = new Node<>(item);
    Node<E> oldNode;
    do {
      oldNode = top.get();
      newNode.next = oldNode;
    } while (!top.compareAndSet(oldNode, newNode));
  }

  public E pop() {

    Node<E> oldHead;
    Node<E> newHead;

    do {
      oldHead = top.get();
      if (oldHead == null) {
        return null;
      }
      newHead = oldHead.next;
    } while (!top.compareAndSet(oldHead, newHead));

    return oldHead.item;
  }

  private static class Node<E> {
    private final E item;
    private Node<E> next;

    private Node(E item) {
      this.item = item;
    }
  }
}
