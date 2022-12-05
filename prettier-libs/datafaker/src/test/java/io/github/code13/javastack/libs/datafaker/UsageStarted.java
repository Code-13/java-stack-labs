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

package io.github.code13.javastack.libs.datafaker;

import java.util.List;
import java.util.Locale;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * UsageStarted.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 15:19
 */
class UsageStarted {

  @Test
  @DisplayName("defaultUsage")
  void defaultUsage() {
    Faker faker = new Faker();

    String fullName = faker.name().fullName();
    System.out.println(fullName);
    String firstName = faker.name().firstName();
    System.out.println(firstName);
    String lastName = faker.name().lastName();
    System.out.println(lastName);

    String address = faker.address().streetAddress();
    System.out.println(address);
  }

  @Test
  @DisplayName("different locale")
  void differentLocale() {
    Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
    System.out.println(faker.name().fullName());
  }

  @Test
  @DisplayName("Multiple locales")
  void multipleLocales() {
    Faker faker1 = new Faker(new Locale("nl"));
    Faker faker2 = new Faker(new Locale("ar"));

    List<Faker> fakers = List.of(faker1, faker2);

    for (int i = 0; i < 10; i++) {
      Faker randomFaker = new Faker().options().nextElement(fakers);
      System.out.println(randomFaker.address().fullAddress());
    }
  }
}
