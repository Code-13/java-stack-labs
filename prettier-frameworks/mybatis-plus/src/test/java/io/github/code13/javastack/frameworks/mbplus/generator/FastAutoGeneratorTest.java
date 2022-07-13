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

package io.github.code13.javastack.frameworks.mbplus.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 * FastAutoGeneratorTest.
 *
 * <p>copy from
 * https://github.com/baomidou/generator/blob/develop/mybatis-plus-generator/src/test/java/com/baomidou/mybatisplus/generator/samples/FastAutoGeneratorTest.java
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 3:12 PM
 */
public class FastAutoGeneratorTest {

  /** 执行初始化数据库脚本 */
  public static void before() throws SQLException {
    Connection conn = DATA_SOURCE_CONFIG.build().getConn();
    InputStream inputStream = H2CodeGeneratorTest.class.getResourceAsStream("/sql/init.sql");
    ScriptRunner scriptRunner = new ScriptRunner(conn);
    scriptRunner.setAutoCommit(true);
    scriptRunner.runScript(new InputStreamReader(inputStream));
    conn.close();
  }

  /** 数据源配置 */
  private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG =
      new DataSourceConfig.Builder(
          "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;MODE=MYSQL",
          "sa",
          "");

  /** 执行 run */
  public static void main(String[] args) throws SQLException {
    //    before();
    //    FastAutoGenerator.create(DATA_SOURCE_CONFIG)
    //        // 全局配置
    //        .globalConfig(
    //            (scanner, builder) -> builder.author(scanner.apply("请输入作者名称？")).fileOverride())
    //        // 包配置
    //        .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
    //        // 策略配置
    //        .strategyConfig(builder -> builder.addInclude("t_simple"))
    //        /*
    //           模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
    //          .templateEngine(new BeetlTemplateEngine())
    //          .templateEngine(new FreemarkerTemplateEngine())
    //        */
    //        .execute();

    before();
    FastAutoGenerator.create(DATA_SOURCE_CONFIG)
        .globalConfig(
            builder -> {
              builder
                  .author("code13") // 设置作者
                  .enableSwagger() // 开启 swagger 模式
                  .fileOverride() // 覆盖已生成文件
                  .outputDir("prettier-frameworks/mybatis-plus/src/generated"); // 指定输出目录
            })
        .packageConfig(
            builder -> {
              builder
                  .parent("com.baomidou.mybatisplus.samples.generator") // 设置父包名
                  .moduleName("system") // 设置父包模块名
                  .pathInfo(
                      Collections.singletonMap(
                          OutputFile.xml,
                          "prettier-frameworks/mybatis-plus/src/generated")); // 设置mapperXml生成路径
            })
        .strategyConfig(
            builder -> {
              builder
                  .addInclude("t_simple") // 设置需要生成的表名
                  .addTablePrefix("t_", "c_"); // 设置过滤表前缀
            })
        .templateConfig(builder -> builder.disable(TemplateType.SERVICE, TemplateType.SERVICEIMPL))
        .templateEngine(new BeetlTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        .execute();
  }
}
