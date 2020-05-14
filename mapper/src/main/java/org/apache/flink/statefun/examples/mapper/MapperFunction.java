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
package org.apache.flink.statefun.examples.mapper;

import org.apache.flink.statefun.examples.common.Identifiers;
import org.apache.flink.statefun.sdk.Context;
import org.apache.flink.statefun.sdk.StatefulFunction;

/**
 * A stateful function that generates a unique greeting for each user based on how many times that
 * user has been seen by the system.
 */
final class MapperFunction implements StatefulFunction {

  @Override
  public void invoke(Context context, Object input) {
    String name = (String) input;
    String out = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    System.out.println("Transformed \"" + name + "\" to \"" + out + "\"");
    context.send(Identifiers.GREETER_TYPE, out.substring(0, 1) ,out);
  }

}
