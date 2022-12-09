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

package io.github.code13.libs.jackson2.contextual;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;

/**
 * SensitiveInfoSerialize.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/22 16:00
 */
class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

  private final SensitiveType type;

  public SensitiveInfoSerialize() {
    this(null);
  }

  SensitiveInfoSerialize(SensitiveType type) {
    this.type = type;
  }

  @Override
  public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializers)
      throws IOException {

    if (type == null) {
      jsonGenerator.writeString(s);
      return;
    }

    switch (type) {
      case CHINESE_NAME -> {
        jsonGenerator.writeString(SensitiveInfoUtils.chineseName(s));
      }
      case ID_CARD -> {
        jsonGenerator.writeString(SensitiveInfoUtils.idCardNum(s));
      }
      case FIXED_PHONE -> {
        jsonGenerator.writeString(SensitiveInfoUtils.fixedPhone(s));
      }
      case MOBILE_PHONE -> {
        jsonGenerator.writeString(SensitiveInfoUtils.mobilePhone(s));
      }
      case ADDRESS -> {
        jsonGenerator.writeString(SensitiveInfoUtils.address(s, 4));
      }
      case EMAIL -> {
        jsonGenerator.writeString(SensitiveInfoUtils.email(s));
      }
      case BANK_CARD -> {
        jsonGenerator.writeString(SensitiveInfoUtils.bankCard(s));
      }
      case CNAPS_CODE -> {
        jsonGenerator.writeString(SensitiveInfoUtils.cnapsCode(s));
      }
    }
  }

  @Override
  public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
      throws JsonMappingException {

    if (property != null && property.getType().getRawClass() == String.class) {
      SensitiveInfo sensitiveInfo = property.getAnnotation(SensitiveInfo.class);
      if (sensitiveInfo != null) {
        return new SensitiveInfoSerialize(sensitiveInfo.type());
      }
    }

    return this;
  }
}
