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

package io.github.code13.libs.datafaker;

import java.nio.file.Paths;
import java.util.Locale;
import net.datafaker.Faker;
import net.datafaker.providers.base.AbstractProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CustomProvider.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/7/7 16:17
 */
class CustomProvider {

  /*
   * Custom hardcoded provider
   */

  static class Insect extends AbstractProvider<Faker> {
    private static final String[] INSECT_NAMES =
        new String[] {"Ant", "Beetle", "Butterfly", "Wasp"};

    public Insect(Faker faker) {
      super(faker);
    }

    public String nextInsectName() {
      return INSECT_NAMES[faker.random().nextInt(INSECT_NAMES.length)];
    }
  }

  static class MyCustomFaker extends Faker {
    public Insect insect() {
      return getProvider(Insect.class, Insect::new, this);
    }
  }

  @Test
  @DisplayName("testHardCodeCustomProvider")
  void testHardCodeCustomProvider() {
    MyCustomFaker faker = new MyCustomFaker();
    System.out.println(faker.insect().nextInsectName());
  }

  /*
   * Custom provider using Yaml file
   */

  static class InsectFromFile extends AbstractProvider<Faker> {
    private static final String KEY = "insectsfromfile";

    public InsectFromFile(Faker faker) {
      super(faker);
      faker.addPath(Locale.ENGLISH, Paths.get("src/test/resources/ants.yml"));
      faker.addPath(Locale.ENGLISH, Paths.get("src/test/resources/bees.yml"));
    }

    public String ant() {
      return faker.resolve(KEY + ".ants");
    }

    public String bee() {
      return faker.resolve(KEY + ".bees");
    }
  }

  static class MyCustomFakerFromFile extends Faker {
    public InsectFromFile insectFromFile() {
      return getProvider(InsectFromFile.class, InsectFromFile::new, this);
    }
  }

  @Test
  @DisplayName("insectFromFile")
  void insectFromFile() {
    MyCustomFakerFromFile myFaker = new MyCustomFakerFromFile();
    System.out.println(myFaker.insectFromFile().ant());
  }
}
