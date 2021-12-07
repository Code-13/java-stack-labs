/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.books.jcip.ch4_composing_objects;

import io.github.code13.javastack.books.jcip.ThreadSafe;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PublishingVehicleTracker.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/7/2021 3:42 PM
 */
@ThreadSafe
public class PublishingVehicleTracker {

  private final Map<String, SafePoint> locations;
  private final Map<String, SafePoint> unmodifiableMap;

  public PublishingVehicleTracker(Map<String, SafePoint> locations) {
    this.locations = new ConcurrentHashMap<>(locations);
    unmodifiableMap = Collections.unmodifiableMap(this.locations);
  }

  public Map<String, SafePoint> getLocations() {
    return unmodifiableMap;
  }

  public SafePoint getLocation(String id) {
    return locations.get(id);
  }

  public void setLocation(String id, int x, int y) {
    if (!locations.containsKey(id)) {
      throw new IllegalArgumentException("invalid vehicle name: " + id);
    }
    locations.get(id).set(x, y);
  }
}
