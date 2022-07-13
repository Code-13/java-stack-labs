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

package io.github.code13.javastack.libs.javafaker;

import com.github.javafaker.Faker;
import java.util.Locale;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JavaFaker.
 *
 * <p>生成模拟仿真数据
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/28 10:05
 */
@DisplayName("JavaFaker")
public class JavaFakerRunner {

  @Data
  static class UserInfo {
    /** 真实姓名. */
    private String realName;
    /** 手机. */
    private String cellPhone;
    /** 大学. */
    private String universityName;
    /** 城市. */
    private String city;
    /** 地址. */
    private String street;
  }

  @Test
  @DisplayName("JavaFaker")
  void javaFakerTest() {
    var fakerWithCn = new Faker(Locale.CHINA);

    for (int i = 0; i < 10; i++) {
      var userInfo = new UserInfo();
      userInfo.setRealName(fakerWithCn.name().fullName());
      userInfo.setCellPhone(fakerWithCn.phoneNumber().phoneNumber());
      userInfo.setUniversityName(fakerWithCn.university().name());
      userInfo.setCity(fakerWithCn.address().city());
      userInfo.setStreet(fakerWithCn.address().streetName());

      System.out.println(userInfo);
    }
  }
}
