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

package io.github.code13.jakartaee.beanvalidation.customer;

import io.github.code13.jakartaee.beanvalidation.ValidationUtils;
import java.io.Serializable;
import java.lang.reflect.Method;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * CustomContainerElementValidationRunner.
 *
 * <p>自定义元素类型元素验证
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/10/24 17:09
 */
class CustomContainerElementValidationRunner {

  @Test
  @DisplayName("test")
  void test() throws NoSuchMethodException {

    ExecutableValidator executableValidator =
        ValidationUtils.obtainValidatorFactory()
            .usingContext()
            .addValueExtractor(new ResultValueExtractor())
            .getValidator()
            .forExecutables();

    RoomService roomService = new RoomService();
    Method method = roomService.getClass().getMethod("room");
    Result<@NotNull @Valid Room> result = roomService.room();

    ValidationUtils.validate(
        () -> executableValidator.validateReturnValue(roomService, method, result));
  }

  static class RoomService {

    public Result<@NotNull @Valid Room> room() {
      Room room = new Room();
      room.name = "AAAAA";
      Result<Room> result = new Result<>();
      result.setData(room);
      return result;
    }
  }

  static class ResultValueExtractor implements ValueExtractor<Result<@ExtractedValue ?>> {

    @Override
    public void extractValues(Result<?> originalValue, ValueReceiver receiver) {
      receiver.value(null, originalValue.getData());
    }
  }

  @Data
  static class Result<T> implements Serializable {

    private static final long serialVersionUID = -5279151785988878136L;

    private boolean success = true;
    private T data = null;

    private String errCode;
    private String errMsg;
  }

  @Data
  static class Room {
    @NotNull public String name;
    @AssertTrue public boolean finished;
  }
}
