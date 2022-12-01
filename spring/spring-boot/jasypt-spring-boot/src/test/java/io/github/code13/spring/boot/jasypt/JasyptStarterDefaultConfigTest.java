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

package io.github.code13.spring.boot.jasypt;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * spring-boot-starter-jasypt 的默认配置.
 *
 * @see com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/11/27 10:47
 */
public class JasyptStarterDefaultConfigTest {

  SimpleStringPBEConfig defaultConfig() {
    // 默认值 copy 自
    // com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize(1);
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    return config;
  }

  @Test
  @DisplayName("jasyptDefault")
  void jasyptDefault() {
    SimpleStringPBEConfig config = defaultConfig();
    // 设置密码
    config.setPassword("jaspyt_password");

    // 初始化 Encryptor
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);

    // 加密
    System.out.println(encryptor.encrypt("root"));
  }
}
