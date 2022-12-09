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

package io.github.code13.javase.ap.processor;

import io.github.code13.javase.ap.annotation.Factory;
import io.github.code13.javase.ap.annotation.FactoryAnnotatedClass;
import io.github.code13.javase.ap.annotation.FactoryGroupedClasses;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/6/8 16:47
 */
public class FactoryAnnotationProcessor extends AbstractProcessor {

  private Types typeUtils;
  private Elements elementUtils;
  private Filer filer;
  private Messager messager;
  private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<>();

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    typeUtils = processingEnv.getTypeUtils();
    elementUtils = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
    messager = processingEnv.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> set = new HashSet<>();
    set.add(Factory.class.getName());
    return set;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    messager.printMessage(Diagnostic.Kind.ERROR, "我执行了吗");

    // 检查被注解为@Factory的元素是否是一个类
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Factory.class)) {
      if (annotatedElement.getKind() != ElementKind.CLASS) {
        error(
            annotatedElement,
            "Only classes can be annotated with @%s",
            Factory.class.getSimpleName());
        // 退出处理
        return true;
      }

      TypeElement typeElement = (TypeElement) annotatedElement;

      try {
        FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);

        if (!isValidClass(annotatedClass)) {
          return true;
        }

        FactoryGroupedClasses factoryClass =
            factoryClasses.get(annotatedClass.getQualifiedFactoryGroupName());
        if (factoryClasses == null) {
          String qualifiedGroupName = annotatedClass.getQualifiedFactoryGroupName();
          factoryClass = new FactoryGroupedClasses(qualifiedGroupName);
          factoryClasses.put(qualifiedGroupName, factoryClass);
        }

        // 如果和其他的@Factory标注的类的id相同冲突，
        // 抛出IdAlreadyUsedException异常
        factoryClass.add(annotatedClass);
      } catch (IllegalArgumentException e) {
        error(typeElement, e.getMessage());
        return true;
      }
    }

    try {
      for (FactoryGroupedClasses factoryClass : factoryClasses.values()) {
        factoryClass.generateCode(elementUtils, filer);
      }

      factoryClasses.clear();
    } catch (IOException e) {
      error(null, e.getMessage());
    }

    return true;
  }

  private boolean isValidClass(FactoryAnnotatedClass item) {
    TypeElement classElement = item.getTypeElement();

    if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
      error(
          classElement, "The class %s is not public.", classElement.getQualifiedName().toString());
      return false;
    }

    if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
      error(
          classElement,
          "The class %s is abstract. You can't annotate abstract classes with @%",
          classElement.getQualifiedName().toString(),
          Factory.class.getSimpleName());
      return false;
    }

    TypeElement supperClassElement =
        elementUtils.getTypeElement(item.getQualifiedFactoryGroupName());

    if (supperClassElement.getKind() == ElementKind.INTERFACE) {
      if (!classElement.getInterfaces().contains(supperClassElement.asType())) {
        error(
            classElement,
            "The class %s annotated with @%s must implement the interface %s",
            classElement.getQualifiedName().toString(),
            Factory.class.getSimpleName(),
            item.getQualifiedFactoryGroupName());
        return false;
      }
    } else {
      TypeElement currentClass = classElement;
      while (true) {
        TypeMirror superclassType = currentClass.getSuperclass();

        if (superclassType.getKind() == TypeKind.NONE) {
          // 到达了基本类型(java.lang.Object), 所以退出
          error(
              classElement,
              "The class %s annotated with @%s must inherit from %s",
              classElement.getQualifiedName().toString(),
              Factory.class.getSimpleName(),
              item.getQualifiedFactoryGroupName());
          return false;
        }

        if (superclassType.toString().equals(item.getQualifiedFactoryGroupName())) {
          break;
        }

        currentClass = (TypeElement) typeUtils.asElement(superclassType);
      }
    }

    for (Element enclosedElement : classElement.getEnclosedElements()) {
      if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
        ExecutableElement constructorElement = (ExecutableElement) enclosedElement;
        if (constructorElement.getParameters().isEmpty()
            && constructorElement.getModifiers().contains(Modifier.PUBLIC)) {
          return true;
        }
      }
    }

    // 没有找到默认构造函数
    error(
        classElement,
        "The class %s must provide an public empty default constructor",
        classElement.getQualifiedName().toString());

    return false;
  }

  private void error(Element e, String msg, Object... args) {
    messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }
}
