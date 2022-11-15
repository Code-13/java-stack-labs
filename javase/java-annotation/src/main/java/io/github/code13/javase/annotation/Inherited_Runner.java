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

package io.github.code13.javase.annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Example for {@link java.lang.annotation.Inherited}.
 *
 * @see java.lang.annotation.Inherited
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/15 10:52
 */
public class Inherited_Runner {

  /*
   * Indicates that an annotation type is automatically inherited. If an Inherited meta-annotation
   * is present on an annotation type declaration, and the user queries the annotation type on a
   * class declaration, and the class declaration has no annotation for this type, then the class's
   * superclass will automatically be queried for the annotation type. This process will be repeated
   * until an annotation for this type is found, or the top of the class hierarchy (Object) is
   * reached. If no superclass has an annotation for this type, then the query will indicate that
   * the class in question has no such annotation.
   *
   * <p>Note that this meta-annotation type has no effect if the annotated type is used to annotate
   * anything other than a class. Note also that this meta-annotation only causes annotations to be
   * inherited from superclasses; annotations on implemented interfaces have no effect.
   */

  /*
   * 1、指示注释类型被自动继承。
   * 2、如果在注释类型声明中存在 Inherited 元注释，并且用户在某一类声明中查询该注释类型，同时该类声明中没有此类型的注释，则将在该类的超类中自动查询该注释类型。
   * 3、此过程会重复进行，直到找到此类型的注释或到达了该类层次结构的顶层 (Object) 为止。如果没有超类具有该类型的注释，则查询将指示当前类没有这样的注释。
   *
   * 注意，4、如果使用注释类型注释类以外的任何事物，此元注释类型都是无效的。
   * 5、还要注意，此元注释仅促成从超类继承注释；对已实现接口的注释无效。
   */

  /*
   * 我们来看一下， 第二句话怎么理解、第一句话最后理解：我们知道@interface可以声明一个注释类型(也可以叫注解类型，英文：annotation
   * type)，就像Class可以声明一个类类型，interface可以声明一个接口类型一样。
   * 所以这句话的意思是，在我们声明一个注释类型时，用到了@Inherited元注解，
   * 例如：我们声明了一个@InheritedTest注解类型；而我们又在某一个类中使用到了我们自定义声明的这个注解类型@InheritedTest，
   * 例如：这个类为Parent；而还有一个类继承了这个Parent类，例如这个子类为Child；而我们在程序中使用Child类时，程序会查此类声明中注解类型@InheritedTest，如果此类中没有，则会在该类的超类中自动查询注解类型@InheritedTest。
   *
   * 而第三句话的意思：这种自动向超类查询的过程会重复进行，就是说如果超类没有找到注解类型@InheritedTest，就获取超类的超类中去找，一直到顶层 (Object)
   * 为止。如果还是没有找到就说嘛此类没有这样的注解类型。
   *
   * 第四句话的意思：这句话很重要，说明我们声明了一个用到了@Inherited元注解的注解类型@InheritedTest只对类是有效的，对其他是无效的，如果我们用我们声明的这个注解类型@InheritedTest去注释一个方法或者字段等，即使子类继承了这个方法和字段，而子类中继承的方法和字段是无法继承父类方法和字段的@InheritedTest注释的。所以@InheritedTest去注释方法和字段是多余的。
   *
   * 第五句话的意思：就是说注解类型只能超类中被继承，即使你有一个接口，这个接口被注释类型@InheritedTest注释了，然后有一个类实现了这个接口。此时这个类不无法继承这个。@InheritedTest注释类型的
   *
   * 所以第一句话，指示注释类型被自动继承就好理解了，表示子类会继承超类中被@Inherited元注解注释的注释类型@InheritedTest
   */

  /** 自定义注解`InheritedTest`使用了`@Inherited`元注解，表示此自定义注解**用在类上时，会被子类所继承** */
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @interface InheritedTest {
    String value() default
        "this is default value for custom annotation InheritedTest that can use Annotation @Inherited.";
  }

  /** 自定义注解`UnInheritedTest`没有使用`@Inherited`元注解，表示此自定义注解**用在类上时，不会被子类所继承** */
  @Retention(RetentionPolicy.RUNTIME)
  @interface UnInheritedTest {
    String value() default
        "this is default value for custom annotation UnInheritedTest that can not use Annotation @Inherited.";
  }

  @InheritedTest("使用@Inherited的注解Parent类")
  @UnInheritedTest("未使用@Inherited的注解Parent类")
  static class Car {}

  static class Bmw extends Car {}

  @Test
  @DisplayName("TestOnClass")
  void testOnClass() {
    Class<Bmw> childClass = Bmw.class;
    assertTrue(childClass.isAnnotationPresent(InheritedTest.class));
    System.out.println(childClass.getAnnotation(InheritedTest.class).value());
    assertFalse(childClass.isAnnotationPresent(UnInheritedTest.class));
  }

  @InheritedTest("使用@Inherited的注解的IParent接口类型")
  interface ICar {}

  /** 子类不再集成父类，而是实现接口 */
  static class Benz implements ICar {}

  @Test
  @DisplayName("TestOnInterface")
  void testOnInterface() {
    Class<Benz> benzClass = Benz.class;
    assertFalse(benzClass.isAnnotationPresent(InheritedTest.class));
  }
}
