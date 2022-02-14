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

package io.github.code13.javastack.libs.easyexcel.web;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UploadDataListener.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:53 PM
 */
public class UploadDataListener implements ReadListener<UploadData> {

  private static final Logger logger = LoggerFactory.getLogger(UploadDataListener.class);

  /** 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收 */
  private static final int BATCH_COUNT = 5;

  private List<UploadData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

  /** 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。 */
  private UploadDAO uploadDAO;

  public UploadDataListener() {
    // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
    uploadDAO = new UploadDAO();
  }

  /**
   * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
   *
   * @param uploadDAO .
   */
  public UploadDataListener(UploadDAO uploadDAO) {
    this.uploadDAO = uploadDAO;
  }

  /**
   * 这个每一条数据解析都会来调用
   *
   * @param data one row value. Is is same as {@link AnalysisContext#readRowHolder()}
   * @param context .
   */
  @Override
  public void invoke(UploadData data, AnalysisContext context) {
    logger.info("解析到一条数据:{}", data.toString());
    cachedDataList.add(data);
    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
    if (cachedDataList.size() >= BATCH_COUNT) {
      saveData();
      // 存储完成清理 list
      cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    }
  }

  /**
   * 所有数据解析完成了 都会来调用
   *
   * @param context
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    logger.info("所有数据解析完成！");
  }

  /** 加上存储数据库 */
  private void saveData() {
    logger.info("{}条数据，开始存储数据库！", cachedDataList.size());
    uploadDAO.save(cachedDataList);
    logger.info("存储数据库成功！");
  }
}
