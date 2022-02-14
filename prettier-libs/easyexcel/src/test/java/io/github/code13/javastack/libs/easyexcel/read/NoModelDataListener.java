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

package io.github.code13.javastack.libs.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NoModelDataListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:40 PM
 */
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

  private static final Logger logger = LoggerFactory.getLogger(NoModelDataListener.class);

  /** 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收 */
  private static final int BATCH_COUNT = 5;

  private List<Map<Integer, String>> cachedDataList =
      ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

  @Override
  public void invoke(Map<Integer, String> data, AnalysisContext context) {
    logger.info("解析到一条数据:{}", data.toString());
    cachedDataList.add(data);
    if (cachedDataList.size() >= BATCH_COUNT) {
      saveData();
      cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    saveData();
    logger.info("所有数据解析完成！");
  }

  /** 加上存储数据库 */
  private void saveData() {
    logger.info("{}条数据，开始存储数据库！", cachedDataList.size());
    logger.info("存储数据库成功！");
  }
}
