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

package io.github.code13.javastack.books.jcip.ch6_task_execution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * TimeBudget.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/13/2021 2:44 PM
 */
public class TimeBudget {

  private static ExecutorService executor = Executors.newCachedThreadPool();

  public List<TravelQuote> getRankedTravelQuotes(
      TravelInfo travelInfo,
      Set<TravelCompany> companies,
      Comparator<TravelQuote> ranking,
      long time,
      TimeUnit unit)
      throws InterruptedException {
    List<QuoteTask> tasks = new ArrayList<>();
    for (TravelCompany company : companies) {
      tasks.add(new QuoteTask(company, travelInfo));
    }

    List<Future<TravelQuote>> futures = executor.invokeAll(tasks, time, unit);

    List<TravelQuote> quotes = new ArrayList<>(tasks.size());
    for (Future<TravelQuote> future : futures) {
      for (QuoteTask task : tasks) {
        try {
          quotes.add(future.get());
        } catch (ExecutionException e) {
          quotes.add(task.getFailureQuote(e.getCause()));
        } catch (CancellationException e) {
          quotes.add(task.getTimeoutQuote(e));
        }
      }
    }

    quotes.sort(ranking);

    return quotes;
  }

  static class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
      this.company = company;
      this.travelInfo = travelInfo;
    }

    TravelQuote getFailureQuote(Throwable t) {
      return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
      return null;
    }

    @Override
    public TravelQuote call() throws Exception {
      return company.solicitQuote(travelInfo);
    }
  }
}

interface TravelCompany {
  TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

interface TravelQuote {}

interface TravelInfo {}
