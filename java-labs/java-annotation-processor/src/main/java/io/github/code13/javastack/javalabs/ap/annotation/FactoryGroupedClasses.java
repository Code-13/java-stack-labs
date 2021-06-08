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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/8 16:42
 */
public class FactoryGroupedClasses {

  /**
   * 将被添加到生成的工厂类的名字中
   */
  private static final String SUFFIX = "Factory";

  private String qualifiedClassName;

  private Map<String, FactoryAnnotatedClass> itemsMap = new LinkedHashMap<>();

  public FactoryGroupedClasses(String qualifiedClassName) {
    this.qualifiedClassName = qualifiedClassName;
  }

  public void add(FactoryAnnotatedClass toInsert) {
    FactoryAnnotatedClass factoryAnnotatedClass = itemsMap.get(toInsert.getId());

    if (Objects.nonNull(factoryAnnotatedClass)) {
      throw new IllegalArgumentException("已经被使用了");
    }

    itemsMap.put(toInsert.getId(), toInsert);

  }

  public void generateCode(Elements elements, Filer filer) throws IOException {
    TypeElement supperClassName = elements.getTypeElement(qualifiedClassName);
    String factoryClassName = supperClassName.getSimpleName() + SUFFIX;

    //final JavaFileObject jfo = filer.createSourceFile(this.qualifiedClassName + SUFFIX);

    //final Writer writer = jfo.openWriter();

    MethodSpec.Builder builder = MethodSpec.methodBuilder("create")
      .addModifiers(Modifier.PUBLIC)
      .addParameter(String.class, "id")
      .returns(supperClassName.getClass());

    for (FactoryAnnotatedClass item : itemsMap.values()) {
      builder.addStatement("if (\"%s\".equals(id))", item.getId());
      builder.addStatement("return new %s()", item.getTypeElement().getQualifiedName().toString());
    }

    TypeSpec typeSpec = TypeSpec.classBuilder(factoryClassName)
      .addModifiers(Modifier.PUBLIC)
      .addMethod(builder.build())
      .build();

    JavaFile javaFile = JavaFile.builder("com.github.code13.ap.factory", typeSpec)
      .build();

    javaFile.writeTo(filer);
  }

}
