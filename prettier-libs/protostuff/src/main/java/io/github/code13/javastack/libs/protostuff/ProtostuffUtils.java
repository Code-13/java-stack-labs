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

package io.github.code13.javastack.libs.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * ProtostuffUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/15 13:58
 */
public final class ProtostuffUtils {

  private static Objenesis objenesis = new ObjenesisStd();

  @SuppressWarnings("unchecked")
  public static <T> byte[] serialize(T obj) {
    Class<T> objClass = (Class<T>) obj.getClass();
    Schema<T> schema = getSchema(objClass);
    byte[] data;
    LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    try {
      data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    } finally {
      buffer.clear();
    }
    return data;
  }

  public static <T> T deserialize(byte[] data, Class<T> clazz) {
    T obj = objenesis.newInstance(clazz);
    Schema<T> schema = getSchema(clazz);
    ProtostuffIOUtil.mergeFrom(data, obj, schema);
    return obj;
  }

  private static <T> Schema<T> getSchema(Class<T> objClass) {
    return RuntimeSchema.getSchema(objClass);
  }

  private ProtostuffUtils() {
    throw new UnsupportedOperationException(String.format("%s is Utils", getClass().getName()));
  }
}
