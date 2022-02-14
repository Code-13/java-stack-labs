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
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoDataListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 3:24 PM
 */
public class DemoDataListener implements ReadListener<DemoData> {

  private static final Logger logger = LoggerFactory.getLogger(DemoDataListener.class);

  /** 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收 */
  private static final int BATCH_COUNT = 5;
  /** 缓存的数据 */
  private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
  /** 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。 */
  private DemoDAO demoDAO;

  public DemoDataListener() {
    // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    demoDAO = new DemoDAO();
  }

  /**
   * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
   *
   * @param demoDAO
   */
  public DemoDataListener(DemoDAO demoDAO) {
    this.demoDAO = demoDAO;
  }

  @Override
  public void invoke(DemoData data, AnalysisContext context) {
    logger.info("解析到一条数据:{}", data.toString());
    cachedDataList.add(data);
    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
    if (cachedDataList.size() >= BATCH_COUNT) {
      saveData();
      // 存储完成清理 list
      cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    logger.info("所有数据解析完成！");
  }

  /** 加上存储数据库 */
  private void saveData() {
    logger.info("{}条数据，开始存储数据库！", cachedDataList.size());
    demoDAO.save(cachedDataList);
    logger.info("存储数据库成功！");
  }
}
