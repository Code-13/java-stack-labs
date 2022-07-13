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

package io.github.code13.javastack.books.jcip.ch15_atomic_variables_and_nonblocking_synchronization;

import java.util.concurrent.atomic.AtomicReference;

/**
 * LinkedQueue.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/22/2021 2:52 PM
 */
public class LinkedQueue<E> {

  private final Node<E> dummy = new Node<>(null, null);
  private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);
  private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

  public boolean put(E item) {
    Node<E> newNode = new Node<>(item, null);
    while (true) {
      Node<E> curTail = tail.get();
      Node<E> tailNext = curTail.next.get();
      if (curTail == tail.get()) {
        if (tailNext != null) {
          // 队列处于中间状态，推进尾节点
          tail.compareAndSet(curTail, tailNext);
        } else {
          // 队列处于稳定状态，尝试插入新节点
          if (curTail.next.compareAndSet(null, newNode)) {
            // 插入操作成功，尝试推进尾节点
            tail.compareAndSet(curTail, newNode);
            return true;
          }
        }
      }
    }
  }

  private static class Node<E> {
    final E item;
    final AtomicReference<Node<E>> next;

    private Node(E item, Node<E> next) {
      this.item = item;
      this.next = new AtomicReference<>(next);
    }
  }
}
