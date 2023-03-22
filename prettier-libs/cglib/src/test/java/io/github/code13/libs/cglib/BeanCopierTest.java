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

import net.sf.cglib.beans.BeanCopier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code BeanCopierTest} tests {@code net.sf.cglib.beans.BeanCopier} utility of cglib. It copies
 * beans by property values.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2022/5/25 21:08
 */
public class BeanCopierTest {

  @Test
  @DisplayName("testBeanCopier")
  void testBeanCopier() {
    BeanCopier beanCopier = BeanCopier.create(SampleBean.class, OtherSampleBean.class, false);

    SampleBean sampleBean = new SampleBean();
    sampleBean.setValue("Hello,cglib!");
    OtherSampleBean otherSampleBean = new OtherSampleBean();
    beanCopier.copy(sampleBean, otherSampleBean, null);

    assertEquals("Hello,cglib!", otherSampleBean.getValue());
  }
}
