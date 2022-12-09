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

package io.github.code13.spring.framework.core.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.StandardClassMetadata;

/**
 * ClassMetadata_Runner.
 *
 * @see org.springframework.core.type.ClassMetadata
 * @see org.springframework.core.type.StandardClassMetadata
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/3 20:49
 */
public class ClassMetadata_Runner {

  /*
  public interface ClassMetadata {
    String getClassName();
    boolean isInterface();
    boolean isAnnotation();
    boolean isAbstract();
    // 是否一个具体的类，即不是接口或者抽象类，换句话说，可 new
    boolean isConcrete();
    boolean isFinal();
    // 是否“独立”，TopLevelClass 或者 NestedClass
    boolean isIndependent();
    // 是否含有 InnerClass | NestedClass | LocalClass
    boolean hasEnclosingClass();
    @Nullable
    String getEnclosingClassName();
    boolean hasSuperClass();
    @Nullable
    String getSuperClassName();
    String[] getInterfaceNames();
    // 返回所有（继承、实现）该类的 成员类（内部类、接口除外）
    String[] getMemberClassNames();
    }
   */

  @Test
  @DisplayName("classMetadata")
  void classMetadata() {
    StandardClassMetadata classMetadata = new StandardClassMetadata(TopLevelClass.class);
    assertEquals(classMetadata.getClassName(), TopLevelClass.class.getName());
    assertFalse(classMetadata.isInterface());
    assertFalse(classMetadata.isAbstract());
    assertTrue(classMetadata.isConcrete());
    assertFalse(classMetadata.isFinal());
    assertTrue(classMetadata.isIndependent());
    assertFalse(classMetadata.hasEnclosingClass());
    assertTrue(classMetadata.hasSuperClass());
    System.out.println(classMetadata.getSuperClassName());
    System.out.println(Arrays.toString(classMetadata.getMemberClassNames()));
  }
}
