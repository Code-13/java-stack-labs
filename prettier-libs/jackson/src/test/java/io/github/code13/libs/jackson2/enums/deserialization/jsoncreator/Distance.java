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

package io.github.code13.libs.jackson2.enums.deserialization.jsoncreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Distance.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @since 2022/6/20 17:03
 */
enum Distance {
  KILOMETER("km", 1000),
  MILE("miles", 1609.34),
  METER("meters", 1),
  INCH("inches", 0.0254),
  CENTIMETER("cm", 0.01),
  MILLIMETER("mm", 0.001);

  private String unit;
  private double meters;

  private Distance(String unit, double meters) {
    this.unit = unit;
    this.meters = meters;
  }

  public void setMeters(double meters) {
    this.meters = meters;
  }

  public double getMeters() {
    return meters;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @JsonCreator
  public static Distance forValues(
      @JsonProperty("unit") String unit, @JsonProperty("meters") double meters) {
    for (Distance distance : values()) {
      if (distance.unit.equals(unit) && Double.compare(distance.meters, meters) == 0) {
        return distance;
      }
    }
    return null;
  }
}
