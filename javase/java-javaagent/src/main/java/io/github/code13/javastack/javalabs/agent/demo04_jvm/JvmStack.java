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

package io.github.code13.javastack.javalabs.agent.demo04_jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.List;

/**
 * JvmStack.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/14 09:15
 */
public class JvmStack {

  private static final long MB = 1048576L;

  static void printMemoryInfo() {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

    String info = """
        init: {1}\t max: {2}\t used: {3}\t committed: {4}\t use rate: {5}
        """
        .replace("{1}",formatUse(heapMemoryUsage.getInit()))
        .replace("{2}",formatUse(heapMemoryUsage.getMax()))
        .replace("{3}",formatUse(heapMemoryUsage.getUsed()))
        .replace("{4}",formatUse(heapMemoryUsage.getCommitted()))
        .replace("{5}",(heapMemoryUsage.getUsed() * 100 / heapMemoryUsage.getCommitted()) + "%");

    System.out.println(info);

    MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

     info = """
        init: {1}\t max: {2}\t used: {3}\t committed: {4}\t use rate: {5}
        """
        .replace("{1}",formatUse(nonHeapMemoryUsage.getInit()))
        .replace("{2}",formatUse(nonHeapMemoryUsage.getMax()))
        .replace("{3}",formatUse(nonHeapMemoryUsage.getUsed()))
        .replace("{4}",formatUse(nonHeapMemoryUsage.getCommitted()))
        .replace("{5}",(nonHeapMemoryUsage.getUsed() * 100 / nonHeapMemoryUsage.getCommitted()) + "%");

     System.out.println(info);
  }

  static void printGCInfo(){
    List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    for (GarbageCollectorMXBean bean : garbageCollectorMXBeans) {
      String info = """
          name: {1}\t count: {2}\t took: {3}\t pool name: {4}
          """
          .replace("{1}",bean.getName())
          .replace("{2}",String.valueOf(bean.getCollectionCount()))
          .replace("{3}",String.valueOf(bean.getCollectionTime()))
          .replace("{4}", Arrays.deepToString(bean.getMemoryPoolNames()));
      System.out.println(info);
    }
  }

  static String formatUse(long use){
    return (use / MB) + "MB";
  }

}
