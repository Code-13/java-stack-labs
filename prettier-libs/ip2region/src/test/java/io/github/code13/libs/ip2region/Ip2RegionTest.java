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

package io.github.code13.libs.ip2region;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * Ip2RegionTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2023/2/26 17:01
 */
class Ip2RegionTest {

  static final String DBPATH = "src/test/resources/ip2region.xdb";

  /** 完全基于文件的查询 */
  @Test
  void withFile() throws Exception {

    Searcher searcher = Searcher.newWithFileOnly(DBPATH);

    String ip = "1.2.3.4";
    long sTime = System.nanoTime();
    String region = searcher.search(ip);
    long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
    System.out.printf(
        "{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);

    searcher.close();
  }

  /**
   * 缓存 VectorIndex 索引
   *
   * <p>我们可以提前从 xdb 文件中加载出来 VectorIndex 数据，然后全局缓存，每次创建 Searcher 对象的时候使用全局的 VectorIndex 缓存可以减少一次固定的
   * IO 操作，从而加速查询，减少 IO 压力。
   */
  @Test
  void withVectorIndex() throws Exception {

    // 从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
    byte[] vIndex = Searcher.loadVectorIndexFromFile(DBPATH);

    // 用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
    Searcher searcher = Searcher.newWithVectorIndex(DBPATH, vIndex);

    // 查询
    String ip = "1.2.3.4";
    long sTime = System.nanoTime();
    String region = searcher.search(ip);
    long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
    System.out.printf(
        "{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);

    // 关闭资源
    searcher.close();
  }

  @Test
  void cacheXdb() throws Exception {
    // 从 dbPath 加载整个 xdb 到内存。
    byte[] cBuff = Searcher.loadContentFromFile(DBPATH);

    // 使用上述的 cBuff 创建一个完全基于内存的查询对象。
    Searcher searcher = Searcher.newWithBuffer(cBuff);

    // 查询
    String ip = "180.102.127.178";
    long sTime = System.nanoTime();
    String region = searcher.search(ip);
    long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
    System.out.printf(
        "{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);

    // 关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
    searcher.close();

    // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
  }
}
