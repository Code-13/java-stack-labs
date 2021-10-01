/*
 *     Copyright 2021-present the original author or authors.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package io.github.code13.javastack.libs.redisson.additionalfeatures;

import io.github.code13.javastack.libs.redisson.RedissonClientBuilder;
import java.util.Collections;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RScript.Mode;
import org.redisson.api.RScript.ReturnType;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

/**
 * ScriptingRunner.
 *
 * <p>Redisson provides RScript object to execute Lua script. It has atomicity property and used to
 * process data on Redis side. Script could be executed in two modes:
 *
 * <p>Mode.READ_ONLY scripts are executed on Redis slaves if they available Mode.READ_WRITE scripts
 * are executed on both Slave and Master Redis nodes
 *
 * <p>One of the follow types returned as a script result object:
 *
 * <p>ReturnType.BOOLEAN - Boolean type. ReturnType.INTEGER - Integer type. ReturnType.MULTI - List
 * type of user defined type. ReturnType.STATUS - Lua String type. ReturnType.VALUE - User defined
 * type. ReturnType.MAPVALUE - Map value type. ReturnType.MAPVALUELIST - List of Map value type.
 *
 * @author <a href="https://github.com/Code-13/">code13</a>
 * @date 2021/10/1 18:35
 */
@DisplayName("Scripting")
class ScriptingRunner {

  static RedissonClient redissonClient;

  @BeforeAll
  static void setup() {
    redissonClient = RedissonClientBuilder.build("127.0.0.1", 6379, "", 0);
  }

  @Test
  @DisplayName("Scripting")
  void examples() {
    RBucket<String> bucket = redissonClient.<String>getBucket("foo");
    bucket.set("bar");

    RScript script = redissonClient.getScript(StringCodec.INSTANCE);

    String r = script.eval(Mode.READ_ONLY, """
        return redis.call('get', 'foo')
        """, ReturnType.VALUE);


    String res = script.scriptLoad("return redis.call('get', 'foo')");
    Future<Object> r1 = redissonClient.getScript().evalShaAsync(Mode.READ_ONLY,
        res,
        RScript.ReturnType.VALUE, Collections.emptyList());
  }
}
