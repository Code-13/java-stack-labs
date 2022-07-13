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

package io.github.code13.javastack.books.jcip.ch10_avoiding_liveness_hazards;

import io.github.code13.javastack.books.jcip.GuardedBy;
import io.github.code13.javastack.books.jcip.ThreadSafe;
import io.github.code13.javastack.books.jcip.ch4_composing_objects.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * CooperatingNoDeadlock.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 12/16/2021 3:54 PM
 */
public class CooperatingNoDeadlock {

  @ThreadSafe
  class Taxi {
    @GuardedBy("this")
    private Point location, destination;
    private final Dispatcher dispatcher;

    public Taxi(Dispatcher dispatcher) {
      this.dispatcher = dispatcher;
    }

    public synchronized Point getLocation() {
      return location;
    }

    public synchronized void setLocation(Point location) {
      boolean reachedDestination;
      synchronized (this) {
        this.location = location;
        reachedDestination = location.equals(destination);
      }
      if (reachedDestination) {
        dispatcher.notifyAvailable(this);
      }
    }

    public synchronized Point getDestination() {
      return destination;
    }

    public synchronized void setDestination(Point destination) {
      this.destination = destination;
    }
  }

  @ThreadSafe
  class Dispatcher {
    @GuardedBy("this")
    private final Set<Taxi> taxis;

    @GuardedBy("this")
    private final Set<Taxi> availableTaxis;

    public Dispatcher() {
      taxis = new HashSet<Taxi>();
      availableTaxis = new HashSet<Taxi>();
    }

    public synchronized void notifyAvailable(Taxi taxi) {
      availableTaxis.add(taxi);
    }

    public Image getImage() {
      Set<Taxi> copy;
      synchronized (this) {
        copy = new HashSet<Taxi>(taxis);
      }
      Image image = new Image();
      for (Taxi t : copy) {
        image.drawMarker(t.getLocation());
      }
      return image;
    }
  }

  class Image {
    public void drawMarker(Point p) {}
  }
}
