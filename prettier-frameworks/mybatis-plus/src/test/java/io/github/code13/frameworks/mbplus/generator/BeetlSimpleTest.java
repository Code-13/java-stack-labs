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

package io.github.code13.frameworks.mbplus.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.junit.jupiter.api.Test;

/**
 * BeetlSimpleTest.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 11/9/2021 4:28 PM
 */
class BeetlSimpleTest {

  @Test
  void test1() throws IOException {
    Configuration cfg = Configuration.defaultConfiguration();
    GroupTemplate groupTemplate = new GroupTemplate(new ClasspathResourceLoader("/"), cfg);

    Template template = groupTemplate.getTemplate("beetl/simple.java.btl");

    template.binding(Map.of("package", "test"));

    File file = new File("src/test/generated/Simple.java");

    file.getParentFile().mkdirs();
    if (!file.exists()) {
      file.createNewFile();
    }

    try (FileOutputStream os = new FileOutputStream(file); ) {
      template.renderTo(os);
    }
  }
}
