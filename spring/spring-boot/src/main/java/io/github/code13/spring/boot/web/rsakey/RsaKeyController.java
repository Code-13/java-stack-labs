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

package io.github.code13.spring.boot.web.rsakey;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RsaKeyController.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2024/1/27 09:29
 */
@RestController
public class RsaKeyController {

  RsaKeyRepository repository = new RsaKeyRepository();

  @PostMapping("/spring/boot/key")
  public Object key() {
    KeyPair keyPair = RsaUtils.generateRsaKey();

    String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

    String index = DigestUtils.md5DigestAsHex(publicKeyBase64.getBytes(StandardCharsets.UTF_8));

    repository.add(index, privateKeyBase64);

    return Map.of("index", index, "key", publicKeyBase64);
  }

  @PostMapping("/spring/boot/keys")
  public Object keys() throws Exception {
    KeyPair keyPair = RsaUtils.generateRsaKey();

    String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

    String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

    String source = "123456";

    String encoded = RsaUtils.encode(source, keyPair.getPublic());

    String sourceDecoded = RsaUtils.decode(encoded, keyPair.getPrivate());

    return Map.of(
        "publicKeyBase64",
        publicKeyBase64,
        "privateKeyBase64",
        privateKeyBase64,
        "source",
        source,
        "encoded",
        encoded,
        "sourceDecoded",
        sourceDecoded);
  }

  @PostMapping("/spring/boot/key/decrypt")
  public Object decrypt(String index, String encryptString) throws Exception {
    String privateKey = repository.get(index);
    return RsaUtils.decode(encryptString, privateKey);
  }

  static class RsaKeyRepository {
    private Map<String, String> store = new HashMap<>();

    void add(String index, String privateKey) {
      store.putIfAbsent(index, privateKey);
    }

    String get(String index) {
      return store.get(index);
    }
  }
}
