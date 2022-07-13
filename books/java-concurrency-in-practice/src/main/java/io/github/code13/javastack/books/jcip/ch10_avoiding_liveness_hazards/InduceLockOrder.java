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

package io.github.code13.javastack.books.jcip.ch10_avoiding_liveness_hazards;

/**
 * InduceLockOrder.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/16/2021 3:26 PM
 */
public class InduceLockOrder {

  private static final Object tieLock = new Object();

  public void transferMoney(Account fromAcct, Account toAcct, DollarAmount amount)
      throws InsufficientFundsException {
    class Helper {
      public void transfer() throws InsufficientFundsException {
        if (fromAcct.getBalance().compareTo(amount) < 0) {
          throw new InsufficientFundsException();
        } else {
          fromAcct.debit(amount);
          toAcct.credit(amount);
        }
      }
    }
    int fromHash = System.identityHashCode(fromAcct);
    int toHash = System.identityHashCode(toAcct);

    if (fromHash < toHash) {
      synchronized (fromAcct) {
        synchronized (toAcct) {
          new Helper().transfer();
        }
      }
    } else if (fromHash > toHash) {
      synchronized (toAcct) {
        synchronized (fromAcct) {
          new Helper().transfer();
        }
      }
    } else {
      synchronized (tieLock) {
        synchronized (fromAcct) {
          synchronized (toAcct) {
            new Helper().transfer();
          }
        }
      }
    }
  }

  interface DollarAmount extends Comparable<DollarAmount> {}

  interface Account {
    void debit(DollarAmount d);

    void credit(DollarAmount d);

    DollarAmount getBalance();

    int getAcctNo();
  }

  class InsufficientFundsException extends Exception {}
}
