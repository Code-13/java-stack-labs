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

package io.github.code13.javase.spi;

import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

/**
 * LoggerService.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/4 10:37
 */
public class LoggerService {

  private static final LoggerService SERVICE = new LoggerService();

  private final Logger logger;

  private LoggerService() {
    ServiceLoader<Logger> loggerServiceLoader = ServiceLoader.load(Logger.class);
    List<Logger> loggerList = loggerServiceLoader.stream().map(Provider::get).toList();

    if (!loggerList.isEmpty()) {
      logger = loggerList.get(0);
    } else {
      logger = null;
    }
  }

  public static LoggerService getService() {
    return SERVICE;
  }

  public Logger getLogger() {
    return logger;
  }
}