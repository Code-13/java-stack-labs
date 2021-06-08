/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.javalabs.ap.annotation;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/8 16:35
 */
public class FactoryAnnotatedClass {

  private TypeElement annotatedClassElement;

  private String qualifiedSuperClassName;

  private String simpleTypeName;

  private String id;

  public FactoryAnnotatedClass(TypeElement classElement) throws IllegalArgumentException {
    annotatedClassElement = classElement;
    Factory annotation = classElement.getAnnotation(Factory.class);
    id = annotation.id();

    if (StringUtils.isEmpty(id)) {
      throw new IllegalArgumentException(
        String.format("id() in @%s for class %s is null or empty! that's not allowed",
          Factory.class.getSimpleName(), classElement.getQualifiedName().toString()));
    }

    // Get the full QualifiedTypeName
    try {
      Class<?> clazz = annotation.type();
      qualifiedSuperClassName = clazz.getCanonicalName();
      simpleTypeName = clazz.getSimpleName();
    } catch (MirroredTypeException mte) {
      DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
      TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
      qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
      simpleTypeName = classTypeElement.getSimpleName().toString();
    }
  }

  /**
   * 获取在{@link Factory#id()}中指定的id
   * return the id
   */
  public String getId() {
    return id;
  }

  /**
   * 获取在{@link Factory#type()}指定的类型合法全名
   *
   * @return qualified name
   */
  public String getQualifiedFactoryGroupName() {
    return qualifiedSuperClassName;
  }


  /**
   * 获取在 {@link Factory#type()} 中指定的类型的简单名字
   *
   * @return qualified name
   */
  public String getSimpleFactoryGroupName() {
    return simpleTypeName;
  }

  /**
   * 获取被@Factory注解的原始元素
   */
  public TypeElement getTypeElement() {
    return annotatedClassElement;
  }

}
