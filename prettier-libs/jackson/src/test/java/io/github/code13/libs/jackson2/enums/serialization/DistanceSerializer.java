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

package io.github.code13.libs.jackson2.enums.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * DistanceSerializer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/20 10:57
 */
public class DistanceSerializer extends StdSerializer<Distance> {

  public DistanceSerializer() {
    super(Distance.class);
  }

  @Override
  public void serialize(Distance distance, JsonGenerator gen, SerializerProvider provider)
      throws IOException {

    gen.writeStartObject();
    gen.writeFieldName("name");
    gen.writeString(distance.name());
    gen.writeFieldName("unit");
    gen.writeString(distance.getUnit());
    gen.writeFieldName("meters");
    gen.writeNumber(distance.getMeters());
    gen.writeEndObject();
  }
}
