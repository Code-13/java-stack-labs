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

package io.github.code13.javastack.libs.easyexcel.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * WebTestController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2/14/2022 5:56 PM
 */
@RestController
public class WebTestController {

  private final ObjectMapper objectMapper;

  public WebTestController(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /** 文件上传 */
  @PostMapping("upload")
  public String upload(@RequestParam("file") MultipartFile file) throws IOException {
    EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener())
        .sheet()
        .doRead();
    return "success";
  }

  @GetMapping("download")
  public void download(HttpServletResponse response) throws IOException {
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");

    // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
    String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

    EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet().doWrite(this::data);
  }

  @GetMapping("downloadFailedUsingJson")
  public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
    try {
      // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setCharacterEncoding("utf-8");

      // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
      String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
      response.setHeader(
          "Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

      // 测试失败打开此处代码
      /*      if (true) {
        throw new IllegalArgumentException("测试失败");
      }*/

      EasyExcel.write(response.getOutputStream(), DownloadData.class)
          // 此处设置不要自动关闭流
          .autoCloseStream(Boolean.FALSE)
          .sheet()
          .doWrite(this::data);
    } catch (Exception e) {
      // 重置response
      response.reset();
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      Map<String, String> map = MapUtils.newHashMap();
      map.put("status", "failure");
      map.put("message", "下载文件失败" + e.getMessage());
      response.getWriter().println(objectMapper.writeValueAsString(map));
    }
  }

  private List<DownloadData> data() {
    List<DownloadData> list = ListUtils.newArrayList();
    for (int i = 0; i < 10; i++) {
      DownloadData data = new DownloadData();
      data.setString("字符串" + 0);
      data.setDate(LocalDateTime.now());
      data.setDoubleData(0.56);
      list.add(data);
    }
    return list;
  }
}
