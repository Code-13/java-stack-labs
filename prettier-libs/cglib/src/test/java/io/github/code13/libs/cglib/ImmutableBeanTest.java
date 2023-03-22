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

package io.github.code13.libs.cglib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.sf.cglib.beans.ImmutableBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ImmutableBeanTest} tests {@link net.sf.cglib.beans.ImmutableBean} utility of cglib. It
 * creates immutable beans.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 21:18
 */
public class ImmutableBeanTest {

  @Test
  @DisplayName("testImmutableBean")
  void testImmutableBean() {
    SampleBean sampleBean = new SampleBean();
    sampleBean.setValue("Hello,World!");

    SampleBean immutableBean = (SampleBean) ImmutableBean.create(sampleBean);
    assertEquals("Hello,World!", immutableBean.getValue());

    sampleBean.setValue("Hello,cglib!");
    assertEquals("Hello,cglib!", immutableBean.getValue());

    assertThrows(IllegalStateException.class, () -> immutableBean.setValue("Hello, cglib! again"));
  }
}
