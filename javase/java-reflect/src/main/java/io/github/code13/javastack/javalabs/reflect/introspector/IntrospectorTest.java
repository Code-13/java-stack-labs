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

package io.github.code13.javastack.javalabs.reflect.introspector;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * IntrospectorTest.
 *
 * <p>内省测试.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/8/15 14:20
 */
@DisplayName("java 内省.")
class IntrospectorTest {

  /*
   * 内省（Introspection）在心理学中，它是心理学基本研究方法之一。
   * 内省法又称自我观察法。它是发生在内部的，我们自己能够意识到的主观现象。也可以说是对于自己的主观经验及其变化的观察。
   * 正因为它的主观性，内省法自古以来就成为心理学界长期的争论。争论于它是否客观，是否可靠。
   * 另外内省也可看作自我反省，也是儒家强调的自我思考。
   * 从这个角度说它可以应用于计算机领域，例如Java内省机制和cocoa内省机制。
   */

  /*
   * Java语言内省（Introspector）是Java语言对Bean类属性、事件的一种缺省处理方法。
   * 例如类A中有属性name,那我们可以通过getName,setName来得到其值或者设置新的值。
   * 通过getName/setName来访问name属性，这就是默认的规则。
   * Java中提供了一套API用来访问某个属性的getter/setter方法，通过这些API可以使你不需要了解这个规则（但你最好还是要搞清楚），这些API存放于包java.beans中。
   */

  @Test
  @DisplayName("使用内省替代直接使用反射可以防止破坏类的封装")
  void test1()
      throws NoSuchFieldException, IllegalAccessException, IntrospectionException,
          InvocationTargetException {

    var person = new Person();
    changeObjectFieldByReflection(person, "age", 20);

    System.out.println("反射修改属性破坏类的封装，使其内部状态错误：");
    System.out.println(person);

    changeObjectFieldByIntrospector(person, "age", 18);
    System.out.println("内省修改属性未破坏类的封装：");
    System.out.println(person);
  }

  private static void changeObjectFieldByReflection(Object o, String fieldName, Object value)
      throws IllegalAccessException, NoSuchFieldException {
    var declaredField = o.getClass().getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    declaredField.set(o, value);
  }

  private static void changeObjectFieldByIntrospector(Object o, String fieldName, Object value)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {

    var propertyDescriptor = new PropertyDescriptor(fieldName, o.getClass());
    propertyDescriptor.getWriteMethod().invoke(o, value);
  }

  static class Person {
    /** 18岁成年. */
    private static final int ADULT_AGE = 18;

    /** 年龄. */
    private int age;
    /** 是否成年. */
    private boolean adult;

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
      adult = age >= ADULT_AGE;
    }

    public boolean isAdult() {
      return adult;
    }

    @Override
    public String toString() {
      return MessageFormat.format("age：{0}，adult：{1}", age, adult);
    }
  }

  /** 使用内省编写属性拷贝. */
  private static class BeanUtils {

    private static void copyProperties(Object dest, Object orig)
        throws IntrospectionException, InvocationTargetException, IllegalAccessException {
      var beanInfo = Introspector.getBeanInfo(orig.getClass());
      var propertyDescriptors = beanInfo.getPropertyDescriptors();
      for (PropertyDescriptor pd : propertyDescriptors) {
        var wm = pd.getWriteMethod();
        var rm = pd.getReadMethod();
        if (rm != null && wm != null) {
          var r = rm.invoke(orig);
          wm.invoke(dest, r);
        }
      }
    }
  }
}
