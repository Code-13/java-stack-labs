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

package io.github.code13.javastack.libs.cglib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Method;
import net.sf.cglib.beans.BeanGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link BeanGeneratorTest} tests {@link net.sf.cglib.beans.BeanGenerator} utility of cglib. It
 * creates new beans at run time.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 21:13
 */
public class BeanGeneratorTest {

  @Test
  @DisplayName("testBeanGenerator")
  void testBeanGenerator() throws Exception {
    BeanGenerator beanGenerator = new BeanGenerator();
    beanGenerator.addProperty("value", String.class);

    Object bean = beanGenerator.create();
    Method setter = bean.getClass().getMethod("setValue", String.class);
    setter.invoke(bean, "Hello,cglib!");

    Method getter = bean.getClass().getMethod("getValue");
    Object res = getter.invoke(bean);

    assertEquals("Hello,cglib!", res);
  }
}
