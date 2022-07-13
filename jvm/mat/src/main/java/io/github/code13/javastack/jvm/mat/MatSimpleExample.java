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

package io.github.code13.javastack.jvm.mat;

import java.util.ArrayList;

/**
 * MatSimpleExample.
 *
 * <p>简单示例.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/8/20 11:43
 */
public class MatSimpleExample {

  /*
   * 次数运行设置一些参数：
   * -Xms2m -Xmx2m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\dump\dump2.hprof
   *
   * -Xms2m -Xmx2m：堆最小内存为2M，最大内存为2M。这里没有显示设置新生代大小，它会自动分配新生代大小，分配完剩下的，就是老年代大小了。
   * -XX:+HeapDumpOnOutOfMemoryError：指发生内存溢出的时候，会自动生成一个二进制的堆快照文件，这个快照文件以.hprof后缀结尾。
   */

  public static void main(String[] args) {

    var users = new ArrayList<User>();

    while (true) {
      users.add(new User());
    }
  }
}

class User {
  private String name = "user";
}
