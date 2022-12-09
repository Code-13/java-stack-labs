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

package io.github.code13.libs.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoHeadDataListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:01 PM
 */
public class DemoHeadDataListener implements ReadListener<DemoData> {

  private static final Logger logger = LoggerFactory.getLogger(DemoHeadDataListener.class);

  /** 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收 */
  private static final int BATCH_COUNT = 5;

  private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

  @Override
  public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
    logger.info("解析到一条头数据:{}", ConverterUtils.convertToStringMap(headMap, context).toString());
    // 如果想转成成 Map<Integer,String>
    // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
    // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换
  }

  @Override
  public void invoke(DemoData data, AnalysisContext context) {
    logger.info("解析到一条数据:{}", data.toString());
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
