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

package io.github.code13.javastack.books.jcip.ch7_cancellation_and_shutdown;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CheckForEmail.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/14/2021 10:34 AM
 */
public class CheckForEmail {

  public boolean checkEmail(Set<String> hosts, long timeout, TimeUnit unit)
      throws InterruptedException {

    ExecutorService executor = Executors.newCachedThreadPool();
    AtomicBoolean hasNewEmail = new AtomicBoolean(false);

    try {
      for (String host : hosts) {
        executor.execute(
            () -> {
              if (checkMail(host)) {
                hasNewEmail.set(true);
              }
            });
      }
    } finally {
      executor.shutdown();
      executor.awaitTermination(timeout, unit);
    }

    return hasNewEmail.get();
  }

  private boolean checkMail(String host) {
    return false;
  }
}
