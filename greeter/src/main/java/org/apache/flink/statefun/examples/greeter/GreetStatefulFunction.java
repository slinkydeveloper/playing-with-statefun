/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flink.statefun.examples.greeter;

import org.apache.flink.statefun.examples.common.Identifiers;
import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.StatefulFunction;
import org.apache.flink.statefun.sdk.annotations.Persisted;
import org.apache.flink.statefun.sdk.state.PersistedValue;

/**
 * A stateful function that generates a unique greeting for each user based on how many times that
 * user has been seen by the system.
 */
final class GreetStatefulFunction implements StatefulFunction {

  /**
   * The persisted value for maintaining state about a particular user. The value returned by this
   * field is always scoped to the current user. seenCount is the number of times the user has been
   * greeted.
   */
  @Persisted
  private final PersistedValue<Integer> seenCount = PersistedValue.of("seen-count", Integer.class);

  @Override
  public void invoke(Context context, Object input) {
    String name = (String) input;
    String out = computePersonalizedGreeting(name);
    System.out.println("Transformed \"" + name + "\" to \"" + out + "\"");
    context.send(Identifiers.OUTBOUND_IDENTIFIER, out);
  }

  private String computePersonalizedGreeting(String name) {
    final int seen = seenCount.getOrDefault(0);
    seenCount.set(seen + 1);

    return greetText(name, seen);
  }

  private static String greetText(String name, int seen) {
    switch (seen) {
      case 0:
        return String.format("Hello %s ! \uD83D\uDE0E", name);
      case 1:
        return String.format("Hello again %s ! \uD83E\uDD17", name);
      case 2:
        return String.format("Third time is a charm! %s! \uD83E\uDD73", name);
      case 3:
        return String.format("Happy to see you once again %s ! \uD83D\uDE32", name);
      default:
        return String.format("Hello at the %d-th time %s \uD83D\uDE4C", seen + 1, name);
    }
  }
}
