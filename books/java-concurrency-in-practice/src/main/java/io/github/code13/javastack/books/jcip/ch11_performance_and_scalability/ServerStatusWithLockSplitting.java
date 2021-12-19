/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch11_performance_and_scalability;

import io.github.code13.javastack.books.jcip.GuardedBy;
import java.util.Set;

/**
 * ServerStatus.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/12/18 23:58
 */
public class ServerStatusWithLockSplitting {
  @GuardedBy("users")
  public final Set<String> users;

  @GuardedBy("queries")
  public final Set<String> queries;

  public ServerStatusWithLockSplitting(Set<String> users, Set<String> queries) {
    this.users = users;
    this.queries = queries;
  }

  public void addUser(String u) {
    synchronized (users) {
      users.add(u);
    }
  }

  public synchronized void addQuery(String q) {
    synchronized (queries) {
      queries.add(q);
    }
  }

  public synchronized void removeUser(String u) {
    synchronized (users) {
      users.remove(u);
    }
  }

  public synchronized void removeQuery(String q) {
    synchronized (queries) {
      queries.remove(q);
    }
  }
}
