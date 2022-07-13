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

package io.github.code13.javastack.libs.spatial4j;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.impl.PointImpl;

/**
 * Spatial4jUtils.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/9/19 15:55
 */
public final class Spatial4jUtils {

  /**
   * Gets distance with spatial4j.
   *
   * @param lat1 the lat 1
   * @param lng1 the lng 1
   * @param lat2 the lat 2
   * @param lng2 the lng 2
   * @return the distance with spatial 4 j
   */
  public static double getDistanceWithSpatial4j(
      double lat1, double lng1, double lat2, double lng2) {
    SpatialContext geo = SpatialContext.GEO;
    return geo.getDistCalc()
            .distance(new PointImpl(lng1, lat1, geo), new PointImpl(lng2, lat2, geo))
        * DistanceUtils.DEG_TO_KM
        * 1000;
  }
}
