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

package io.github.code13.libs.jackson2.enums.deserialization.customdeserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

/**
 * CustomDistanceDeserializer.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/21 09:57
 */
class CustomDistanceDeserializer extends StdDeserializer<Distance> {

  private static final long serialVersionUID = 3066061394480655146L;

  public CustomDistanceDeserializer() {
    super(Distance.class);
  }

  @Override
  public Distance deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JacksonException {

    JsonNode node = p.getCodec().readTree(p);

    String unit = node.get("unit").asText();
    double meters = node.get("meters").asDouble();

    for (Distance distance : Distance.values()) {
      if (distance.getUnit().equals(unit) && Double.compare(distance.getMeters(), meters) == 0) {
        return distance;
      }
    }

    return null;
  }
}
