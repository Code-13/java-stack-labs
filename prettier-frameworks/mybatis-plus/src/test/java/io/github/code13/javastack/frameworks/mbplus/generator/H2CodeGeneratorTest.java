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

package io.github.code13.javastack.frameworks.mbplus.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * H2CodeGeneratorTest.
 *
 * <p>copy from
 * https://github.com/baomidou/generator/blob/develop/mybatis-plus-generator/src/test/java/com/baomidou/mybatisplus/generator/samples/H2CodeGeneratorTest.java
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 3:14 PM
 */
public class H2CodeGeneratorTest {

  /** 执行初始化数据库脚本 */
  @BeforeAll
  public static void before() throws SQLException {
    Connection conn = DATA_SOURCE_CONFIG.getConn();
    InputStream inputStream = H2CodeGeneratorTest.class.getResourceAsStream("/sql/init.sql");
    ScriptRunner scriptRunner = new ScriptRunner(conn);
    scriptRunner.setAutoCommit(true);
    scriptRunner.runScript(new InputStreamReader(inputStream));
    conn.close();
  }

  /** 数据源配置 */
  private static final DataSourceConfig DATA_SOURCE_CONFIG =
      new DataSourceConfig.Builder(
              "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;CASE_INSENSITIVE_IDENTIFIERS=TRUE;MODE=MYSQL",
              "sa",
              "")
          .build();

  /** 策略配置 */
  private StrategyConfig.Builder strategyConfig() {
    return new StrategyConfig.Builder().addInclude("t_simple"); // 设置需要生成的表名
  }

  /** 全局配置 */
  private GlobalConfig.Builder globalConfig() {
    return new GlobalConfig.Builder().fileOverride();
  }

  /** 包配置 */
  private PackageConfig.Builder packageConfig() {
    return new PackageConfig.Builder();
  }

  /** 模板配置 */
  private TemplateConfig.Builder templateConfig() {
    return new TemplateConfig.Builder();
  }

  /** 注入配置 */
  private InjectionConfig.Builder injectionConfig() {
    // 测试自定义输出文件之前注入操作，该操作再执行生成代码前 debug 查看
    return new InjectionConfig.Builder()
        .beforeOutputFile(
            (tableInfo, objectMap) -> {
              System.out.println(
                  "tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
            });
  }

  /** 简单生成 */
  @Test
  public void testSimple() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 过滤表前缀（后缀同理，支持多个） result: t_simple -> simple */
  @Test
  public void testTablePrefix() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().addTablePrefix("t_", "c_").build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 过滤字段后缀（前缀同理，支持多个） result: deleted_flag -> deleted */
  @Test
  public void testFieldSuffix() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().addFieldSuffix("_flag").build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /**
   * 乐观锁字段设置 result: 新增@Version注解 填充字段设置 result: 新增@TableField(value = "xxx", fill =
   * FieldFill.xxx)注解
   */
  @Test
  public void testVersionAndFill() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(
        strategyConfig()
            .entityBuilder()
            .versionColumnName("version") // 基于数据库字段
            .versionPropertyName("version") // 基于模型属性
            .addTableFills(new Column("create_time", FieldFill.INSERT)) // 基于数据库字段填充
            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE)) // 基于模型属性填充
            .build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 逻辑删除字段设置 result: 新增@TableLogic注解 忽略字段设置 result: 不生成 */
  @Test
  public void testLogicDeleteAndIgnoreColumn() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(
        strategyConfig()
            .entityBuilder()
            .logicDeleteColumnName("deleted") // 基于数据库字段
            .logicDeletePropertyName("deleteFlag") // 基于模型属性
            .addIgnoreColumns("age") // 基于数据库字段
            .build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 自定义模板生成的文件名称 result: TSimple -> TSimpleEntity */
  @Test
  public void testCustomTemplateName() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(
        strategyConfig()
            .entityBuilder()
            .formatFileName("%sEntity")
            .mapperBuilder()
            .formatMapperFileName("%sDao")
            .formatXmlFileName("%sXml")
            .controllerBuilder()
            .formatFileName("%sAction")
            .serviceBuilder()
            .formatServiceFileName("%sService")
            .formatServiceImplFileName("%sServiceImp")
            .build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /**
   * 自定义模板生成的文件路径
   *
   * @see OutputFile
   */
  @Test
  public void testCustomTemplatePath() {
    // 设置自定义路径
    Map<OutputFile, String> pathInfo = new HashMap<>();
    pathInfo.put(OutputFile.xml, "D://");
    pathInfo.put(OutputFile.entity, "D://entity//");
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().build());
    generator.packageInfo(packageConfig().pathInfo(pathInfo).build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 自定义模板 */
  @Test
  public void testCustomTemplate() {
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().build());
    generator.template(templateConfig().entity("/templates/entity1.java").build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 自定义注入属性 */
  @Test
  public void testCustomMap() {
    // 设置自定义属性
    Map<String, Object> map = new HashMap<>();
    map.put("abc", 123);
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().build());
    generator.template(templateConfig().entity("/templates/entity1.java").build());
    generator.injection(injectionConfig().customMap(map).build());
    generator.global(globalConfig().build());
    generator.execute();
  }

  /** 自定义文件 key为文件名称，value为文件路径 输出目录为 other */
  @Test
  public void testCustomFile() {
    // 设置自定义输出文件
    Map<String, String> customFile = new HashMap<>();
    customFile.put("test.txt", "/templates/test.vm");
    AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
    generator.strategy(strategyConfig().build());
    generator.injection(injectionConfig().customFile(customFile).build());
    generator.global(globalConfig().build());
    generator.execute();
  }
}
